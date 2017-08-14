package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.LotteryInquiryEntity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.person.adapter.LotteryInquiryAdapter;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/21.
 * 抽奖查询
 */

public class LotteryInquiryActivity extends ToolBarActivity {

    private FootLoadingListView lotteryListView;
    private LotteryInquiryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lottery_inquiry);
        lotteryListView = (FootLoadingListView) findViewById(R.id.lottery_inquiry_list);
        setLeftTitle(getString(R.string.person_lottery_inquiry));

        lotteryListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        loadData(false);
    }

    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        if (isMore) {
            request = REQUEST_NET_TWO;
            page = adapter.getPage() + 1;
        }
        params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_20);
        params.put(CommonConstant.PAGENUM, String.valueOf(page));
        requestHttpData(Constants.Urls.URL_POST_PRIZE_QUERY, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        lotteryListView.setOnRefreshComplete();
        PagesEntity<LotteryInquiryEntity> pagesEntity = Parsers.getLottery(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (pagesEntity!=null && pagesEntity.getDatas().size()>0) {
                    adapter = new LotteryInquiryAdapter(this, pagesEntity.getDatas());
                    lotteryListView.setAdapter(adapter);
                    if (pagesEntity.getTotalPage()>adapter.getPage()) {
                        lotteryListView.setCanAddMore(true);
                    } else {
                        lotteryListView.setCanAddMore(false);
                    }
                }
                break;
            case REQUEST_NET_TWO://加载更多
                if (pagesEntity!=null && pagesEntity.getDatas().size()>0) {
                    adapter.addDatas(pagesEntity.getDatas());
                    adapter.notifyDataSetChanged();
                    if (pagesEntity.getTotalPage()>adapter.getPage()) {
                        lotteryListView.setCanAddMore(true);
                    } else {
                        lotteryListView.setCanAddMore(false);
                    }
                }
                break;
        }
    }
}
