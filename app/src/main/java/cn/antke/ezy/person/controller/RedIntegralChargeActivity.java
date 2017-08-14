package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.Map;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.PayEntity;
import cn.antke.ezy.pay.util.PayUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;

/**
 * Created by zhaoweiwei on 2017/5/18.
 * 红积分充值
 */

public class RedIntegralChargeActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.red_integral_usercode)
    private TextView userCode;
    @ViewInject(R.id.red_integral_money)
    private EditText money;
    @ViewInject(R.id.red_integral_wx)
    private TextView payWx;
    @ViewInject(R.id.red_integral_zfb)
    private TextView payZfb;
    @ViewInject(R.id.red_integral_pay)
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
                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                ToastUtil.shortShow(RedIntegralChargeActivity.this, getString(R.string.pay_failed));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_red_integral_charge);
        ViewInjectUtils.inject(this);
        initView();
    }

    private void initView() {
        setLeftTitle(getString(R.string.consumer_red_integral_charge));
        userCode.setText(UserCenter.getUserCode(this));
        pay.setOnClickListener(this);
        payWx.setOnClickListener(this);
        payZfb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.red_integral_wx:
                payWx.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_selected, 0);
                payZfb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_normal, 0);
                payType = "2";
                break;
            case R.id.red_integral_zfb:
                payZfb.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_selected, 0);
                payWx.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.language_area_normal, 0);
                payType = "1";
                break;
            case R.id.red_integral_pay:
                String amount = money.getText().toString();
                if (!StringUtil.isEmpty(amount)) {
                    int amountInt = Integer.parseInt(amount);
                    if (amountInt > 0) {
                        showProgressDialog();
                        IdentityHashMap<String, String> params = new IdentityHashMap<>();
                        params.put("type", "5");
                        params.put("pay_type", payType);
                        params.put("amount", amount);
                        requestHttpData(Constants.Urls.URL_POST_INTEGRAL_CHARGE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                    } else {
                        ToastUtil.shortShow(this, getString(R.string.consumer_charge_number));
                    }
                }
                break;
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        PayEntity payEntity = Parsers.getPay(data);
        if (payEntity != null) {
            String prePayId = payEntity.getPayId();
            if ("2".equals(payType)) {//微信支付
                PayUtils.startWXPay(this, prePayId);
            } else {//支付宝支付
                PayUtils.startAliPay(this, handler, prePayId);
            }
        }
    }
}
