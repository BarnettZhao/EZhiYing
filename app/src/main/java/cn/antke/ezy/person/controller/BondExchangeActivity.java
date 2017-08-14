package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
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
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.BondExchangeEntity;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/19.
 * 债券兑换
 */

public class BondExchangeActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.bond_account)
    private TextView bondAccount;
    @ViewInject(R.id.bond_usercode)
    private TextView bondUserCode;
    @ViewInject(R.id.bond_multi_function)
    private TextView bondMultiFunction;
    @ViewInject(R.id.bond_bond)
    private EditText bondBond;
    @ViewInject(R.id.bond_exchange)
    private TextView bondExchange;

    private String bondAllNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bond_exchange);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.person_bond_exchange));
        setRightText(getString(R.string.bond_recycle));
        hideTitleLine();

        rightText.setOnClickListener(this);
        bondExchange.setOnClickListener(this);
        bondUserCode.setText(UserCenter.getUserCode(this));
        getBond();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bond_exchange:
                String bondNum = bondBond.getText().toString();
                if (!StringUtil.isEmpty(bondNum)) {
                    int bondNumInt = Integer.parseInt(bondNum);
                    if (bondNumInt > 0) {
                        DialogUtils.showTwoBtnDialog(this, getString(R.string.person_bond_exchange),
                                getString(R.string.bond_is_exchange),
                                null,
                                v12 -> {
                                    showProgressDialog();
                                    IdentityHashMap<String, String> params = new IdentityHashMap<>();
                                    params.put("bond", bondNum);
                                    requestHttpData(Constants.Urls.URL_POST_BOND_EXCHANGE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
                                    DialogUtils.closeDialog();
                                },
                                v1 -> DialogUtils.closeDialog());
                    } else {
                        ToastUtil.shortShow(this, getString(R.string.bond_mine_num));
                    }

                }
                break;
            case R.id.rigth_text:
                startActivity(new Intent(this, BondRecycleActivity.class).putExtra(EXTRA_ENTITY, bondAllNum));
                break;
        }
    }

    public void getBond() {
        showProgressDialog();
        requestHttpData(Constants.Urls.URL_POST_BOND_NUM, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, new IdentityHashMap<>());
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_ONE://获取债券数
                BondExchangeEntity bondExchangeEntity = Parsers.getBond(data);
                if (bondExchangeEntity != null) {
                    bondAllNum = getString(R.string.bond_unit, bondExchangeEntity.getBond());
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder(bondAllNum);
                    stringBuilder.setSpan(new AbsoluteSizeSpan(CommonTools.dp2px(this, 18)), bondExchangeEntity.getBond().length(), bondAllNum.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    bondAccount.setText(stringBuilder);
                    bondMultiFunction.setText(bondExchangeEntity.getMulIntegral());
                    bondBond.setText("");
                    bondBond.setHint(getString(R.string.bond_most_exchange, bondExchangeEntity.getExchangeBond()));
                    bondBond.setHintTextColor(ContextCompat.getColor(this, R.color.text_introduce_color));
                }
                break;
            case REQUEST_NET_TWO://债券兑换
                getBond();
                break;
        }
    }
}
