package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.OrderEntity;
import cn.antke.ezy.person.controller.OrderDetailActivity;

/**
 * Created by zhaoweiwei on 2017/5/11.
 * 订单列表
 */

public class OrderListAdapter extends BaseAdapterNew<OrderEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public OrderListAdapter(Context context, List<OrderEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_person_order;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        OrderEntity entity = getItem(position);
        TextView storeName = ViewHolder.get(convertView, R.id.item_order_store);
        TextView orderStatus = ViewHolder.get(convertView, R.id.item_order_status);
        ListView goodList = ViewHolder.get(convertView, R.id.item_order_list);
        TextView orderPriceTv = ViewHolder.get(convertView, R.id.item_order_integral);
        TextView orderFreight = ViewHolder.get(convertView, R.id.item_order_freight);
        RelativeLayout orderBtnLayout = ViewHolder.get(convertView,R.id.item_order_btn_layout);
        TextView orderBtn1 = ViewHolder.get(convertView, R.id.item_order_btn1);
        TextView orderBtn2 = ViewHolder.get(convertView, R.id.item_order_btn2);

        if (entity != null) {
            storeName.setText(entity.getStoreName());
            orderStatus.setText(entity.getStatusName());
            String orderPrice = "";
            if (!StringUtil.isEmpty(entity.getOrderTotal()) && !"0".equals(entity.getOrderTotal())) {
                if (!StringUtil.isEmpty(entity.getOrderTotalInregral()) && !"0".equals(entity.getOrderTotalInregral())) {
                    orderPrice = context.getString(R.string.person_order_good_price, context.getString(R.string.person_order_money_integral, entity.getOrderTotal(), entity.getOrderTotalInregral()));
                } else {
                    orderPrice = context.getString(R.string.person_order_good_price, context.getString(R.string.product_sell_price2, entity.getOrderTotal()));
                }
            } else {
                if (!StringUtil.isEmpty(entity.getOrderTotalInregral()) && !"0".equals(entity.getOrderTotalInregral())) {
                    orderPrice = context.getString(R.string.person_order_good_price, context.getString(R.string.product_sell_integral, entity.getOrderTotalInregral()));
                }
            }
            orderPriceTv.setText(orderPrice);

            String logisticPrice = "";
            if (!StringUtil.isEmpty(entity.getLogisticCost()) && !"0".equals(entity.getLogisticCost())) {
                if (!StringUtil.isEmpty(entity.getLogisticCostIntegral()) && !"0".equals(entity.getLogisticCostIntegral())) {
                    logisticPrice = context.getString(R.string.person_order_freight, context.getString(R.string.person_order_money_integral, entity.getLogisticCost(), entity.getLogisticCostIntegral()));
                } else {
                    logisticPrice = context.getString(R.string.person_order_freight, context.getString(R.string.product_sell_price2, entity.getLogisticCost()));
                }
            } else {
                if (!StringUtil.isEmpty(entity.getLogisticCostIntegral()) && !"0".equals(entity.getLogisticCostIntegral())) {
                    logisticPrice = context.getString(R.string.person_order_freight, context.getString(R.string.product_sell_integral, entity.getLogisticCostIntegral()));
                }
            }
            orderFreight.setText(logisticPrice);

            OrderGoodAdapter adapter = new OrderGoodAdapter(context, entity.getGoodEntities());
            goodList.setAdapter(adapter);
            goodList.setTag(entity);
            goodList.setOnItemClickListener((parent, view, position1, id) -> OrderDetailActivity.startOrderDetailActivity(context, entity));

            orderBtn1.setTag(entity);
            orderBtn1.setOnClickListener(onClickListener);
            orderBtn2.setTag(entity);
            orderBtn2.setOnClickListener(onClickListener);
            //订单状态：1，待支付；2，待发货；3，已发货； 4.已完成；5，取消订单（待支付状态）6、退款申请；7.退款中；8.已退款；9，拒绝 。
            switch (entity.getStatus()) {
                case "1"://待支付
                    orderBtn1.setVisibility(View.VISIBLE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn1.setText(context.getString(R.string.person_order_cancel));
                    orderBtn2.setText(context.getString(R.string.person_order_pay));
                    break;
                case "2"://待发货
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(context.getString(R.string.person_order_refund));
                    break;
                case "3"://待收货
                    orderBtn1.setVisibility(View.VISIBLE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn1.setText(context.getString(R.string.person_order_logistic_detail));
                    orderBtn2.setText(context.getString(R.string.person_order_get_ensure));
                    break;
                case "4"://已完成
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(context.getString(R.string.person_order_logistic_detail));
//                    orderBtn2.setText(context.getString(R.string.person_order_buy_again));
                    break;
                case "5"://取消
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(context.getString(R.string.delete));
                    break;
                case "6"://退款申请
                    orderBtn1.setVisibility(View.VISIBLE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn1.setText(context.getString(R.string.person_order_refund_detail));
                    orderBtn2.setText(context.getString(R.string.person_order_refund_cancel));
                    break;
                case "7"://退款中
                case "8"://已退款
                case "9"://拒绝退款
                    orderBtn1.setVisibility(View.GONE);
                    orderBtn2.setVisibility(View.VISIBLE);
                    orderBtn2.setText(context.getString(R.string.person_order_refund_detail));
                    break;
                case "10":
                    orderBtnLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
