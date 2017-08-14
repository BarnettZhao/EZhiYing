package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ID;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;

/**
 * Created by zww on 2017/6/24.
 */

public class StoreLogisticActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.store_logistic_close)
    private TextView close;
    @ViewInject(R.id.store_logistic_company)
    private TextView company;
    @ViewInject(R.id.store_logistic_no)
    private EditText logisticNoEt;
    @ViewInject(R.id.store_logistic_confirm)
    private TextView confirm;
    private String logisticName;
    private String logisticId;
    private String orderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_logistic_choose);
        ViewInjectUtils.inject(this);
        orderCode = getIntent().getStringExtra(EXTRA_ID);
        initStyle();
        initOnclick();
    }

    private void initOnclick() {
        close.setOnClickListener(this);
        company.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private void initStyle() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = CommonTools.dp2px(this, 284);
        this.onWindowAttributesChanged(wl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.store_logistic_close:
                finish();
                break;
            case R.id.store_logistic_company:
                startActivityForResult(new Intent(this,LogisticCompanyActivity.class),REQUEST_ACT_ONE);
                break;
            case R.id.store_logistic_confirm:
                String logisticNo = logisticNoEt.getText().toString();
                deliver(logisticNo);
                break;
        }
    }

    private void deliver(String logisticNo) {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("order_code", orderCode);
        params.put("logistics_id", logisticId);
        params.put("logistics_no", logisticNo);
        params.put("invoice_name", logisticName);
        requestHttpData(Constants.Urls.URL_POST_STORE_DELIVER, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_ACT_ONE == requestCode) {
            logisticName = data.getStringExtra("name");
            logisticId = data.getStringExtra("id");
            company.setText(logisticName);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.actionsheet_dialog_out);
    }
}
