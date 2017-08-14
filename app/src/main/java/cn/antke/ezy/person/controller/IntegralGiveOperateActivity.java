package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.entities.IntegralGiveEntity;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;

/**
 * Created by zww on 2017/6/19.
 * 积分赠送
 */

public class IntegralGiveOperateActivity extends ToolBarActivity {
    @ViewInject(R.id.give_operate_usernumber)
    private EditText userCodeTv;
    @ViewInject(R.id.give_operate_realname)
    private TextView nameTv;
    @ViewInject(R.id.give_operate_phone)
    private TextView phoneTv;
    @ViewInject(R.id.give_operate_ingeral)
    private EditText giveIntegralEt;
    @ViewInject(R.id.give_operate_give)
    private TextView giveBtn;
    private String giveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_integral_give_operate);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.integral_give));



        String integral = "";
        IntegralGiveEntity entity = getIntent().getParcelableExtra(EXTRA_ENTITY);
        if (entity!=null) {
            giveId = entity.getGiveId();
            integral = entity.getIntegralNum();
        }

        userCodeTv.setEnabled(false);
        userCodeTv.setText(UserCenter.getUserCode(this));
        nameTv.setText(UserCenter.getUserName(this));
        phoneTv.setText(UserCenter.getPhone(this));
        giveIntegralEt.setHint(getString(R.string.integral_give_max,integral));

        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String integral = giveIntegralEt.getText().toString();
                if (!StringUtil.isEmpty(integral) && !StringUtil.isEmpty(giveId)) {
                    showProgressDialog();
                    IdentityHashMap<String, String> params = new IdentityHashMap<>();
                    params.put("integral", integral);
                    params.put("integral_id", giveId);
                    requestHttpData(Constants.Urls.URL_POST_INTEGRAL_DOUBLY, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                }
            }
        });
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        finish();
    }
}
