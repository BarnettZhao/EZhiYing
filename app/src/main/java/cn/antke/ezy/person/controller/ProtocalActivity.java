package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.main.controller.MainActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.utils.ExitManager;
import cn.antke.ezy.utils.ViewInjectUtils;
import cn.antke.ezy.widget.ScrollWebView;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_FIVE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_FOUR;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_THREE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_TWO;

/**
 * Created by zhaoweiwei on 2017/5/11.
 * 协议
 */

public class ProtocalActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.store_protocal_webview)
    private ScrollWebView webView;
    @ViewInject(R.id.store_protocal_agree)
    private TextView protocalAgree;
    private int from;
    private int type;

    public static void startProtocalActivity(Context context, int from) {
        Intent intent = new Intent(context, ProtocalActivity.class);
        intent.putExtra(EXTRA_FROM, from);
        context.startActivity(intent);
    }

    public static void startProtocalActivity(Context context, int from, int type) {
        Intent intent = new Intent(context, ProtocalActivity.class);
        intent.putExtra(EXTRA_FROM, from);
        intent.putExtra(CommonConstant.EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_protocal);
        ExitManager.instance.addApplyStoreActivity(this);
        ViewInjectUtils.inject(this);

        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra(EXTRA_TYPE, CommonConstant.TYPE_PERSONAL);
        //FROM_ACT_ONE 开店协议  FROM_ACT_TWO 消费服务中心协议
        from = getIntent().getIntExtra(EXTRA_FROM, FROM_ACT_ONE);

        String url = null;
//1:企业店铺协议2：商家店铺协议3：个体店铺协议4:实体店铺协议 5:消费服务中心 协议  6：用户注册   8：交易大厅协议 9:登录协议   10 用户中心协议
        if (FROM_ACT_ONE == from) {//开店协议
            setLeftTitle(getString(R.string.store_protocal));
            String typeValue;
            switch (type) {
                case CommonConstant.TYPE_PERSONAL:
                    typeValue = "3";
                    break;
                case CommonConstant.TYPE_BUSINESS:
                    typeValue = "2";
                    break;
                case CommonConstant.TYPE_ENTERPRISE:
                    typeValue = "1";
                    break;
                case CommonConstant.TYPE_PHYSICAL:
                    typeValue = "4";
                    break;
                default:
                    typeValue = "3";
            }
            url = Constants.Urls.URL_BASE_DOMAIN + "/interface/storeAgreement.do?type=" + typeValue;
        } else if (FROM_ACT_TWO == from) {//消费服务中心
            setLeftTitle(getString(R.string.person_consumer_service));
            url = Constants.Urls.URL_BASE_DOMAIN + "/interface/storeAgreement.do?type=" + 5;
        } else if (FROM_ACT_THREE == from) {//注册协议
            setLeftTitle(getString(R.string.protocal_name));
            url = Constants.Urls.URL_BASE_DOMAIN + "/interface/storeAgreement.do?type=" + 6;
            protocalAgree.setText(getString(R.string.protocal_agree));
        } else if (FROM_ACT_FOUR == from) {//登录协议
            setLeftTitle(getString(R.string.protocal_name));
            url = Constants.Urls.URL_BASE_DOMAIN + "/interface/storeAgreement.do?type=" + 9;
            protocalAgree.setText(getString(R.string.protocal_agree));
        } else if (FROM_ACT_FIVE == from) {//用户注册协议（个人中心）
            setLeftTitle(getString(R.string.protocal_name));
            url = Constants.Urls.URL_BASE_DOMAIN + "/interface/storeAgreement.do?type=" + 10 + "&user_id=" + UserCenter.getUserId(this);
            protocalAgree.setText(getString(R.string.protocal_agree));
        }
        webView.loadUrl(url);
        webView.setOnWebScrollListener(() -> {
            if (webView.getContentHeight() * webView.getScale() == (webView.getHeight() + webView.getScrollY())) {
                protocalAgree.setEnabled(true);
            }
        });
        protocalAgree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_protocal_agree:
                if (FROM_ACT_ONE == from) {
                    Intent intent = new Intent(this, StoreApplyInfoActivity.class);
                    intent.putExtra(CommonConstant.EXTRA_TYPE, type);
                    startActivity(intent);
                } else if (FROM_ACT_TWO == from) {
                    ConsumerServicePayActivity.startConsumerServicePayActivity(this);
                } else if (FROM_ACT_FIVE == from) {
                    UserCenter.setPersonFirst(this, false);
                    startActivity(new Intent(this, MainActivity.class).putExtra(MainActivity.EXTRA_WHICH_TAB, 4));
                }
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
