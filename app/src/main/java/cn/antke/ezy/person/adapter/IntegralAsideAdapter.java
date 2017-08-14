package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.IntegralItemEntity;

/**
 * Created by zhaoweiwei on 2017/5/26.
 * 待用积分
 */

public class IntegralAsideAdapter extends BaseAdapterNew<IntegralItemEntity> {
    private Context context;

    public IntegralAsideAdapter(Context context, List<IntegralItemEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_integral_aside;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        IntegralItemEntity entity = getItem(position);
        TextView orderNumber = ViewHolder.get(convertView, R.id.integral_aside_order_number);
        TextView returnIntegral = ViewHolder.get(convertView, R.id.integral_aside_consume_return);
        TextView confirmTime = ViewHolder.get(convertView, R.id.integral_aside_confirm_time);
        TextView returnTiem = ViewHolder.get(convertView, R.id.integral_aside_return_time);

        orderNumber.setText(context.getString(R.string.integral_number, entity.getIntegralNo()));
        orderNumber.setVisibility(View.INVISIBLE);
        returnIntegral.setText(entity.getRemark() + "：" + context.getString(R.string.product_sell_integral, entity.getTotal()));
        confirmTime.setText(entity.getTradeDate());
        returnTiem.setText(entity.getReturnDate());
    }
}
