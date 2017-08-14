package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.CreateOrderEntity;
import cn.antke.ezy.network.entities.ListDialogEntity;
import cn.antke.ezy.network.entities.ListDialogPageEntity;
import cn.antke.ezy.network.entities.OrderEntity;
import cn.antke.ezy.network.entities.OrderListPageEntity;
import cn.antke.ezy.pay.controller.OnlinePayActivity;
import cn.antke.ezy.pay.controller.PayResultActivity;
import cn.antke.ezy.person.adapter.OrderListAdapter;
import cn.antke.ezy.utils.DialogUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_ALL;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_DELIVED;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_DELIVING;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_FINISHED;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_PAYING;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_REFUND;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_FIVE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_FOUR;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_SEVEN;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_SIX;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/10.
 * 订单列表
 */

public class OrderListActivity extends ToolBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private int orderState;
    private FootLoadingListView orderListView;
    private List<OrderEntity> entities;
    private OrderListAdapter adapter;
    private String cancelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_order);
        orderState = getIntent().getIntExtra(EXTRA_TYPE, ORDERSTATE_ALL);
        setLeftTitle(getOrderTitle());

        orderListView = (FootLoadingListView) findViewById(R.id.order_list);
        initLoadingView(this);
        setLoadingStatus(LoadingStatus.GONE);


        orderListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        orderListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(false);
    }

    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        String url = Constants.Urls.URL_POST_ORDER_LIST;
        if (isMore) {
            request = REQUEST_NET_TWO;
            page = adapter.getPage() + 1;
        }

        if (ORDERSTATE_REFUND == orderState) {
            url = Constants.Urls.URL_POST_REFUND_LIST;
        } else {
            params.put("order_status", String.valueOf(orderState));
        }
        params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
        params.put(CommonConstant.PAGENUM, String.valueOf(page));
        requestHttpData(url, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        orderListView.setOnRefreshComplete();
        OrderListPageEntity pageEntity = Parsers.getOrders(data);

        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pageEntity != null) {
                    entities = pageEntity.getOrderEntities();
                    if (entities != null && entities.size() > 0) {
                        adapter = new OrderListAdapter(this, entities, this);
                        orderListView.setAdapter(adapter);
                        if (pageEntity.getTotalPager() > adapter.getPage()) {
                            orderListView.setCanAddMore(true);
                        } else {
                            orderListView.setCanAddMore(false);
                        }
                    } else {
                        if (adapter != null) {
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            case REQUEST_NET_TWO://加载更多
                if (pageEntity != null) {
                    entities = pageEntity.getOrderEntities();
                    if (entities != null && entities.size() > 0) {
                        adapter.addDatas(entities);
                        adapter.notifyDataSetChanged();
                        if (pageEntity.getTotalPager() > adapter.getPage()) {
                            orderListView.setCanAddMore(true);
                        } else {
                            orderListView.setCanAddMore(false);
                        }
                    }
                }
                break;
            case REQUEST_NET_THREE://取消订单
            case REQUEST_NET_FOUR://删除订单
            case REQUEST_NET_FIVE://确认收货
            case REQUEST_NET_SIX://取消退款
                loadData(false);
                break;
            case REQUEST_NET_SEVEN://付款
                CreateOrderEntity createOrder = Parsers.getCreateOrder(data);
                if (StringUtil.parseInt(createOrder.getOrderTotal(), 0) > 0) {
                    OnlinePayActivity.startOnlinePayActivity(this, createOrder.getOrderId(), createOrder.getOrderTotal());
                } else {
                    PayResultActivity.startPayResultActivity(this, createOrder.getTotal(), createOrder.getPayType(), 0);
                }
                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        closeProgressDialog();
        orderListView.setOnRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loading_layout:
                loadData(false);
                break;
            case R.id.item_order_btn1:
                OrderEntity entity1 = (OrderEntity) v.getTag();
                //订单状态：0 全部 1 待支付 2 已支付 3 已发货 4 已完成 5 取消 6、申请中；7、退款中；8、已退款；9、拒绝；10、取消
                if (entity1 != null) {
                    String status = entity1.getStatus();
                    switch (status) {
                        case "1"://取消订单
                            String[] datas = new String[]{"我不想买了",
                                    "信息填写错误，重新拍",
                                    "卖家缺货",
                                    "其他原因"};
                            List<ListDialogEntity> entities = new ArrayList<>();
                            for (String data : datas) {
                                entities.add(new ListDialogEntity(data, false));
                            }

                            ListDialogPageEntity listDialogPageEntity = new ListDialogPageEntity(entities);
                            startActivityForResult(new Intent(OrderListActivity.this, ListDialogActivity.class).putExtra(EXTRA_ENTITY, listDialogPageEntity), REQUEST_ACT_ONE);
                            cancelId = entity1.getOrderCode();
                            break;
                        case "3"://查看物流
                            String orderCode = entity1.getOrderCode();
                            LogisticDetailActivity.startLogisticActivity(this, orderCode);
                            break;
                        case "6"://退款详情
                            OrderDetailActivity.startOrderDetailActivity(this, entity1);
                            break;
                    }
                }
                break;
            case R.id.item_order_btn2:
                OrderEntity entity2 = (OrderEntity) v.getTag();
                //订单状态：0 全部 1 待支付 2 已支付 3 已发货 4 已完成 5 取消 6、申请中；7、退款中；8、已退款；9、拒绝；10、取消
                if (entity2 != null) {
                    String status = entity2.getStatus();
                    int totalMoney = Integer.parseInt(entity2.getOrderTotal()) + Integer.parseInt(entity2.getLogisticCost());
                    int totalIntegral = Integer.parseInt(entity2.getOrderTotalInregral()) + Integer.parseInt(entity2.getLogisticCostIntegral());
                    switch (status) {
                        case "1"://付款
//                            CreateOrderEntity createOrder = Parsers.getCreateOrder(data);
                            //如果有积分需要支付，先通过密码支付积分，如果没有则直接进入支付方式页面
                            if (!"1".equals(entity2.getIsPayIntegral()) && StringUtil.parseInt(entity2.getOrderTotalInregral(), 0) > 0) {
                                DialogUtils.showPwdInputDialog(this, (v1, editText) -> {
                                    String pwd = editText.getText().toString();
                                    showProgressDialog();
                                    IdentityHashMap<String, String> params = new IdentityHashMap<>();
                                    params.put("order_id", entity2.getTradeNo());
                                    params.put("integral_password", pwd);
                                    requestHttpData(Constants.Urls.URL_POST_INTEGRAL_PAY, CommonConstant.REQUEST_NET_SEVEN, FProtocol.HttpMethod.POST, params);
                                    DialogUtils.closeDialog();
                                }, view -> DialogUtils.closeDialog());
                            } else {
                                OnlinePayActivity.startOnlinePayActivity(this, entity2.getTradeNo(), String.valueOf(totalMoney));
                            }
                            break;
                        case "2"://退款
                            String orderCode = entity2.getOrderCode();
                            RefundApplyActivity.startRefundApplyActivity(this, entity2, String.valueOf(totalMoney), String.valueOf(totalIntegral), orderCode);
                            break;
                        case "3"://完成(确认收货)
                            getConfirm(entity2.getOrderId());
                            break;
                        case "4"://查看物流
                            String orderCode1 = entity2.getOrderCode();
                            LogisticDetailActivity.startLogisticActivity(this, orderCode1);
                            break;
                        case "5"://删除
                            String deleteId = entity2.getOrderId();
                            deleteOrder(deleteId);
                            break;
                        case "6"://取消退款
                            String cancelId = entity2.getRefundId();
                            cancelRefund(cancelId);
                            break;
                        case "7":
                        case "8":
                        case "9"://退款详情
                            OrderDetailActivity.startOrderDetailActivity(this, entity2);
                            break;
                    }
                }
                break;
        }
    }

    public String getOrderTitle() {
        String orderTitle = "";
        switch (orderState) {
            case ORDERSTATE_ALL:
                orderTitle = getString(R.string.person_all_order);
                break;
            case ORDERSTATE_PAYING:
                orderTitle = getString(R.string.order, getString(R.string.person_order_paying));
                break;
            case ORDERSTATE_DELIVING:
                orderTitle = getString(R.string.order, getString(R.string.person_order_deliving));
                break;
            case ORDERSTATE_DELIVED:
                orderTitle = getString(R.string.order, getString(R.string.person_order_delived));
                break;
            case ORDERSTATE_FINISHED:
                orderTitle = getString(R.string.order, getString(R.string.person_order_finished));
                break;
            case ORDERSTATE_REFUND:
                orderTitle = getString(R.string.order, getString(R.string.person_order_refund));
                break;
        }
        return orderTitle;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderDetailActivity.startOrderDetailActivity(this, entities.get(position));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_ACT_ONE:
                    String reason = data.getStringExtra(EXTRA_ENTITY);
                    cancelOrder(reason);
                    break;
            }
        }
    }

    /*取消退款*/
    private void cancelRefund(String cancelId) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("refund_id", cancelId);
        requestHttpData(Constants.Urls.URL_POST_REFUND_CANCEL, REQUEST_NET_SIX, FProtocol.HttpMethod.POST, params);
    }

    /*
    * 确认收货*/
    private void getConfirm(String deleteId) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("order_id", deleteId);
        requestHttpData(Constants.Urls.URL_POST_GET_CONFIRM, REQUEST_NET_FIVE, FProtocol.HttpMethod.POST, params);
    }

    private void cancelOrder(String reason) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("order_id", cancelId);
        params.put("reason", reason);
        requestHttpData(Constants.Urls.URL_POST_ORDER_CANCEL, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    private void deleteOrder(String deleteId) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("order_id", deleteId);
        requestHttpData(Constants.Urls.URL_POST_ORDER_DELETE, REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
    }
}
