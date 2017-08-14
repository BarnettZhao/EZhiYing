package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.IntegralGiveEntity;

/**
 * Created by zww on 2017/6/19.
 */

public class IntegralGiveAdapter extends BaseAdapterNew<IntegralGiveEntity> {
    private Context context;
    private View.OnClickListener onClickListener;

    public IntegralGiveAdapter(Context context, List<IntegralGiveEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_integral_give;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        IntegralGiveEntity entity = getItem(position);

        TextView itemIntegral = ViewHolder.get(convertView, R.id.item_integral_give_integral);
        TextView itemStartTime = ViewHolder.get(convertView, R.id.item_integral_give_starttime);
        TextView itemEndTime = ViewHolder.get(convertView, R.id.item_integral_give_endtime);
        TextView itemBtn = ViewHolder.get(convertView, R.id.item_integral_give_btn);

        itemIntegral.setText(context.getString(R.string.product_sell_integral, entity.getIntegralNum()) +"    " + entity.getIntegralType());
        if (!StringUtil.isEmpty(entity.getStartData())) {
            itemStartTime.setText(context.getString(R.string.consumer_start_data, entity.getStartData()));
        } else {
            itemStartTime.setText("");
        }
        if (!StringUtil.isEmpty(entity.getEndData())) {
            itemEndTime.setText(context.getString(R.string.consumer_end_data, entity.getEndData()));
        } else {
            itemEndTime.setText("");
        }
        if ("1".equals(entity.getIsUsable())) {
            itemBtn.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
            itemBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_tran_button));
            itemBtn.setTag(entity);
            itemBtn.setOnClickListener(onClickListener);
        } else {
            itemBtn.setTextColor(ContextCompat.getColor(context, R.color.text_introduce_color));
            itemBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_gray_border));
        }
    }
}
