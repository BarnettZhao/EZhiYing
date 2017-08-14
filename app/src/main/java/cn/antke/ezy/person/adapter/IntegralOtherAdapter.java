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
 * 其他积分列表
 */

public class IntegralOtherAdapter extends BaseAdapterNew<IntegralItemEntity> {
    private Context context;

    public IntegralOtherAdapter(Context context, List<IntegralItemEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_integral_other;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        IntegralItemEntity entity = getItem(position);
        TextView orderNumber = ViewHolder.get(convertView, R.id.integral_other_order_number);
        TextView integral = ViewHolder.get(convertView, R.id.integral_other_integral);
        TextView goodsName = ViewHolder.get(convertView, R.id.integral_other_goods_name);
        TextView time = ViewHolder.get(convertView, R.id.integral_other_time);


        orderNumber.setText(context.getString(R.string.integral_number, entity.getIntegralNo()));
        orderNumber.setVisibility(View.INVISIBLE);
        integral.setText(context.getString(R.string.product_sell_integral, entity.getTotal()));
        goodsName.setText(entity.getRemark());
        time.setText(entity.getTradeDate());
    }
}
