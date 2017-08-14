package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ConsumerServiceEntity;

/**
 * Created by zww on 2017/6/18.
 */

public class ConsumerServiceAdapter extends BaseAdapterNew<ConsumerServiceEntity> {
    private Context context;

    public ConsumerServiceAdapter(Context context, List<ConsumerServiceEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_consumer_service;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ConsumerServiceEntity entity = getItem(position);

        TextView itemData = ViewHolder.get(convertView, R.id.item_consumer_data);
        TextView itemDesc = ViewHolder.get(convertView, R.id.item_consumer_desc);
        TextView itemIntegral = ViewHolder.get(convertView, R.id.item_consumer_integral);

        itemData.setText(entity.getConsumerTime());
        itemDesc.setText(entity.getConsumerDesc());
        itemIntegral.setText(context.getString(R.string.product_sell_integral, entity.getConsumerTotal()));
    }
}
