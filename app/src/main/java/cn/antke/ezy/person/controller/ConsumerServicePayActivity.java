package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.AreaEntity;
import cn.antke.ezy.network.entities.ConsumerQueryEntity;
import cn.antke.ezy.network.entities.ListDialogEntity;
import cn.antke.ezy.network.entities.ListDialogPageEntity;
import cn.antke.ezy.network.entities.PayEntity;
import cn.antke.ezy.pay.util.PayUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/17.
 * 消费服务中心开通(支付)
 */
public class ConsumerServicePayActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.consumer_service_choose_area)
    private LinearLayout chooseAreaLl;
    @ViewInject(R.id.consumer_service_area)
    private TextView areaTv;
    @ViewInject(R.id.consumer_service_user_code)
    private TextView userCode;
    @ViewInject(R.id.consumer_service_realname)
    private TextView realNameTv;
    @ViewInject(R.id.consumer_service_phone)
    private TextView phoneTv;
    @ViewInject(R.id.consumer_service_money)
    private TextView moneyTv;
    @ViewInject(R.id.consumer_service_integral)
    private TextView integralTv;
    @ViewInject(R.id.consumer_service_member)
    private TextView memberTv;
    @ViewInject(R.id.consumer_service_wx)
    private TextView payWx;
    @ViewInject(R.id.consumer_service_zfb)
    private TextView payZfb;
    @ViewInject(R.id.consumer_service_pay)
    private TextView pay;

    private String payType = "2";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Map<String, String> result = (Map<String, String>) msg.obj;
            String resultStatus = "";
            for (String key : result.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = result.get(key);
                }
            }
            /*
			 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
			 */
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
//                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                startActivity(new Intent(ConsumerServicePayActivity.this, ConsumerServiceActivity.class));
                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                ToastUtil.shortShow(ConsumerServicePayActivity.this, getString(R.string.pay_failed));
            }
        }
    };


    public static void startConsumerServicePayActivity(Context context){
        Intent intent = new Intent(context,ConsumerServicePayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_consumer_service_open);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.person_consumer_service));
        initView();
        loadData();
    }

    private void initView() {
        userCode.setText(UserCenter.getUserCode(this));
        realNameTv.setText(UserCenter.getUserName(this));
        phoneTv.setText(UserCenter.getPhone(this));

        chooseAreaLl.setOnClickListener(this);
        payWx.setOnClickListener(this);
        payZfb.setOnClickListener(this);
        pay.setOnClickListener(this);
    }

    private void loadData() {
        showProgressDialog();
        requestHttpData(Constants.Urls.URL_POST_CONSUMER_CONDITION, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_ONE://查询消费服务中心
                ConsumerQueryEntity entity = Parsers.getConsumerInfo(data);
                if (entity != null) {
                    moneyTv.setText(getString(R.string.product_sell_price2, entity.getRechargeAmount()));
                    integralTv.setText(getString(R.string.consumer_aside_integral, entity.getFreezeIntegral(), entity.getIntegralStd()));
                    memberTv.setText(getString(R.string.consumer_transductive_member_value, entity.getMember(), entity.getMemberStandard(), entity.getMemIntegralStd()));
                }
                break;
            case REQUEST_NET_TWO://选择大区
                List<AreaEntity> areaEntities = Parsers.getAreaList(data);//新的数据源
                if (areaEntities != null && areaEntities.size() > 0) {
                    List<ListDialogEntity> listEntities = new ArrayList<>();
                    for (AreaEntity areaEntity : areaEntities) {
                        ListDialogEntity listDialogEntity = new ListDialogEntity(areaEntity.getName(), false);
                        listEntities.add(listDialogEntity);
                    }
                    ListDialogPageEntity pageEntity = new ListDialogPageEntity(listEntities);
                    startActivityForResult(new Intent(this, ListDialogActivity.class).putExtra(EXTRA_ENTITY, pageEntity), REQUEST_ACT_ONE);
                }
                break;
            case REQUEST_NET_THREE:
                PayEntity payEntity = Parsers.getPay(data);
                if (payEntity != null) {
                    String prePayId = payEntity.getPayId();
                    if ("2".equals(payType)) {//微信支付
                        PayUtils.startWXPay(this, prePayId);
                    } else {//支付宝支付
                        PayUtils.startAliPay(this, handler, prePayId);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.consumer_service_choose_area:
                showProgressDialog();
                requestHttpData(Constants.Urls.URL_POST_AREA, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, null);
                break;
            case R.id.consumer_service_wx:
                payWx.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_selected, 0);
                payZfb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_normal, 0);
                payType = "2";
                break;
            case R.id.consumer_service_zfb:
                payZfb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_selected, 0);
                payWx.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_normal, 0);
                payType = "1";
                break;
            case R.id.consumer_service_pay:
//                startActivity(new Intent(ConsumerServicePayActivity.this, ConsumerServiceActivity.class));
                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("pay_type", payType);
                requestHttpData(Constants.Urls.URL_POST_OPEN_CONSUMER, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_ACT_ONE == requestCode) {
            String area = data.getStringExtra(EXTRA_ENTITY);
            areaTv.setText(area);
        }
    }
}
