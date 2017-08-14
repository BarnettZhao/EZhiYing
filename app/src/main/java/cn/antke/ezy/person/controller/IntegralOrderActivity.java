package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.IntegralDetailEntity;
import cn.antke.ezy.network.entities.IntegralItemEntity;
import cn.antke.ezy.person.adapter.IntegralAsideAdapter;
import cn.antke.ezy.person.adapter.IntegralOtherAdapter;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.PAGENUM;
import static cn.antke.ezy.common.CommonConstant.PAGESIZE;
import static cn.antke.ezy.common.CommonConstant.PAGE_SIZE_10;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;
import static cn.antke.ezy.common.CommonConstant.TYPE_0;
import static cn.antke.ezy.common.CommonConstant.TYPE_1;
import static cn.antke.ezy.common.CommonConstant.TYPE_2;
import static cn.antke.ezy.common.CommonConstant.TYPE_3;
import static cn.antke.ezy.common.CommonConstant.TYPE_4;
import static cn.antke.ezy.common.CommonConstant.TYPE_6;

/**
 * Created by zhaoweiwei on 2017/5/26.
 * 积分订单页
 */

public class IntegralOrderActivity extends ToolBarActivity {
    @ViewInject(R.id.integral_name)
    private TextView integralName;
    @ViewInject(R.id.integral_account)
    private TextView integralAccount;
    @ViewInject(R.id.integral_list)
    private FootLoadingListView integralList;

    private String title;
    private int type;
    private IntegralOtherAdapter otherAdapter;
    private IntegralAsideAdapter asideAdapter;

    public static void startIntegralOrderActivity(Context context, int type) {
        Intent intent = new Intent(context, IntegralOrderActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_integral_order);
        ViewInjectUtils.inject(this);
        type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_0);
        initView();
        loadData(false);
    }

    private void initView() {
        hideTitleLine();
        getTitle(type);
        setLeftTitle(title);
        integralName.setText(title);
        integralList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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


    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("integral_type", String.valueOf(type));
        int page = 1;
        int request = REQUEST_NET_ONE;
        if (isMore) {
            if (TYPE_2 == type) {
                page = asideAdapter.getPage() + 1;
            } else {
                page = otherAdapter.getPage() + 1;
            }
            request = REQUEST_NET_TWO;
        }
        params.put(PAGENUM, String.valueOf(page));
        params.put(PAGESIZE, PAGE_SIZE_10);
        requestHttpData(Constants.Urls.URL_POST_QUERY_INTEGRAL, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        integralList.setOnRefreshComplete();
        IntegralDetailEntity integralEntity = Parsers.getIntegral(data);

        String integralNum = getString(R.string.product_sell_integral, integralEntity.getIntegral());
        SpannableStringBuilder ssb = new SpannableStringBuilder(integralNum);
        ssb.setSpan(new AbsoluteSizeSpan(CommonTools.dp2px(this, 18)), integralEntity.getIntegral().length(), integralNum.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        integralAccount.setText(ssb);

        List<IntegralItemEntity> entities = integralEntity.getIntegralListEntities();
        if (integralEntity != null) {
            switch (requestCode) {
                case REQUEST_NET_ONE://积分列表
                    if (entities != null && entities.size() > 0) {
                        if (TYPE_2 == type) {
                            asideAdapter = new IntegralAsideAdapter(this, entities);
                            integralList.setAdapter(asideAdapter);
                            if (integralEntity.getTotalPage() > asideAdapter.getPage()) {
                                integralList.setCanAddMore(true);
                            } else {
                                integralList.setCanAddMore(false);
                            }
                        } else {
                            otherAdapter = new IntegralOtherAdapter(this, entities);
                            integralList.setAdapter(otherAdapter);
                            if (integralEntity.getTotalPage() > otherAdapter.getPage()) {
                                integralList.setCanAddMore(true);
                            } else {
                                integralList.setCanAddMore(false);
                            }
                        }
                    }

                    break;
                case REQUEST_NET_TWO://加载更多
                    if (entities != null && entities.size() > 0) {
                        if (TYPE_2 == type) {
                            asideAdapter.addDatas(entities);
                            asideAdapter.notifyDataSetChanged();
                            if (integralEntity.getTotalPage() > asideAdapter.getPage()) {
                                integralList.setCanAddMore(true);
                            } else {
                                integralList.setCanAddMore(false);
                            }
                        } else {
                            otherAdapter.addDatas(entities);
                            otherAdapter.notifyDataSetChanged();
                            if (integralEntity.getTotalPage() > otherAdapter.getPage()) {
                                integralList.setCanAddMore(true);
                            } else {
                                integralList.setCanAddMore(false);
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        integralList.setOnRefreshComplete();
    }

    //积分类型 1、可用积分；2、待用积分；3、购物积分；4、分享积分；5、红分；6、多功能积分；7、债券
    private void getTitle(int type) {
        switch (type) {
            case TYPE_1:
                title = getString(R.string.person_useable_integral);
                break;
            case TYPE_3:
                title = getString(R.string.integral_shop);
                break;
            case TYPE_2:
                title = getString(R.string.integral_aside);
                break;
            case TYPE_6:
                title = getString(R.string.integral_multi_function);
                break;
            case TYPE_4:
                title = getString(R.string.integral_share);
                break;
        }
    }
}
