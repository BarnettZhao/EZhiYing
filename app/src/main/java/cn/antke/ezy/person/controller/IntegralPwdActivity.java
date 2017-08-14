package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;

/**
 * Created by zhaoweiwei on 2017/5/19.
 * 积分交易密码
 */

public class IntegralPwdActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.integral_pwd_set)
    private TextView setPwd;
    @ViewInject(R.id.login_pwd_change)
    private TextView loginPwdChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_integtal_trading_pwd);
        initView();
    }

    private void initView() {
        setLeftTitle(getString(R.string.person_pwd_set));
        ViewInjectUtils.inject(this);
        if (UserCenter.isSetPwd(this)) {
            setPwd.setText(getString(R.string.integral_trading_pwd_reset));
        } else {
            setPwd.setText(getString(R.string.integral_trading_pwd_set));
        }

        setPwd.setOnClickListener(this);
        loginPwdChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.integral_pwd_set:
//                if (UserCenter.isSetPwd(this)) {
                    startActivityForResult(new Intent(IntegralPwdActivity.this, ResetPwdActivity.class), REQUEST_ACT_ONE);
//                } else {
//                    SetPassWordActivity.startSetPassWordActivity(this, FROM_PERSON);
//                }
                break;
            case R.id.login_pwd_change:
                startActivity(new Intent(this, LoginPwdChangeActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_ACT_ONE == requestCode) {
            setPwd.setText(getString(R.string.integral_trading_pwd_reset));
        }
    }
}
