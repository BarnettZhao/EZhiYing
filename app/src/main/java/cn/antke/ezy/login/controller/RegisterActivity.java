package cn.antke.ezy.login.controller;

import android.content.Intent;
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
import cn.antke.ezy.network.entities.RecommenderEntity;
import cn.antke.ezy.network.entities.UserEntity;
import cn.antke.ezy.person.controller.ProtocalActivity;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.DownTimeUtil;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.VerifyUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_FOUR;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/3.
 * 注册
 */

public class RegisterActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.register_usernumber)
    private TextView userNumber;
    @ViewInject(R.id.register_change_usernumber)
    private TextView changeUserNumber;
    @ViewInject(R.id.register_phone)
    private EditText phoneEt;
    @ViewInject(R.id.register_password)
    private EditText passwordEt;
    @ViewInject(R.id.register_verify_code)
    private EditText verifyCodeEt;
    @ViewInject(R.id.register_get_verify_code)
    private TextView getVerifyCode;
    @ViewInject(R.id.register_recommendnumber)
    private EditText recommednNumberEt;
    @ViewInject(R.id.register_service_protocal)
    private TextView servicePro;
    @ViewInject(R.id.register_confirm)
    private TextView confirm;

    private String phone;
    private String password;
    private String verifyCode;
    private String recommentCode;
    private String userCode;
    private DownTimeUtil downTimeUtil;
    private boolean isCanRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        ViewInjectUtils.inject(this);
        initView();
        getUserCode();
    }

    private void initView() {
        setLeftTitle(getString(R.string.login_register));
        changeUserNumber.setOnClickListener(this);
        getVerifyCode.setOnClickListener(this);
        servicePro.setOnClickListener(this);
        confirm.setOnClickListener(this);
        InputUtil.editIsEmpty(confirm, phoneEt, passwordEt, verifyCodeEt, recommednNumberEt);
        downTimeUtil = new DownTimeUtil(this);
        downTimeUtil.initCountDownTime(getVerifyCode);
    }


    @Override
    public void onClick(View v) {
        phone = phoneEt.getText().toString();
        password = passwordEt.getText().toString();
        verifyCode = verifyCodeEt.getText().toString();
        recommentCode = recommednNumberEt.getText().toString();
        switch (v.getId()) {
            case R.id.register_change_usernumber:
                getUserCode();
                break;
            case R.id.register_get_verify_code:
                if (VerifyUtils.checkPhoneNumber(phone)) {
                    getVerifyCode();
                    downTimeUtil.countDownTimer.start();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.register_input_phone));
                }
                break;
            case R.id.register_service_protocal:
                Intent intent = new Intent(this, ProtocalActivity.class);
                intent.putExtra(EXTRA_FROM, FROM_ACT_THREE);
                startActivityForResult(intent, REQUEST_ACT_ONE);
                break;
            case R.id.register_confirm:
                getRecommendInfo();
                break;
        }
    }

    @Override
    public void success(int requestCode, String data) {
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
            case REQUEST_NET_ONE:
                userCode = Parsers.getUserCode(data);
                userNumber.setText(userCode);
                break;
            case REQUEST_NET_TWO:
                RecommenderEntity recommenderEntity = Parsers.getRecommender(data);
                String userCode = recommenderEntity.getUserCode();
                String user = recommenderEntity.getUserName();
                String phone = recommenderEntity.getPhone();
                DialogUtils.showRecommendInfoDialog(this, userCode, user, phone, v -> {
                    register();
                    DialogUtils.closeDialog();
                }, v -> DialogUtils.closeDialog());
                break;
            case REQUEST_NET_THREE://注册
                UserEntity userEntity = Parsers.getUserInfo(data);
                UserCenter.savaUserInfo(this, userEntity);
                if (4 != userEntity.getStatus()) {
                    BindPersonalInfoAcitvity.startBindPersonalInfoAcitvity(this);
                }
                finish();
                break;
            case REQUEST_NET_FOUR://获取验证码

                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        ToastUtil.shortShow(this, errorMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_ACT_ONE == requestCode) {
            servicePro.setCompoundDrawablesWithIntrinsicBounds(R.drawable.register_service_protocal_checked, 0, 0, 0);
            isCanRegister = true;
        }
    }

    private void getUserCode() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        requestHttpData(Constants.Urls.URL_POST_USER_USER_CODE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    private void getRecommendInfo() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("recommender", recommentCode);
        requestHttpData(Constants.Urls.URL_POST_USER_RECOMMEND_CODE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    private void register() {
        if (isCanRegister) {
            if (VerifyUtils.isPassword(this, password)) {
                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("userCode", userCode);
                params.put("loginName", phone);
                params.put("password", password);
                params.put("verifiationCode", verifyCode);
                params.put("recommender", recommentCode);
                requestHttpData(Constants.Urls.URL_POST_USER_REGISTER, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
            }
        } else {
            ToastUtil.shortShow(this, getString(R.string.protocal_agree_first));
        }
    }

    private void getVerifyCode() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("loginName", phone);
        requestHttpData(Constants.Urls.URL_POST_SMS_CODE, REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
    }
}
