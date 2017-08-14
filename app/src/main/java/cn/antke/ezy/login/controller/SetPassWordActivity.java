package cn.antke.ezy.login.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.person.controller.PersonInfoActivity;
import cn.antke.ezy.widget.PasswordInputView;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.FROM_BIND;
import static cn.antke.ezy.common.CommonConstant.FROM_RESET;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;

/**
 * Created by zhaoweiwei on 2017/5/4.
 * 设置交易密码
 */

public class SetPassWordActivity extends ToolBarActivity {

    private int from;
    private String phone;
    private String code;
    private PasswordInputView passwordInputView;

    public static void startSetPassWordActivity(Context context, int from) {
        Intent intent = new Intent(context, SetPassWordActivity.class);
        intent.putExtra(EXTRA_FROM, from);
        context.startActivity(intent);
    }

    public static void startSetPassWordActivity(Activity activity, int requestCode, String phone, String code) {
        Intent intent = new Intent(activity, SetPassWordActivity.class);
        intent.putExtra(EXTRA_FROM, FROM_RESET);
        intent.putExtra(CommonConstant.EXTRA_PHONE, phone);
        intent.putExtra(CommonConstant.EXTRA_CODE, code);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set_password);
        initView();
    }

    private void initView() {
        from = getIntent().getIntExtra(EXTRA_FROM, FROM_RESET);
        phone = getIntent().getStringExtra(CommonConstant.EXTRA_PHONE);
        code = getIntent().getStringExtra(CommonConstant.EXTRA_CODE);

        passwordInputView = (PasswordInputView) findViewById(R.id.set_password_input);

        if (FROM_RESET == from) {//重置积分交易密码
            setLeftTitle(getString(R.string.integral_trading_pwd_reset));
        } else {//注册流程
            setLeftTitle(getString(R.string.setpassword_title));
            setRightText(getString(R.string.jump));
        }

        rightText.setOnClickListener(v -> {
            startActivity(new Intent(this, PersonInfoActivity.class).putExtra(EXTRA_FROM, FROM_ACT_TWO));
            finish();
        });

        passwordInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = passwordInputView.getText().toString();
                if (6 == pwd.length()) {
                    showProgressDialog();
                    IdentityHashMap<String, String> params = new IdentityHashMap<>();
                    String requestUrl;
                    if (FROM_RESET == from) {
                        //修改
                        params.put("loginName", phone);
                        params.put("verifiationCode", code);
                        params.put("password", pwd);
                        params.put("type", "2");
                        params.put("user_code", UserCenter.getUserCode(SetPassWordActivity.this));
                        requestUrl = Constants.Urls.URL_POST_RESET_PASSWORD;
                    } else {
                        params.put("integralPassword", pwd);
                        requestUrl = Constants.Urls.URL_POST_SET_PASSWORD;
                    }
                    requestHttpData(requestUrl, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                }
            }
        });
    }

    @Override
    public void success(int requestCode, String data) {
        closeProgressDialog();
        Entity result = Parsers.getResult(data);
        if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            UserCenter.setIsSetPwd(this, true);
            if (FROM_RESET == from) {
                setResult(RESULT_OK);
            } else if (FROM_BIND == from) {
                startActivity(new Intent(SetPassWordActivity.this, PersonInfoActivity.class).putExtra(EXTRA_FROM, FROM_ACT_TWO));
            } else {
                finish();
            }
            finish();
        } else {
            passwordInputView.getText().clear();
            ToastUtil.shortShow(this, result.getResultMsg());
        }

        UserCenter.setIsSetPwd(this, true);
    }
}
