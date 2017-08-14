package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.ActivationEntity;
import cn.antke.ezy.network.entities.ActivationPageEntity;
import cn.antke.ezy.person.adapter.ActivationAdapter;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.PAGENUM;
import static cn.antke.ezy.common.CommonConstant.PAGESIZE;
import static cn.antke.ezy.common.CommonConstant.PAGE_SIZE_10;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/18.
 * 赠送审核 列表
 */

public class ConsumerActivationActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.activation_list)
    private FootLoadingListView activationList;
    @ViewInject(R.id.activation_checkall)
    private TextView checkAll;
    @ViewInject(R.id.activation_activate)
    private TextView activate;

    private ActivationAdapter adapter;
    private List<ActivationEntity> entities;
    private ActivationPageEntity pageEntity;
    private List<ActivationEntity> selectEntities;
    private boolean isCheckAll;

    public static void startConsumerActivationActivity(Context context) {
        Intent intent = new Intent(context, ConsumerActivationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_consumer_service_activation);
        setLeftTitle(getString(R.string.consumer_give_eximine));
        ViewInjectUtils.inject(this);

        loadData(false);
        activationList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        activate.setOnClickListener(this);
        checkAll.setOnClickListener(this);
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
        requestHttpData(Constants.Urls.URL_POST_ACTIVATION_LIST, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        activationList.setOnRefreshComplete();
        pageEntity = Parsers.getActivationList(data);
        if (pageEntity != null) {
            entities = pageEntity.getEntities();
            switch (requestCode) {
                case REQUEST_NET_ONE:
                    adapter = new ActivationAdapter(this, entities, this);
                    activationList.setAdapter(adapter);
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        activationList.setCanAddMore(true);
                    } else {
                        activationList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_TWO://加载更多
                    adapter.addDatas(entities);
                    adapter.notifyDataSetChanged();
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        activationList.setCanAddMore(true);
                    } else {
                        activationList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_THREE://确认赠送
                    finish();
                    break;
            }
        }

    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        activationList.setOnRefreshComplete();
        closeProgressDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activation_activate:

                //生成选中集合
                selectEntities = new ArrayList<>();
                for (ActivationEntity entity : entities) {
                    if (entity.isSelected()) {
                        selectEntities.add(entity);
                    }
                }

                //拼接所选中的选项的ids
                StringBuilder integraIds = new StringBuilder();
                for (ActivationEntity entity : selectEntities) {
                    integraIds.append(entity.getIntegralId());
                    integraIds.append(",");
                }

                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("integral_id", integraIds.toString());
                requestHttpData(Constants.Urls.URL_POST_ACTIVATE, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
                break;
            case R.id.activation_checkall:
                isCheckAll = !isCheckAll;
                setCheckAllState(isCheckAll);
                for (ActivationEntity entity : entities) {
                    entity.setSelected(isCheckAll);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.item_activation_time:
                ActivationEntity entity = (ActivationEntity) view.getTag();
                entity.setSelected(!entity.isSelected());
                adapter.notifyDataSetChanged();
                setCheckAllState(isAllCheck());
                break;
        }
    }

    private boolean isAllCheck() {
        for (ActivationEntity activationEntity : entities) {
            if (!activationEntity.isSelected())
                return false;
        }
        return true;
    }

    private void setCheckAllState(boolean isCheck) {
        if (isCheck) {
            checkAll.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.activation_selected, 0, 0, 0);
        } else {
            checkAll.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.activation_normal, 0, 0, 0);
        }
    }
}
