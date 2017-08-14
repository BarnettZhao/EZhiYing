package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.base.WebViewActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.deal.controller.DealRechargeActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.MyMessageEntity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.person.adapter.MyMessageAdapter;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TITLE;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.EXTRA_URL;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_DELIVING;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_REFUND;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;
import static cn.antke.ezy.common.CommonConstant.TYPE_1;

/**
 * Created by zhaoweiwei on 2017/5/16.
 * 我的消息
 */

public class MyMessageActivity extends ToolBarActivity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.my_message_list)
    private FootLoadingListView messageList;

    private MyMessageAdapter adapter;
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_message);
        ViewInjectUtils.inject(this);
        from = getIntent().getIntExtra(EXTRA_FROM, 0);
        if (FROM_ACT_ONE == from) {//站内公告
            setLeftTitle(getString(R.string.person_notice));
        } else {
            setLeftTitle(getString(R.string.person_my_message));
        }

        loadData(false);

        messageList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        messageList.setOnItemClickListener(this);
    }

    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int request = REQUEST_NET_ONE;
        int page = 1;
        if (isMore) {
            request = REQUEST_NET_TWO;
            page = adapter.getPage() + 1;
        }
        params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
        params.put(CommonConstant.PAGENUM, String.valueOf(page));
        String url;
        if (FROM_ACT_ONE == from) {
            url = Constants.Urls.URL_POST_NOTICE;
        } else {
            url = Constants.Urls.URL_POST_MESSAGE_LIST;
        }
        requestHttpData(url, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        messageList.setOnRefreshComplete();
        PagesEntity<MyMessageEntity> pagesEntity = Parsers.getMessages(data);
        if (pagesEntity != null) {
            List<MyMessageEntity> entities = pagesEntity.getDatas();
            switch (requestCode) {
                case REQUEST_NET_ONE:
                    if (entities != null && entities.size() > 0) {
                        adapter = new MyMessageAdapter(this, entities);
                        messageList.setAdapter(adapter);
                    }
                    if (pagesEntity.getTotalPage() > adapter.getPage()) {
                        messageList.setCanAddMore(true);
                    } else {
                        messageList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_TWO:
                    if (entities != null && entities.size() > 0) {
                        adapter.addDatas(entities);
                    }
                    if (pagesEntity.getTotalPage() > adapter.getPage()) {
                        messageList.setCanAddMore(true);
                    } else {
                        messageList.setCanAddMore(false);
                    }
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyMessageEntity entity = adapter.getItem(i);
        if (FROM_ACT_ONE == from) {
            startActivity(new Intent(this, WebViewActivity.class).putExtra(EXTRA_URL, entity.getMessUrl()).putExtra(EXTRA_TITLE, entity.getMessTitle()));
        } else {
            String type = entity.getMessType();
            if (!StringUtil.isEmpty(type)) {
                switch (type) {
                    case "1:"://系统消息
                        break;
                    case "2:"://交易大厅续费
                        DealRechargeActivity.startDealRechargeActivity(this);
                        break;
                    case "3:"://消费服务中心续费
                        ConsumerServicePayActivity.startConsumerServicePayActivity(this);
                        break;
                    case "4:"://赠送审批消息
                        ConsumerActivationActivity.startConsumerActivationActivity(this);
                        break;
                    case "5:"://赠送审批结果消息
                        IntegralGiveActivity.startIntegralGiveActivity(this);
                        break;
                    case "6:"://积分互转
                        IntegralOrderActivity.startIntegralOrderActivity(this, TYPE_1);
                        break;
                    case "7:"://商品审核
                        break;
                    case "8"://支付成功
                        startActivity(new Intent(this, OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_DELIVING));
                        break;
                    case "9:"://退款消息
                        startActivity(new Intent(this, OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_REFUND));
                        break;
                }
            }
        }
    }
}
