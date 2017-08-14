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
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.main.controller.MainActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.UserEntity;
import cn.antke.ezy.person.controller.ProtocalActivity;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.VerifyUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_FOUR;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/3.
 * 登录
 */

public class LoginActivity extends ToolBarActivity implements View.OnClickListener, View.OnFocusChangeListener {

    @ViewInject(R.id.login_username)
    private EditText loginUsername;
    @ViewInject(R.id.login_password)
    private EditText loginPassword;
    @ViewInject(R.id.login_forget)
    private TextView loginForget;
    @ViewInject(R.id.login_service_protocal)
    private TextView loginProtocal;
    @ViewInject(R.id.login_confirm)
    private TextView loginConfirm;
    private boolean isCanLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ViewInjectUtils.inject(this);
        initView();
    }

    private void initView() {
        setLeftTitle(getString(R.string.login_text));
        setRightText(getString(R.string.login_register));
//		mBtnTitleLeft.setVisibility(View.INVISIBLE);
        rightText.setOnClickListener(this);
        InputUtil.editIsEmpty(loginConfirm, loginUsername, loginPassword);
        loginForget.setOnClickListener(this);
        loginProtocal.setOnClickListener(this);
        loginConfirm.setOnClickListener(this);
        loginUsername.setOnFocusChangeListener(this);
        loginPassword.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rigth_text:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login_forget:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.login_service_protocal:
                Intent intent = new Intent(this, ProtocalActivity.class);
                intent.putExtra(EXTRA_FROM, FROM_ACT_FOUR);
                startActivityForResult(intent, REQUEST_ACT_ONE);
                break;
            case R.id.login_confirm:
                String userName = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();
                if (isCanLogin) {
                    if (VerifyUtils.isPassword(this, password)) {
                        showProgressDialog();
                        IdentityHashMap<String, String> params = new IdentityHashMap<>();
                        params.put("loginName", userName);
                        params.put("password", password);
                        requestHttpData(Constants.Urls.URL_POST_USER_LOGIN, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                    }
                } else {
                    ToastUtil.shortShow(this, getString(R.string.protocal_agree_first));
                }
                break;
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        //status 4是认证成功
        switch (requestCode) {
            case REQUEST_NET_ONE:
                UserEntity userEntity = Parsers.getUserInfo(data);
                UserCenter.savaUserInfo(this, userEntity);
                startActivity(new Intent(this, MainActivity.class));
                if (StringUtil.isEmpty(userEntity.getCertCode())) {
                    BindPersonalInfoAcitvity.startBindPersonalInfoAcitvity(this);
                }
                break;
            case REQUEST_NET_TWO:
                String status = Parsers.isCheckedProtocal(data);//1：没看过协议 2：看过协议
                if (!StringUtil.isEmpty(status)) {
                    if ("1".equals(status)) {
                        loginProtocal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_service_protocal_normal, 0, 0, 0);
                        isCanLogin = false;
                    } else if ("2".equals(status)) {
                        loginProtocal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_service_protocal_checked, 0, 0, 0);
                        isCanLogin = true;
                    }
                }
                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        closeProgressDialog();
        ToastUtil.shortShow(this, errorMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_ACT_ONE == requestCode) {
            loginProtocal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_service_protocal_checked, 0, 0, 0);
            isCanLogin = true;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.login_username:
                if (hasFocus) {
                    loginProtocal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_service_protocal_normal, 0, 0, 0);
                    isCanLogin = false;
                    loginPassword.setText("");
                }
                break;
            case R.id.login_password:
                String phone = loginUsername.getText().toString();
                if (hasFocus && !StringUtil.isEmpty(phone)) {
                    IdentityHashMap<String, String> params = new IdentityHashMap<>();
                    params.put("login_name", phone);
                    requestHttpData(Constants.Urls.URL_POST_LOGIN_PROTOCAL, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
                }
                break;
        }
    }
}
