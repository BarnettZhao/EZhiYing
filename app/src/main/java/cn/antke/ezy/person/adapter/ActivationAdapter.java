package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ActivationEntity;

/**
 * Created by zhaoweiwei on 2017/5/18.
 * 激活
 */

public class ActivationAdapter extends BaseAdapterNew<ActivationEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public ActivationAdapter(Context context, List<ActivationEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_consumer_activation;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ActivationEntity entity = getItem(position);
        TextView itemData = ViewHolder.get(convertView, R.id.item_activation_time);
        TextView itemUserCode = ViewHolder.get(convertView, R.id.item_activation_usercode);
        TextView itemIntegral = ViewHolder.get(convertView, R.id.item_activation_integral);

        if (entity != null) {
            itemData.setText(entity.getActivationData());
            itemUserCode.setText(entity.getUserCode());
            itemIntegral.setText(context.getString(R.string.consumer_activation_integral,String.valueOf(entity.getIntegral())));
            if (entity.isSelected()) {
                itemData.setCompoundDrawablesWithIntrinsicBounds(R.drawable.activation_selected, 0, 0, 0);
            } else {
                itemData.setCompoundDrawablesWithIntrinsicBounds(R.drawable.activation_normal, 0, 0, 0);
            }
            itemData.setTag(entity);
            itemData.setOnClickListener(onClickListener);
        }
    }
}
