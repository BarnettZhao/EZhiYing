package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.controller.SetPassWordActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.utils.DownTimeUtil;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/19.
 * 重置支付密码
 */
public class ResetPwdActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.reset_pwd_phone)
    private TextView resetPhone;
    @ViewInject(R.id.reset_pwd_identify_code)
    private EditText resetIdentifyCode;
    @ViewInject(R.id.reset_get_verify_code)
    private TextView getIdentifyCode;
    @ViewInject(R.id.reset_next)
    private TextView resetNext;

    private String phone;
    private DownTimeUtil downTimeUtil;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_integral_pwd_reset);
        ViewInjectUtils.inject(this);
        initView();
    }

    private void initView() {
        setLeftTitle(getString(R.string.integral_trading_pwd_set));
        phone = UserCenter.getPhone(this);
        resetPhone.setText(phone);
        getIdentifyCode.setOnClickListener(this);
        resetNext.setOnClickListener(this);
        downTimeUtil = new DownTimeUtil(this);
        downTimeUtil.initCountDownTime(getIdentifyCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_get_verify_code:
                if (!StringUtil.isEmpty(phone) && phone.startsWith("1") && phone.length() == 11) {
                    getVerifyCode();
                    downTimeUtil.countDownTimer.start();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.register_input_phone));
                }
                break;
            case R.id.reset_next:
                code = resetIdentifyCode.getText().toString();
                if (TextUtils.isEmpty(phone) || phone.length() != 11 || !phone.startsWith("1")) {
                    ToastUtil.shortShow(this, getString(R.string.register_input_phone));
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.shortShow(this, getString(R.string.register_input_verify_code));
                    return;
                }

                verifyCode();
                break;
        }
    }

    private void verifyCode() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("phone", phone);
        params.put("verifiationCode", code);
        requestHttpData(Constants.Urls.URL_POST_VERIFY_SMSCODE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    private void getVerifyCode() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("loginName", phone);
        requestHttpData(Constants.Urls.URL_POST_SMS_CODE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        Entity entity = Parsers.getResult(data);
        if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
            closeProgressDialog();
        } else {
            closeProgressDialog();
            ToastUtil.shortShow(this, entity.getResultMsg());
            getIdentifyCode.setEnabled(true);
            getIdentifyCode.setText(getString(R.string.register_get_verify_code));
            downTimeUtil.countDownTimer.cancel();
        }
        super.success(requestCode, data);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_TWO://验证验证码
                SetPassWordActivity.startSetPassWordActivity(this, REQUEST_ACT_ONE, phone, code);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACT_ONE:
                if (RESULT_OK == resultCode) {
                    finish();
                }
                break;
        }
    }
}
