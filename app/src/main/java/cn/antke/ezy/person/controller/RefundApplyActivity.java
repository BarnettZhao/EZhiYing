package cn.antke.ezy.person.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.entities.ListDialogEntity;
import cn.antke.ezy.network.entities.ListDialogPageEntity;
import cn.antke.ezy.network.entities.OrderEntity;
import cn.antke.ezy.network.entities.OrderGoodEntity;
import cn.antke.ezy.person.adapter.OrderGoodAdapter;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;

/**
 * Created by zhaoweiwei on 2017/5/23.
 * 申请退款
 */

public class RefundApplyActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.order_detail_list)
    private ListView orderListView;
    @ViewInject(R.id.order_detail_price)
    private TextView orderPriceTv;
    @ViewInject(R.id.order_detail_btn1)
    private TextView orderBtn1;
    @ViewInject(R.id.order_detail_btn2)
    private TextView orderBtn2;

    private OrderEntity entity;
    private TextView store;
    private TextView status;
    private TextView goodPrice;
    private TextView freight;
    private TextView refundReason;
    private EditText refundExplain;
    private String reason;
    private String totalMoney;
    private String totalIntegral;
    private String orderCode;

    public static void startRefundApplyActivity(Context context, OrderEntity entity, String totalPrice, String totalIntegral,String orderCode) {
        Intent intent = new Intent(context, RefundApplyActivity.class);
        intent.putExtra(EXTRA_ENTITY, entity);
        intent.putExtra("totalPrice", totalPrice);
        intent.putExtra("totalIntegral", totalIntegral);
        intent.putExtra("orderCode", orderCode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_detail);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.person_order_refund_apply));
        initView();
    }

    private void initView() {
        entity = getIntent().getParcelableExtra(EXTRA_ENTITY);
        totalMoney = getIntent().getStringExtra("totalPrice");
        totalIntegral = getIntent().getStringExtra("totalIntegral");
        orderCode = getIntent().getStringExtra("orderCode");

        View topView = getLayoutInflater().inflate(R.layout.act_refund_apply_top, null);
        store = (TextView) topView.findViewById(R.id.order_refund_store);
        status = (TextView) topView.findViewById(R.id.order_refund_status);

        View bottomView = getLayoutInflater().inflate(R.layout.act_refund_apply_bottom, null);
        goodPrice = (TextView) bottomView.findViewById(R.id.order_refund_goods_price);
        freight = (TextView) bottomView.findViewById(R.id.order_refund_freight);
        refundReason = (TextView) bottomView.findViewById(R.id.order_refund_refund_reason);
        refundExplain = (EditText) bottomView.findViewById(R.id.order_refund_refund_explain);

        topView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, CommonTools.dp2px(this, 30)));
        bottomView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        orderListView.addHeaderView(topView);
        orderListView.addFooterView(bottomView);
        setData(entity);

        orderBtn1.setVisibility(View.GONE);
        orderBtn2.setVisibility(View.VISIBLE);
        orderBtn2.setText(getString(R.string.commit));
        orderBtn2.setOnClickListener(this);
        refundReason.setOnClickListener(this);
    }

    private void setData(OrderEntity entity) {
        List<OrderGoodEntity> goodEntities = entity.getGoodEntities();
        OrderGoodAdapter goodAdapter = new OrderGoodAdapter(this, goodEntities);
        orderListView.setAdapter(goodAdapter);

        store.setText(entity.getStoreName());
        String orderPrice = "";
        if (!StringUtil.isEmpty(entity.getOrderTotal()) && !"0".equals(entity.getOrderTotal())) {
            if (!StringUtil.isEmpty(entity.getOrderTotalInregral()) && !"0".equals(entity.getOrderTotalInregral())) {
                orderPrice = getString(R.string.person_order_good_price, getString(R.string.person_order_money_integral, entity.getOrderTotal(), entity.getOrderTotalInregral()));
            } else {
                orderPrice = getString(R.string.person_order_good_price, getString(R.string.product_sell_price2, entity.getOrderTotal()));
            }
        } else {
            if (!StringUtil.isEmpty(entity.getOrderTotalInregral()) && !"0".equals(entity.getOrderTotalInregral())) {
                orderPrice = getString(R.string.person_order_good_price, getString(R.string.product_sell_integral, entity.getOrderTotalInregral()));
            } else {
                goodPrice.setVisibility(View.GONE);
            }
        }
        goodPrice.setText(orderPrice);

        String logisticPrice = "";
        if (!StringUtil.isEmpty(entity.getLogisticCost()) && !"0".equals(entity.getLogisticCost())) {
            if (!StringUtil.isEmpty(entity.getLogisticCostIntegral()) && !"0".equals(entity.getLogisticCostIntegral())) {
                logisticPrice = getString(R.string.person_order_freight, getString(R.string.person_order_money_integral, entity.getLogisticCost(), entity.getLogisticCostIntegral()));
            } else {
                logisticPrice = getString(R.string.person_order_freight, getString(R.string.product_sell_price2, entity.getLogisticCost()));
            }
        } else {
            if (!StringUtil.isEmpty(entity.getLogisticCostIntegral()) && !"0".equals(entity.getLogisticCostIntegral())) {
                logisticPrice = getString(R.string.person_order_freight, getString(R.string.product_sell_integral, entity.getLogisticCostIntegral()));
            } else {
                freight.setVisibility(View.GONE);
            }
        }
        freight.setText(logisticPrice);

        String totalPrice;
        if (!StringUtil.isEmpty(totalMoney) && !"0".equals(totalMoney)) {
            if (!StringUtil.isEmpty(totalIntegral) && !"0".equals(totalIntegral)) {
                totalPrice = getString(R.string.person_orderdetail_order_price, getString(R.string.person_order_money_integral, totalMoney, totalIntegral));
            } else {
                totalPrice = getString(R.string.person_orderdetail_order_price, getString(R.string.product_sell_price2, totalMoney));
            }
        } else {
            if (!StringUtil.isEmpty(totalIntegral) && !"0".equals(totalIntegral)) {
                totalPrice = getString(R.string.person_orderdetail_order_price,
                        getString(R.string.product_sell_integral, totalIntegral));
            } else {
                totalPrice = getString(R.string.person_orderdetail_order_price, "");
            }
        }

        orderPriceTv.setText(totalPrice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_ACT_ONE == requestCode) {
            reason = data.getStringExtra(EXTRA_ENTITY);
            refundReason.setText(reason);
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        finish();
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        closeProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_refund_refund_reason:
                String[] datas = new String[]{"我不想买了",
                        "信息填写错误，重新拍",
                        "卖家缺货",
                        "其他原因"};
                List<ListDialogEntity> entities = new ArrayList<>();
                for (int i = 0; i < datas.length; i++) {
                    entities.add(new ListDialogEntity(datas[i], false));
                }
                ListDialogPageEntity listDialogPageEntity = new ListDialogPageEntity(entities);
                startActivityForResult(new Intent(RefundApplyActivity.this, ListDialogActivity.class).putExtra(EXTRA_ENTITY, listDialogPageEntity), REQUEST_ACT_ONE);
                break;
            case R.id.order_detail_btn2:
                String explain = refundExplain.getText().toString();
                if (!StringUtil.isEmpty(reason)) {
                    showProgressDialog();
                    IdentityHashMap<String, String> params = new IdentityHashMap<>();
                    params.put("order_code", orderCode);
                    params.put("refund_reason", explain);
                    params.put("refund", reason);
                    requestHttpData(Constants.Urls.URL_POST_ORDER_REFUND, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                } else {
                    ToastUtil.shortShow(RefundApplyActivity.this, "请选择退款原因");
                }
                break;
        }
    }
}
