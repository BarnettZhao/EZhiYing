package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.utils.DownTimeUtil;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.VerifyUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zww on 2017/7/20.
 * 修改登录密码
 */

public class LoginPwdChangeActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.login_change_pwd_phone)
    private TextView pwdPhone;
    @ViewInject(R.id.login_change_get_verify_code)
    private TextView getVerifyTv;
    @ViewInject(R.id.login_change_verify_code)
    private EditText verifyEt;
    @ViewInject(R.id.login_change_pwd_old)
    private EditText pwdOld;
    @ViewInject(R.id.login_change_pwd_new)
    private EditText pwdNew;
    @ViewInject(R.id.login_change_pwd_new_confirm)
    private EditText pwdNewConfirm;
    @ViewInject(R.id.login_change_pwd_finish)
    private TextView pwdFinshed;

    private DownTimeUtil downTimeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_change_pwd);
        ViewInjectUtils.inject(this);
        initView();
    }

    private void initView() {
        setLeftTitle(getString(R.string.login_change_pwd));
        pwdPhone.setText(UserCenter.getPhone(this));
        InputUtil.editIsEmpty(pwdFinshed, verifyEt, pwdOld, pwdNew, pwdNewConfirm);
        pwdFinshed.setOnClickListener(this);
        getVerifyTv.setOnClickListener(this);
        downTimeUtil = new DownTimeUtil(this);
        downTimeUtil.initCountDownTime(getVerifyTv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_change_pwd_finish:
                String verifyCode = verifyEt.getText().toString();
                String oldPwd = pwdOld.getText().toString();
                String newPwd = pwdNew.getText().toString();
                String newPwdConfirm = pwdNewConfirm.getText().toString();
                if (VerifyUtils.isPassword(this, oldPwd) || VerifyUtils.isPassword(this, newPwd) || VerifyUtils.isPassword(this, newPwdConfirm)) {
                    if (newPwd.equals(newPwdConfirm)) {
                        IdentityHashMap<String, String> params = new IdentityHashMap<>();
                        params.put("login_name", UserCenter.getPhone(this));
                        params.put("verifiationCode", verifyCode);
                        params.put("old_password", oldPwd);
                        params.put("new_password", newPwd);
                        params.put("user_code", UserCenter.getUserCode(this));
                        requestHttpData(Constants.Urls.URL_POST_CHANGE_LOGIN_PWD, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                    } else {
                        ToastUtil.shortShow(this, getString(R.string.login_change_pwd_different));
                    }
                }
                break;
            case R.id.login_change_get_verify_code:
                getVerifyCode();
                downTimeUtil.countDownTimer.start();
                break;
        }
    }

    private void getVerifyCode() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("loginName", UserCenter.getPhone(this));
        requestHttpData(Constants.Urls.URL_POST_SMS_CODE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                Entity entity = Parsers.getResult(data);
                if (entity != null) {
                    ToastUtil.shortShow(this, entity.getResultMsg());
                }
                finish();
                break;
            case REQUEST_NET_TWO:
                Entity entity2 = Parsers.getResult(data);
                if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity2.getResultCode())) {
                    closeProgressDialog();
                } else {
                    closeProgressDialog();
                    ToastUtil.shortShow(this, entity2.getResultMsg());
                    getVerifyTv.setEnabled(true);
                    getVerifyTv.setText(getString(R.string.register_get_verify_code));
                    downTimeUtil.countDownTimer.cancel();
                }
                break;
        }
    }
}
