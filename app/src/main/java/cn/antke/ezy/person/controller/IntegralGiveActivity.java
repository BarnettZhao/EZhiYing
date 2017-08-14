package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.IntegralGiveEntity;
import cn.antke.ezy.network.entities.IntegralGivePageEntity;
import cn.antke.ezy.person.adapter.IntegralGiveAdapter;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.PAGENUM;
import static cn.antke.ezy.common.CommonConstant.PAGESIZE;
import static cn.antke.ezy.common.CommonConstant.PAGE_SIZE_10;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zww on 2017/6/19.
 * 积分赠送列表
 */

public class IntegralGiveActivity extends ToolBarActivity implements View.OnClickListener{

    private IntegralGiveAdapter adapter;
    private FootLoadingListView giveList;

    public static void startIntegralGiveActivity(Context context) {
        Intent intent = new Intent(context,IntegralGiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_integral_give);
        setLeftTitle(getString(R.string.integral_give));
        giveList = (FootLoadingListView) findViewById(R.id.integral_give_list);

        giveList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        if (isMore) {
            page = adapter.getPage() + 1;
            request = REQUEST_NET_TWO;
        }
        params.put(PAGENUM, String.valueOf(page));
        params.put(PAGESIZE, PAGE_SIZE_10);
        requestHttpData(Constants.Urls.URL_POST_GIVE_LIST, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        giveList.setOnRefreshComplete();
        IntegralGivePageEntity pageEntity = Parsers.getGiveList(data);
        if (pageEntity != null) {
            List<IntegralGiveEntity> entities = pageEntity.getEntities();
            switch (requestCode) {
                case REQUEST_NET_ONE:
                    adapter = new IntegralGiveAdapter(this, entities,this);
                    giveList.setAdapter(adapter);
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        giveList.setCanAddMore(true);
                    } else {
                        giveList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_TWO://加载更多
                    adapter.addDatas(entities);
                    adapter.notifyDataSetChanged();
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        giveList.setCanAddMore(true);
                    } else {
                        giveList.setCanAddMore(false);
                    }
                    break;
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_integral_give_btn:
                IntegralGiveEntity entity = (IntegralGiveEntity) view.getTag();
                startActivity(new Intent(this,IntegralGiveOperateActivity.class).putExtra(EXTRA_ENTITY,entity));
                break;
        }
    }
}
