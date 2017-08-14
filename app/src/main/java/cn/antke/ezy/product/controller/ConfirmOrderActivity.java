package cn.antke.ezy.product.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.view.ListViewForInner;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.AddressEntity;
import cn.antke.ezy.network.entities.CreateOrderEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.OrderConfirmEntity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.StoreDetailEntity;
import cn.antke.ezy.pay.controller.OnlinePayActivity;
import cn.antke.ezy.pay.controller.PayResultActivity;
import cn.antke.ezy.person.controller.AddressListActivity;
import cn.antke.ezy.person.controller.OrderListActivity;
import cn.antke.ezy.product.adapter.OrderStoreAdapter;
import cn.antke.ezy.product.adapter.OrderWayAdapter;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.ExitManager;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.R.id.ll_confirm_order_address;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_PAYING;

/**
 * Created by liuzhichao on 2017/5/19.
 * 确认订单
 */
public class ConfirmOrderActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(ll_confirm_order_address)
    private View llConfirmOrderAddress;
    @ViewInject(R.id.tv_confirm_order_user_name)
    private TextView tvConfirmOrderUserName;
    @ViewInject(R.id.tv_confirm_order_phone)
    private TextView tvConfirmOrderPhone;
    @ViewInject(R.id.tv_confirm_order_address)
    private TextView tvConfirmOrderAddress;
    @ViewInject(R.id.lvfi_confirm_order_way)
    private ListViewForInner lvfiConfirmOrderWay;
    @ViewInject(R.id.lvfi_confirm_order_store)
    private ListViewForInner lvfiConfirmOrderStore;
    @ViewInject(R.id.tv_confirm_order_amount)
    private TextView tvConfirmOrderAmount;
    @ViewInject(R.id.tv_confirm_order_postage_amount)
    private TextView tvConfirmOrderPostageAmount;
    @ViewInject(R.id.tv_confirm_order_buy)
    private View tvConfirmOrderBuy;

    private int from;
    private String cartIds;
    private String productId;
    private String storeId;
    private String attrId;
    private String count;
    private OrderConfirmEntity orderConfirm;
    private String type;
    private OrderWayAdapter wayAdapter;
    private int currentTypePosition;
    private String skuId = "";
    private int multiple = 1;

    public static void startConfirmOrderActivity(Context context, int from, String cartIds) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra(CommonConstant.EXTRA_FROM, from);
        intent.putExtra("cartIds", cartIds);
        context.startActivity(intent);
    }

    public static void startConfirmOrderActivity(Context context, int from, String productId, String storeId, String attrId, String count) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra(CommonConstant.EXTRA_FROM, from);
        intent.putExtra("productId", productId);
        intent.putExtra("storeId", storeId);
        intent.putExtra("attrId", attrId);
        intent.putExtra("count", count);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_confirm_order);
        ExitManager.instance.addBuyNowActivity(this);
        ViewInjectUtils.inject(this);
        initView();
        loadData();
    }

    private void initView() {
        setLeftTitle(getString(R.string.integral_confirm));
        from = getIntent().getIntExtra(CommonConstant.EXTRA_FROM, 0);
        cartIds = getIntent().getStringExtra("cartIds");
        productId = getIntent().getStringExtra("productId");
        storeId = getIntent().getStringExtra("storeId");
        attrId = getIntent().getStringExtra("attrId");
        count = getIntent().getStringExtra("count");

        lvfiConfirmOrderWay.setOnItemClickListener((parent, view, position, id) -> {
            if (orderConfirm != null && wayAdapter != null) {
                List<OrderConfirmEntity.PayWay> integralPayList = orderConfirm.getIntegralPayList();
                OrderConfirmEntity.PayWay payWay = integralPayList.get(position);
                if (!payWay.isSelected()) {
                    integralPayList.get(currentTypePosition).setSelected(false);
                    payWay.setSelected(true);
                    currentTypePosition = position;
                    type = payWay.getIntegralType();
                    multiple = payWay.getMultiple();
                    updateTotal();
                    wayAdapter.notifyDataSetChanged();
                }
            }
        });

        llConfirmOrderAddress.setOnClickListener(this);
        tvConfirmOrderBuy.setOnClickListener(this);
    }

    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        String requestUrl;
        if (CommonConstant.FROM_PRODUCT_DETAIL == from) {
            params.put("goods_id", productId);
            params.put("attr_relationids", attrId);
            params.put("goods_num", count);
            params.put("store_id", storeId);
            requestUrl = Constants.Urls.URL_POST_BUY_NOW;
        } else {
            params.put("cart_list", cartIds);
            requestUrl = Constants.Urls.URL_POST_SHOP_CAR_ORDER;
        }
        requestHttpData(requestUrl, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        closeProgressDialog();
        Entity entity = Parsers.getResult(data);
        if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
            switch (requestCode) {
                case CommonConstant.REQUEST_NET_ONE:
                    orderConfirm = Parsers.getOrderConfirm(data);
                    refreshAddress();
                    if (orderConfirm != null) {
                        List<OrderConfirmEntity.PayWay> integralPayList = orderConfirm.getIntegralPayList();
                        if (integralPayList != null && integralPayList.size() > 0) {
                            currentTypePosition = 0;
                            OrderConfirmEntity.PayWay payWay = integralPayList.get(currentTypePosition);
                            payWay.setSelected(true);
                            type = payWay.getIntegralType();
                            multiple = payWay.getMultiple();
                            wayAdapter = new OrderWayAdapter(this, integralPayList);
                            lvfiConfirmOrderWay.setAdapter(wayAdapter);
                        }

                        List<StoreDetailEntity> storeList = orderConfirm.getStoreList();
                        if (CommonConstant.FROM_PRODUCT_DETAIL == from) {
                            //从商品直接下单的需要在这里获取sku_id
                            try {
                                skuId = storeList.get(0).getProductEntities().get(0).getSkuId();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        updateTotal();
                        //总邮费计算
                        int logisticsIntegral = 0;
                        int logisticsPrice = 0;
                        for (StoreDetailEntity storeDetailEntity : storeList) {
                            for (ProductDetailEntity productDetailEntity : storeDetailEntity.getProductEntities()) {
                                //这里都是计算过数量的，不必重复计算
                                logisticsIntegral += StringUtil.parseInt(productDetailEntity.getLogisticsIntegral(), 0);
                                logisticsPrice += StringUtil.parseInt(productDetailEntity.getLogisticsCost(), 0);
                            }
                        }
                        tvConfirmOrderPostageAmount.setText(getString(R.string.person_order_freight, InputUtil.formatLogistics(logisticsIntegral, logisticsPrice)));

                        //显示店铺以及商品
                        OrderStoreAdapter adapter = new OrderStoreAdapter(this, storeList);
                        lvfiConfirmOrderStore.setAdapter(adapter);
                    }
                    break;
                case CommonConstant.REQUEST_NET_TWO: {
                    CreateOrderEntity createOrder = Parsers.getCreateOrder(data);
                    //如果有积分需要支付，先通过密码支付积分，如果没有则直接进入支付方式页面
                    if (StringUtil.parseInt(createOrder.getOrderIntegral(), 0) > 0) {
                        DialogUtils.showPwdInputDialog(this, (v, editText) -> {
                            String pwd = editText.getText().toString();
                            showProgressDialog();
                            IdentityHashMap<String, String> params = new IdentityHashMap<>();
                            params.put("order_id", createOrder.getOrderId());
                            params.put("integral_password", pwd);
                            requestHttpData(Constants.Urls.URL_POST_INTEGRAL_PAY, CommonConstant.REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
                            DialogUtils.closeDialog();
                        }, view -> {
                            //待支付订单
                            startActivity(new Intent(ConfirmOrderActivity.this, OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_PAYING));
                            DialogUtils.closeDialog();
                            finish();
                        });
                    } else {
                        OnlinePayActivity.startOnlinePayActivity(this, createOrder.getOrderId(), createOrder.getOrderTotal());
                    }
                    break;
                }
                case CommonConstant.REQUEST_NET_THREE: {
                    CreateOrderEntity createOrder = Parsers.getCreateOrder(data);
                    if (StringUtil.parseInt(createOrder.getOrderTotal(), 0) > 0) {
                        OnlinePayActivity.startOnlinePayActivity(this, createOrder.getOrderId(), createOrder.getOrderTotal());
                    } else {
                        PayResultActivity.startPayResultActivity(this, createOrder.getTotal(), createOrder.getPayType(), 0);
                    }
                    finish();
                    break;
                }
            }
        } else {
            ToastUtil.shortShow(this, entity.getResultMsg());
            if (requestCode == CommonConstant.REQUEST_NET_ONE) {
                finish();
            }
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        closeProgressDialog();
        super.mistake(requestCode, status, errorMessage);
        if (requestCode == CommonConstant.REQUEST_NET_ONE) {
            finish();
        }
    }

    //商品总价格计算
    private void updateTotal() {
        int productIntegral = 0;
        int productPrice = 0;
        if (orderConfirm != null) {
            for (StoreDetailEntity storeDetailEntity : orderConfirm.getStoreList()) {
                for (ProductDetailEntity productDetailEntity : storeDetailEntity.getProductEntities()) {
                    //这里都是计算过数量的，不必重复计算
                    productIntegral += StringUtil.parseInt(productDetailEntity.getOrderIntegral(), 0);
                    productPrice += StringUtil.parseInt(productDetailEntity.getOrderTotal(), 0);
                }
            }
        }
        tvConfirmOrderAmount.setText(getString(R.string.total_integral, InputUtil.formatPrice(productIntegral * multiple, productPrice)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case ll_confirm_order_address:
                Intent intent = new Intent(this, AddressListActivity.class);
                intent.putExtra(CommonConstant.EXTRA_FROM, CommonConstant.FROM_CONFIRM_ORDER);
                startActivityForResult(intent, CommonConstant.REQUEST_ACT_ONE);
                break;
            case R.id.tv_confirm_order_buy:
                //结算
                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("receiving_id", orderConfirm.getReceivingId());
                params.put("integral_type", type);
                if (CommonConstant.FROM_PRODUCT_DETAIL == from) {
                    params.put("type", "2");
                    params.put("sku_id", skuId);
                    params.put("goods_num", count);
                    params.put("store_id", storeId);
                } else {
                    params.put("type", "1");
                    params.put("cart_list", cartIds);
                }
                requestHttpData(Constants.Urls.URL_POST_CREATE_ORDER, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && CommonConstant.REQUEST_ACT_ONE == requestCode && data != null) {
            AddressEntity address = data.getParcelableExtra("address");
            if (address != null && orderConfirm != null) {
                orderConfirm.setReceivingId(address.getReciveId());
                orderConfirm.setConsignee(address.getUserName());
                orderConfirm.setContacts(address.getUserPhone());
                orderConfirm.setAddress(address.getProvinceName() + address.getCityName() + address.getDistrictName() + address.getAddress());
                refreshAddress();
            }
        }
    }

    /**
     * 刷新收货人信息
     */
    private void refreshAddress() {
        if (orderConfirm != null && !TextUtils.isEmpty(orderConfirm.getReceivingId())) {
            tvConfirmOrderUserName.setText(orderConfirm.getConsignee());
            tvConfirmOrderPhone.setText(orderConfirm.getContacts());
            tvConfirmOrderAddress.setText(orderConfirm.getAddress());
        }
    }
}
