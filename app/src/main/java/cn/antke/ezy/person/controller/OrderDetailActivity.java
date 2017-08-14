package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.CreateOrderEntity;
import cn.antke.ezy.network.entities.ListDialogEntity;
import cn.antke.ezy.network.entities.ListDialogPageEntity;
import cn.antke.ezy.network.entities.OrderDetailEntity;
import cn.antke.ezy.network.entities.OrderEntity;
import cn.antke.ezy.network.entities.OrderGoodEntity;
import cn.antke.ezy.pay.controller.OnlinePayActivity;
import cn.antke.ezy.pay.controller.PayResultActivity;
import cn.antke.ezy.person.adapter.OrderGoodAdapter;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.TimeUtil;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_FIVE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_FOUR;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_SIX;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/15.
 * 购物订单详情
 */

public class OrderDetailActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.order_detail_list)
    private ListView orderList;
    @ViewInject(R.id.order_detail_price)
    private TextView orderAllPrice;
    @ViewInject(R.id.order_detail_btn1)
    private TextView orderBtn1;
    @ViewInject(R.id.order_detail_btn2)
    private TextView orderBtn2;
    @ViewInject(R.id.order_detail_bottom_rl)
    private RelativeLayout orderDetailBottom;

    private TextView user;
    private TextView phone;
    private TextView address;
    private TextView store;
    private TextView status;
    private TextView goodPrice;
    private TextView freight;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private RelativeLayout topRl;
    private View view;
    private View refundView;
    private LinearLayout refundLl;
    private TextView refundReason;
    private TextView refundExplain;
    private OrderDetailEntity orderDetailEntity;
    private TextView outTime;
    private String orderId;
    private String orderStatus;
    private String refundId;

    public static void startOrderDetailActivity(Context context, OrderEntity entity) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(EXTRA_ENTITY, entity);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_detail);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.person_order_detail));
        OrderEntity entity = getIntent().getParcelableExtra(EXTRA_ENTITY);
        orderId = entity.getOrderId();
        refundId = entity.getRefundId();
        orderStatus = entity.getStatus();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initView() {
        View topView = getLayoutInflater().inflate(R.layout.act_order_detail_top, null);
        outTime = (TextView) topView.findViewById(R.id.order_detail_outtime);
        topRl = (RelativeLayout) topView.findViewById(R.id.order_detail_top_rl);
        view = topView.findViewById(R.id.order_detail_top_view);
        user = (TextView) topView.findViewById(R.id.order_detail_consignee);
        phone = (TextView) topView.findViewById(R.id.order_detail_phone);
        address = (TextView) topView.findViewById(R.id.order_detail_address);
        store = (TextView) topView.findViewById(R.id.order_detail_store);
        status = (TextView) topView.findViewById(R.id.order_detail_status);
        View bottomView = getLayoutInflater().inflate(R.layout.act_order_detail_bottom, null);
        goodPrice = (TextView) bottomView.findViewById(R.id.order_detail_goods_price);
        freight = (TextView) bottomView.findViewById(R.id.order_detail_freight);
        refundView = bottomView.findViewById(R.id.order_detail_refund_view);
        refundLl = (LinearLayout) bottomView.findViewById(R.id.order_detail_refund_ll);
        refundReason = (TextView) bottomView.findViewById(R.id.order_detail_refund_reason);
        refundExplain = (TextView) bottomView.findViewById(R.id.order_detail_refund_explain);
        textView1 = (TextView) bottomView.findViewById(R.id.order_detail_text1);
        textView2 = (TextView) bottomView.findViewById(R.id.order_detail_text2);
        textView3 = (TextView) bottomView.findViewById(R.id.order_detail_text3);
        orderList.addHeaderView(topView);
        orderList.addFooterView(bottomView);
    }

    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        String url = Constants.Urls.URL_POST_ORDER_DETAIL;
        if ("6".equals(orderStatus) || "7".equals(orderStatus) || "8".equals(orderStatus) || "9".equals(orderStatus)||"10".equals(orderStatus)) {
            url = Constants.Urls.URL_POST_REFUND_DETAIL;
            params.put("refund_id", refundId);
        } else {
            params.put("order_id", orderId);
        }
        requestHttpData(url, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                orderDetailEntity = Parsers.getOrderDetail(data);
                if (orderDetailEntity != null) {
                    setData();
                }
                break;
            case REQUEST_NET_TWO://取消订单
                loadData();
                break;
            case REQUEST_NET_THREE://删除订单
                finish();
                break;
            case REQUEST_NET_FOUR://取消退款
            case REQUEST_NET_FIVE://确认收货
                loadData();
                break;
            case REQUEST_NET_SIX://付款
                CreateOrderEntity createOrder = Parsers.getCreateOrder(data);
                if (StringUtil.parseInt(createOrder.getOrderTotal(), 0) > 0) {
                    OnlinePayActivity.startOnlinePayActivity(this, createOrder.getOrderId(), createOrder.getOrderTotal());
                } else {
                    PayResultActivity.startPayResultActivity(this, createOrder.getTotal(), createOrder.getPayType(), 0);
                }
                break;
        }
    }

    private void setData() {
        List<OrderEntity> orderEntities = orderDetailEntity.getOrderEntities();
        OrderEntity orderEntity = null;
        if (orderEntities != null && orderEntities.size() > 0) {
            orderEntity = orderEntities.get(0);
        } else {
            return;
        }
        List<OrderGoodEntity> goodEntities = orderEntity.getGoodEntities();
        OrderGoodAdapter goodAdapter = new OrderGoodAdapter(this, goodEntities);
        orderList.setAdapter(goodAdapter);

        if (!StringUtil.isEmpty(orderDetailEntity.getOutTime())) {
            int countDownTime = Integer.parseInt(orderDetailEntity.getOutTime());
            if (countDownTime > 0) {
                outTime.setVisibility(View.VISIBLE);
                new CountDownTimer(countDownTime * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        outTime.setText(TimeUtil.getFormatTime2(millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        status.setText(getString(R.string.person_order_close));
//                        orderBottomLayout.setVisibility(View.GONE);
                        outTime.setVisibility(View.GONE);
                        cancel();
                    }
                }.start();
            } else {
                outTime.setVisibility(View.GONE);
            }
        } else {
            outTime.setVisibility(View.GONE);
        }

        String totalPrice = "";
        //订单状态：0 全部 1 待支付 2 已支付 3 已发货 4 已完成 5 取消订单 6、申请中；7、退款中；8、已退款；9、拒绝；10、取消退款
        switch (orderDetailEntity.getStatus()) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                topRl.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                user.setText(getString(R.string.person_address_user, orderDetailEntity.getUserName()));
                phone.setText(orderDetailEntity.getPhone());
                address.setText(getString(R.string.person_address_address, orderDetailEntity.getAddress()));
                refundLl.setVisibility(View.GONE);
                refundView.setVisibility(View.GONE);
                textView1.setText(getString(R.string.person_orderdetail_order_number, orderDetailEntity.getOrderCode()));
                if (!StringUtil.isEmpty(orderDetailEntity.getCreatData())) {
                    textView2.setText(getString(R.string.person_orderdetail_create_time, orderDetailEntity.getCreatData()));
                }
                if (!StringUtil.isEmpty(orderDetailEntity.getPayTime())) {
                    textView3.setText(getString(R.string.person_orderdetail_pay_time, orderDetailEntity.getPayTime()));
                }
                totalPrice = setTotalPrice(R.string.person_orderdetail_order_price);
                break;

            case "6":
                setRefundView();
                totalPrice = setTotalPrice(R.string.person_orderdetail_refund_price);
                break;
            case "7":
                setRefundView();
                orderDetailBottom.setVisibility(View.GONE);
                break;
            case "8":
                setRefundView();
                if (!StringUtil.isEmpty(orderDetailEntity.getRefundTime())) {
                    textView3.setText(getString(R.string.person_orderdetail_refund_time, orderDetailEntity.getRefundTime()));
                }
                totalPrice = setTotalPrice(R.string.person_orderdetail_refund_price);
                break;
            case "9":
                setRefundView();
                if (!StringUtil.isEmpty(orderDetailEntity.getRefundTime())) {
                    textView3.setText(getString(R.string.person_orderdetail_refused_time, orderDetailEntity.getRefundTime()));
                }
                orderDetailBottom.setVisibility(View.GONE);
                break;
            case "10":
                setRefundView();
                if (!StringUtil.isEmpty(orderDetailEntity.getRefundTime())) {
                    textView3.setText(getString(R.string.person_orderdetail_cancel_time, orderDetailEntity.getRefundTime()));
                }
                orderDetailBottom.setVisibility(View.GONE);
                break;
        }

        store.setText(orderEntity.getStoreName());
        status.setText(orderDetailEntity.getStatusName());
        String orderPrice = "";
        if (!StringUtil.isEmpty(orderEntity.getOrderTotal()) && !"0".equals(orderEntity.getOrderTotal())) {
            if (!StringUtil.isEmpty(orderEntity.getOrderTotalInregral()) && !"0".equals(orderEntity.getOrderTotalInregral())) {
                orderPrice = getString(R.string.person_order_good_price, getString(R.string.person_order_money_integral, orderEntity.getOrderTotal(), orderEntity.getOrderTotalInregral()));
            } else {
                orderPrice = getString(R.string.person_order_good_price, getString(R.string.product_sell_price2, orderEntity.getOrderTotal()));
            }
        } else {
            if (!StringUtil.isEmpty(orderEntity.getOrderTotalInregral()) && !"0".equals(orderEntity.getOrderTotalInregral())) {
                orderPrice = getString(R.string.person_order_good_price, getString(R.string.product_sell_integral, orderEntity.getOrderTotalInregral()));
            } else {
                goodPrice.setVisibility(View.GONE);
            }
        }
        goodPrice.setText(orderPrice);

        String logisticPrice = "";
        if (!StringUtil.isEmpty(orderEntity.getLogisticCost()) && !"0".equals(orderEntity.getLogisticCost())) {
            if (!StringUtil.isEmpty(orderEntity.getLogisticCostIntegral()) && !"0".equals(orderEntity.getLogisticCostIntegral())) {
                logisticPrice = getString(R.string.person_order_freight, getString(R.string.person_order_money_integral, orderEntity.getLogisticCost(), orderEntity.getLogisticCostIntegral()));
            } else {
                logisticPrice = getString(R.string.person_order_freight, getString(R.string.product_sell_price2, orderEntity.getLogisticCost()));
            }
        } else {
            if (!StringUtil.isEmpty(orderEntity.getLogisticCostIntegral()) && !"0".equals(orderEntity.getLogisticCostIntegral())) {
                logisticPrice = getString(R.string.person_order_freight, getString(R.string.product_sell_integral, orderEntity.getLogisticCostIntegral()));
            } else {
                freight.setVisibility(View.GONE);
            }
        }
        freight.setText(logisticPrice);
        orderAllPrice.setText(totalPrice);
        setBtn();
    }

    private void setRefundView() {
        refundLl.setVisibility(View.VISIBLE);
        refundView.setVisibility(View.VISIBLE);
        topRl.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        refundReason.setText(orderDetailEntity.getRefundReason());
        refundExplain.setText(orderDetailEntity.getRefundDescribe());
        textView1.setText(getString(R.string.person_orderdetail_order_number, orderDetailEntity.getRefundCode()));
        if (!StringUtil.isEmpty(orderDetailEntity.getApplyTime())) {
            textView2.setText(getString(R.string.person_orderdetail_refund_apply_time, orderDetailEntity.getApplyTime()));
        }
    }

    private String setTotalPrice(int resId) {
        String totalPrice;
        if (!StringUtil.isEmpty(orderDetailEntity.getOrderPrice()) && !"0".equals(orderDetailEntity.getOrderPrice())) {
            if (!StringUtil.isEmpty(orderDetailEntity.getOrderIntegral()) && !"0".equals(orderDetailEntity.getOrderIntegral())) {
                totalPrice = getString(resId, getString(R.string.person_order_money_integral, orderDetailEntity.getOrderPrice(), orderDetailEntity.getOrderIntegral()));
            } else {
                totalPrice = getString(resId, getString(R.string.product_sell_price2, orderDetailEntity.getOrderPrice()));
            }
        } else {
            if (!StringUtil.isEmpty(orderDetailEntity.getOrderIntegral()) && !"0".equals(orderDetailEntity.getOrderIntegral())) {
                totalPrice = getString(resId,
                        getString(R.string.product_sell_integral, orderDetailEntity.getOrderIntegral()));
            } else {
                totalPrice = getString(resId, "");
            }
        }
        return totalPrice;
    }

    private void setBtn() {
        if (orderDetailEntity != null) {
            //订单状态：1，待支付；2，待发货；3，已发货； 4.已完成；5，取消订单（待支付状态）6、退款申请；7.退款中；8.已退款；9，拒绝 。
            switch (orderDetailEntity.getStatus()) {
                case "1"://待支付
                    orderBtn1.setVisibility(View.VISIBLE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn1.setText(getString(R.string.person_order_cancel));
                    orderBtn2.setText(getString(R.string.person_order_pay));
                    break;
                case "2"://待发货
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(getString(R.string.person_order_refund));
                    break;
                case "3"://已发货
                    orderBtn1.setVisibility(View.VISIBLE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn1.setText(getString(R.string.person_order_logistic_detail));
                    orderBtn2.setText(getString(R.string.person_order_get_ensure));
                    break;
                case "4"://已完成
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(getString(R.string.person_order_logistic_detail));
//                    orderBtn2.setText(getString(R.string.person_order_buy_again));
                    break;
                case "5"://取消
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(getString(R.string.delete));
                    break;
                case "6"://退款申请
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(getString(R.string.person_order_refund_cancel));
                    break;
                case "7"://退款中
                case "8"://已退款
                case "9"://拒绝退款
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.GONE);
                    break;
            }
            orderBtn1.setOnClickListener(this);
            orderBtn2.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_btn1:
                switch (orderDetailEntity.getStatus()) {
                    case "1"://取消订单
                        String[] datas = new String[]{"我不想买了",
                                "信息填写错误，重新拍",
                                "卖家缺货",
                                "其他原因"};
                        List<ListDialogEntity> entities = new ArrayList<>();
                        for (int i = 0; i < datas.length; i++) {
                            entities.add(new ListDialogEntity(datas[i], false));
                        }
                        ListDialogPageEntity listDialogPageEntity = new ListDialogPageEntity(entities);
                        startActivityForResult(new Intent(OrderDetailActivity.this, ListDialogActivity.class).putExtra(EXTRA_ENTITY, listDialogPageEntity), REQUEST_ACT_ONE);
                        break;
                    case "3"://查看物流
                        String orderCode = orderDetailEntity.getOrderCode();
                        LogisticDetailActivity.startLogisticActivity(this, orderCode);
                        break;
                }
                break;
            case R.id.order_detail_btn2:
                switch (orderDetailEntity.getStatus()) {
                    case "1"://付款
                        if (!"1".equals(orderDetailEntity.getIsPayIntegral()) && StringUtil.parseInt(orderDetailEntity.getOrderIntegral(), 0) > 0) {
                            DialogUtils.showPwdInputDialog(this, (v1, editText) -> {
                                String pwd = editText.getText().toString();
                                showProgressDialog();
                                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                                params.put("order_id", orderDetailEntity.getTradeNo());
                                params.put("integral_password", pwd);
                                requestHttpData(Constants.Urls.URL_POST_INTEGRAL_PAY, REQUEST_NET_SIX, FProtocol.HttpMethod.POST, params);
                                DialogUtils.closeDialog();
                            }, view1 -> DialogUtils.closeDialog());
                        } else {
                            OnlinePayActivity.startOnlinePayActivity(this, orderDetailEntity.getTradeNo(), orderDetailEntity.getOrderPrice());
                        }
                        break;
                    case "2"://退款
                        String totalPrice = orderDetailEntity.getOrderPrice();
                        String totalIntegral = orderDetailEntity.getOrderIntegral();
                        String orderCode = orderDetailEntity.getOrderCode();
                        RefundApplyActivity.startRefundApplyActivity(this, orderDetailEntity.getOrderEntities().get(0), totalPrice, totalIntegral, orderCode);
                        break;
                    case "3"://完成(确认收货)
                        getConfirm();
                        break;
                    case "4"://查看物流
                        String orderCode2 = orderDetailEntity.getOrderCode();
                        LogisticDetailActivity.startLogisticActivity(this, orderCode2);
                        break;
                    case "5"://删除
                        deleteOrder();
                        break;
                    case "6"://取消退款
                        cancelRefund();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_ACT_ONE://取消订单
                    String reason = data.getStringExtra(EXTRA_ENTITY);
                    cancelOrder(reason);
                    break;
            }
        }
    }

    /*取消退款*/
    private void cancelRefund() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("refund_id", orderDetailEntity.getRefundId());
        requestHttpData(Constants.Urls.URL_POST_REFUND_CANCEL, REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
    }

    /*
   * 确认收货*/
    private void getConfirm() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("order_id", orderDetailEntity.getOrderId());
        requestHttpData(Constants.Urls.URL_POST_GET_CONFIRM, REQUEST_NET_FIVE, FProtocol.HttpMethod.POST, params);
    }

    /*取消订单（待支付）*/
    private void cancelOrder(String reason) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("order_id", orderDetailEntity.getOrderCode());
        params.put("reason", reason);
        requestHttpData(Constants.Urls.URL_POST_ORDER_CANCEL, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    private void deleteOrder() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("order_id", orderDetailEntity.getOrderId());
        requestHttpData(Constants.Urls.URL_POST_ORDER_DELETE, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }
}
