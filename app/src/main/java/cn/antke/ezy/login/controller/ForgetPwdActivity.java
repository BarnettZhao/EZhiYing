package cn.antke.ezy.login.controller;

import android.content.Intent;
import android.os.Bundle;
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
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.main.controller.MainActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.UserEntity;
import cn.antke.ezy.utils.DownTimeUtil;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.VerifyUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/3.
 * 忘记密码
 */

public class ForgetPwdActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.forget_phone)
    private EditText forgetPhone;
    @ViewInject(R.id.forget_password)
    private EditText newPassword;
    @ViewInject(R.id.forget_verify_code)
    private EditText verifyCodeEt;
    @ViewInject(R.id.forget_get_verify_code)
    private TextView getVerifyCode;
    @ViewInject(R.id.forget_confirm)
    private TextView confirm;

    private String phone;
    private String password;
    private String verifyCode;
    private DownTimeUtil downTimeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_froget_pwd);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.forget_password_title));
        getVerifyCode.setOnClickListener(this);
        confirm.setOnClickListener(this);
        InputUtil.editIsEmpty(confirm, forgetPhone, newPassword, verifyCodeEt);
        downTimeUtil = new DownTimeUtil(this);
        downTimeUtil.initCountDownTime(getVerifyCode);
    }

    @Override
    public void onClick(View v) {
        phone = forgetPhone.getText().toString();
        password = newPassword.getText().toString();
        verifyCode = verifyCodeEt.getText().toString();
        switch (v.getId()) {
            case R.id.forget_get_verify_code:
                if (!StringUtil.isEmpty(phone) && phone.startsWith("1") && phone.length() == 11) {
                    getVerifyCode();
                    downTimeUtil.countDownTimer.start();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.register_input_phone));
                }
                break;
            case R.id.forget_confirm:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        if (VerifyUtils.isPassword(this, password)) {
            showProgressDialog();
            IdentityHashMap<String, String> params = new IdentityHashMap<>();
            params.put("loginName", phone);
            params.put("password", password);
            params.put("verifiationCode", verifyCode);
            params.put("type", "1");
            requestHttpData(Constants.Urls.URL_POST_RESET_PASSWORD, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
        }
    }

    private void getVerifyCode() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("token", "");
        params.put("loginName", phone);
        requestHttpData(Constants.Urls.URL_POST_SMS_CODE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        super.success(requestCode, data);
        Entity entity = Parsers.getResult(data);
        if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
            closeProgressDialog();
        } else {
            closeProgressDialog();
            ToastUtil.shortShow(this, entity.getResultMsg());
            getVerifyCode.setEnabled(true);
            getVerifyCode.setText(getString(R.string.register_get_verify_code));
            downTimeUtil.countDownTimer.cancel();
        }
        super.success(requestCode, data);
    }

    @Override
    public void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        switch (requestCode) {
            case REQUEST_NET_ONE://获取验证码
                break;
            case REQUEST_NET_TWO://重新设置新密码
                UserEntity userEntity = Parsers.getUserInfo(data);
                UserCenter.savaUserInfo(this, userEntity);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
