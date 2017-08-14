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

/**
 * Created by zhaoweiwei on 2017/5/14.
 */

public class StoreOrderListAdapter extends BaseAdapterNew<OrderEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public StoreOrderListAdapter(Context context, List<OrderEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_store_order;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        OrderEntity entity = getItem(position);

        TextView orderCode = ViewHolder.get(convertView, R.id.item_store_order_number);
        TextView orderStatus = ViewHolder.get(convertView, R.id.item_store_order_status);
        ListView goodListView = ViewHolder.get(convertView, R.id.item_store_order_list);
        TextView refundReason = ViewHolder.get(convertView, R.id.item_store_order_refund);
        TextView orderTotalTv = ViewHolder.get(convertView, R.id.item_store_order_integral);
        TextView orderLogistic = ViewHolder.get(convertView, R.id.item_store_order_freight);
        RelativeLayout btnLayout = ViewHolder.get(convertView,R.id.item_store_order_btn_ll);
        TextView orderBtn = ViewHolder.get(convertView, R.id.item_store_order_btn);
        TextView applyPerson = ViewHolder.get(convertView, R.id.item_store_order_apply);
        TextView applyPhone = ViewHolder.get(convertView, R.id.item_store_order_phone);
        TextView orderAddress = ViewHolder.get(convertView, R.id.item_store_order_address);

        if (entity != null) {
            String statusName = "";
            //22：待发货  23：已发货 24：已完成  25：退款申请     26：已退款   27.退款中
            switch (entity.getStatus()) {
                case "22":
                    statusName = "待发货";
                    break;
                case "23":
                    statusName = "已发货";
                    break;
                case "24":
                    statusName = "已完成";
                    break;
                case "25":
                    statusName = "退款申请";
                    break;
                case "26":
                    statusName = "已退款";
                    break;
                case "27":
                    statusName = "退款中";
                    break;
            }
            orderStatus.setText(statusName);
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
            if (!StringUtil.isEmpty(orderPrice)) {
                orderTotalTv.setVisibility(View.VISIBLE);
                orderTotalTv.setText(orderPrice);
            } else {
                orderTotalTv.setVisibility(View.GONE);
            }

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

            if (!StringUtil.isEmpty(logisticPrice)) {
                orderLogistic.setVisibility(View.VISIBLE);
                orderLogistic.setText(logisticPrice);
            } else {
                orderLogistic.setVisibility(View.GONE);
            }

            OrderGoodAdapter adapter = new OrderGoodAdapter(context, entity.getGoodEntities());
            goodListView.setAdapter(adapter);
            goodListView.setTag(entity);

            applyPerson.setText(context.getString(R.string.store_order_apply_person, entity.getConsignee()));
            applyPhone.setText(entity.getContacts());
            orderAddress.setText(entity.getAddress());

            orderBtn.setTag(entity);
            orderBtn.setOnClickListener(onClickListener);
            //22：待发货  23：已发货 24：已完成  25：退款申请     26：已退款   27.退款中
            switch (entity.getStatus()) {
                case "21"://待发货

                    break;
                case "22"://待发货
                    orderCode.setText(context.getString(R.string.person_orderdetail_order_number, entity.getOrderCode()));
                    orderAddress.setVisibility(View.VISIBLE);
                    refundReason.setVisibility(View.GONE);
                    btnLayout.setVisibility(View.VISIBLE);
                    orderBtn.setText(context.getString(R.string.store_order_delive));
                    break;
                case "23"://待收货
                    orderCode.setText(context.getString(R.string.person_orderdetail_order_number, entity.getOrderCode()));
                    orderAddress.setVisibility(View.VISIBLE);
                    refundReason.setVisibility(View.GONE);
                    btnLayout.setVisibility(View.GONE);
                    break;
                case "24"://已完成
                    orderCode.setText(context.getString(R.string.person_orderdetail_order_number, entity.getOrderCode()));
                    orderAddress.setVisibility(View.VISIBLE);
                    refundReason.setVisibility(View.GONE);
                    btnLayout.setVisibility(View.GONE);
                    break;
                case "25"://退款申请
                    orderCode.setText(context.getString(R.string.person_orderdetail_refund_number, entity.getRefundCode()));
                    orderAddress.setVisibility(View.GONE);
                    refundReason.setVisibility(View.VISIBLE);
                    btnLayout.setVisibility(View.VISIBLE);
                    refundReason.setText(context.getString(R.string.person_order_refund_reason2, entity.getRefundReason()));
                    orderBtn.setText(context.getString(R.string.store_order_refund));
                    break;
                case "26"://已退款
                    orderCode.setText(context.getString(R.string.person_orderdetail_refund_number, entity.getRefundCode()));
                    orderAddress.setVisibility(View.GONE);
                    refundReason.setVisibility(View.VISIBLE);
                    btnLayout.setVisibility(View.GONE);
                    refundReason.setText(context.getString(R.string.person_order_refund_reason2,entity.getRefundReason()));
                    break;
                case "27"://退款中
                    orderCode.setText(context.getString(R.string.person_orderdetail_refund_number, entity.getRefundCode()));
                    orderAddress.setVisibility(View.GONE);
                    refundReason.setVisibility(View.VISIBLE);
                    btnLayout.setVisibility(View.GONE);
                    refundReason.setText(context.getString(R.string.person_order_refund_reason2,entity.getRefundReason()));
                    break;
            }
        }
    }
}
