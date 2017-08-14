package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseFragment;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.OrderEntity;
import cn.antke.ezy.network.entities.OrderListPageEntity;
import cn.antke.ezy.person.adapter.StoreOrderListAdapter;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ID;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2016/12/21.
 * 订单列表
 */

public class StoreOrderListFragment extends BaseFragment implements View.OnClickListener {

    private int state;
    private String storeId;
    private FootLoadingListView loadingListView;
    private StoreOrderListAdapter adapter;
    private List<OrderEntity> entities;

    public void setArgs(int state, String storeId) {
        switch (state) {
            case 2:
                this.state = 22;
                break;
            case 3:
                this.state = 23;
                break;
            case 4:
                this.state = 24;
                break;
            case 6:
                this.state = 25;
                break;
        }
        this.storeId = storeId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_store_order, null);
        loadingListView = (FootLoadingListView) view.findViewById(R.id.store_order_list);
        loadData(false);
        loadingListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });
        return view;
    }

    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        String url = Constants.Urls.URL_POST_ORDER_LIST;
        if (isMore) {
            request = REQUEST_NET_TWO;
        }

        if (25 == state) {
            url = Constants.Urls.URL_POST_REFUND_LIST;
        } else {
            params.put("order_status", String.valueOf(state));
        }

        params.put("store_id", storeId);
        params.put("store_order_type", "2");
        params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
        params.put(CommonConstant.PAGENUM, String.valueOf(page));
        requestHttpData(url, request, FProtocol.HttpMethod.POST, params);

    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        loadingListView.setOnRefreshComplete();
        OrderListPageEntity pageEntity = Parsers.getOrders(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pageEntity != null) {
                    entities = pageEntity.getOrderEntities();
                    if (entities != null && entities.size() > 0) {
                        adapter = new StoreOrderListAdapter(getActivity(), entities, this);
                        loadingListView.setAdapter(adapter);
                        if (pageEntity.getTotalPager() > adapter.getPage()) {
                            loadingListView.setCanAddMore(true);
                        } else {
                            loadingListView.setCanAddMore(false);
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
                            loadingListView.setCanAddMore(true);
                        } else {
                            loadingListView.setCanAddMore(false);
                        }
                    }
                }
                break;
            case REQUEST_NET_THREE://确认退款
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_store_order_btn:
                OrderEntity entity = (OrderEntity) view.getTag();
                String status = entity.getStatus();
                switch (status) {
                    case "22"://发货
                        startActivityForResult(new Intent(getActivity(), StoreLogisticActivity.class).putExtra(EXTRA_ID, entity.getOrderCode()), REQUEST_ACT_ONE);
                        break;
                    case "25"://确认退款
                        showProgressDialog();
                        IdentityHashMap<String, String> params = new IdentityHashMap<>();
                        params.put("store_id", entity.getStoreId());
                        params.put("refund_id", entity.getRefundId());
                        requestHttpData(Constants.Urls.URL_POST_AGREE_REFUND, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
                        break;
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity().RESULT_OK == resultCode && REQUEST_ACT_ONE == requestCode) {
            loadData(false);
        }
    }
}
