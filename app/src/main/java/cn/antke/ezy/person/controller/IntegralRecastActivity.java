package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.RecommenderEntity;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;
import static cn.antke.ezy.common.CommonConstant.TYPE_1;

/**
 * Created by zww on 2017/6/19.
 * 积分激活
 */

public class IntegralRecastActivity extends ToolBarActivity {
    @ViewInject(R.id.consumer_service_usercode)
    private TextView consumerCode;
    @ViewInject(R.id.give_operate_usernumber)
    private EditText userCodeTv;
    @ViewInject(R.id.give_operate_realname)
    private TextView nameTv;
    @ViewInject(R.id.give_operate_phone)
    private TextView phoneTv;
    @ViewInject(R.id.activate_integral)
    private TextView activateIntegral;
    @ViewInject(R.id.give_operate_ingeral)
    private EditText giveIntegralEt;
    @ViewInject(R.id.give_operate_give)
    private TextView giveBtn;
    private String userCode;

    public static void startIntegralRecastActivity(Context context, int type) {
        Intent intent = new Intent(context, IntegralRecastActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_integral_give_operate);
        ViewInjectUtils.inject(this);
        int type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_1);

        activateIntegral.setText(getString(R.string.integral_activate_integral));
        giveBtn.setText(getString(R.string.confirm));
        if (TYPE_1 == type) {//激活
            setLeftTitle(getString(R.string.activation));
        } else {//复投
            setLeftTitle(getString(R.string.integral_recast));
        }

        giveBtn.setOnClickListener(view -> {
            String integral = giveIntegralEt.getText().toString();
            if (!StringUtil.isEmpty(integral)) {
                giveBtn.setEnabled(false);
                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("integral", integral);
                params.put("user_code", userCode);
                requestHttpData(Constants.Urls.URL_POST_INTEGRAL_RECAST, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
            } else {
                ToastUtil.shortShow(IntegralRecastActivity.this, getString(R.string.integral_exchange_mines));
            }
        });

        userCodeTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userCode = userCodeTv.getText().toString();
                if (7 == userCode.length()) {
                    showProgressDialog();
                    IdentityHashMap<String, String> params = new IdentityHashMap<>();
                    params.put("recommender", userCode);
                    requestHttpData(Constants.Urls.URL_POST_USER_RECOMMEND_CODE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                }
            }
        });
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_TWO://激活
                giveBtn.setEnabled(true);
                finish();
                break;
            case REQUEST_NET_ONE://获取推荐人信息
                RecommenderEntity recommenderEntity = Parsers.getRecommender(data);
                String user = recommenderEntity.getUserName();
                String phone = recommenderEntity.getPhone();

                nameTv.setText(user);
                phoneTv.setText(phone);
                break;
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        ToastUtil.shortShow(this,errorMessage);
        giveBtn.setEnabled(true);
    }
}
