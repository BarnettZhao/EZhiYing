package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.ConsumerServiceEntity;
import cn.antke.ezy.network.entities.ConsumerServicePageEntity;
import cn.antke.ezy.person.adapter.ConsumerServiceAdapter;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.PAGENUM;
import static cn.antke.ezy.common.CommonConstant.PAGESIZE;
import static cn.antke.ezy.common.CommonConstant.PAGE_SIZE_10;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;
import static cn.antke.ezy.common.CommonConstant.TYPE_1;

/**
 * Created by zhaoweiwei on 2017/5/17.
 * 消费服务中心
 */

public class ConsumerServiceActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.consumer_service_account_integral)
    private TextView accountIntegral;
    @ViewInject(R.id.consumer_service_charge_integral)
    private TextView integralCharge;
    @ViewInject(R.id.consumer_service_time_start)
    private TextView startTimeTv;
    @ViewInject(R.id.consumer_service_time_end)
    private TextView endTime;
    @ViewInject(R.id.consumer_service_list)
    private FootLoadingListView consumerList;
    @ViewInject(R.id.consumer_service_activate)
    private TextView activate;
    @ViewInject(R.id.consumer_service_give_examine)
    private TextView giveExamine;

    private ConsumerServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_consumer_service);
        ViewInjectUtils.inject(this);
        initView();
        loadData(false);

        consumerList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });
    }

    private void initView() {
        setLeftTitle(getString(R.string.person_consumer_service));
        setRightText(getString(R.string.renew));
        hideTitleLine();
        rightText.setOnClickListener(this);
        integralCharge.setOnClickListener(this);
        activate.setOnClickListener(this);
        giveExamine.setOnClickListener(this);
    }


    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        if (isMore) {
            page = adapter.getPage() + 1;
            request = REQUEST_NET_TWO;
        }
        params.put(PAGENUM, String.valueOf(page));
        params.put(PAGESIZE, PAGE_SIZE_10);
        requestHttpData(Constants.Urls.URL_POST_CONSUMER_QUERY, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        consumerList.setOnRefreshComplete();
        ConsumerServicePageEntity pageEntity = Parsers.getConsumerPage(data);
        if (pageEntity != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(R.string.product_sell_integral, pageEntity.getIntegralNum()));
            spannableStringBuilder.setSpan(new AbsoluteSizeSpan(CommonTools.dp2px(this, 18)), pageEntity.getIntegralNum().length(), pageEntity.getIntegralNum().length() + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            accountIntegral.setText(spannableStringBuilder);

            if (!StringUtil.isEmpty(pageEntity.getStartTime())) {
                startTimeTv.setVisibility(View.VISIBLE);
                startTimeTv.setText(getString(R.string.consumer_start_data, pageEntity.getStartTime()));
            } else {
                startTimeTv.setVisibility(View.GONE);
            }
            if (!StringUtil.isEmpty(pageEntity.getEndTime())) {
                endTime.setVisibility(View.VISIBLE);
            } else {
                endTime.setVisibility(View.GONE);
                endTime.setText(getString(R.string.consumer_start_data, pageEntity.getEndTime()));
            }

            List<ConsumerServiceEntity> entities = pageEntity.getEntities();
            switch (requestCode) {
                case REQUEST_NET_ONE:
                    adapter = new ConsumerServiceAdapter(this, entities);
                    consumerList.setAdapter(adapter);
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        consumerList.setCanAddMore(true);
                    } else {
                        consumerList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_TWO://加载更多
                    adapter.addDatas(entities);
                    adapter.notifyDataSetChanged();
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        consumerList.setCanAddMore(true);
                    } else {
                        consumerList.setCanAddMore(false);
                    }
                    break;
            }
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        consumerList.setOnRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rigth_text:
                ConsumerServicePayActivity.startConsumerServicePayActivity(this);
                break;
            case R.id.consumer_service_charge_integral:
                startActivity(new Intent(this, RedIntegralChargeActivity.class));
                break;
            case R.id.consumer_service_activate://激活
                IntegralRecastActivity.startIntegralRecastActivity(this, TYPE_1);
                break;
            case R.id.consumer_service_give_examine:
                ConsumerActivationActivity.startConsumerActivationActivity(this);
                break;
        }
    }
}
