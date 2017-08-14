package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.AddressEntity;

/**
 * Created by zhaoweiwei on 2016/12/25.
 * 地址列表
 */

public class AddressAdapter extends BaseAdapterNew<AddressEntity> {
    private View.OnClickListener onClickListener;
    private Resources resources;

    public AddressAdapter(Context context, List<AddressEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        resources = context.getResources();
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int Position) {
        return R.layout.item_person_address;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        AddressEntity entity = getItem(position);
        TextView userName = ViewHolder.get(convertView, R.id.address_item_username);
        TextView phone = ViewHolder.get(convertView, R.id.address_item_phone);
        TextView address = ViewHolder.get(convertView, R.id.address_item_address);
        TextView defaultAddress = ViewHolder.get(convertView, R.id.address_item_default);
        TextView edit = ViewHolder.get(convertView, R.id.address_item_edit);

        edit.setTag(entity);
        edit.setOnClickListener(onClickListener);

        //1是默认 0不是默认
        if (entity.isDefault()) {
            defaultAddress.setTextColor(resources.getColor(R.color.primary_color_text));
            defaultAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_checked, 0, 0, 0);
            defaultAddress.setOnClickListener(null);
        } else {
            defaultAddress.setTextColor(resources.getColor(R.color.text_introduce_color));
            defaultAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_normal, 0, 0, 0);
            defaultAddress.setTag(entity);
            defaultAddress.setOnClickListener(onClickListener);
        }
        userName.setText(resources.getString(R.string.person_address_user, entity.getUserName()));
        phone.setText(entity.getUserPhone());
        address.setText(resources.getString(R.string.person_address_address, entity.getProvinceName() + entity.getCityName() + entity.getDistrictName() + entity.getAddress()));
    }
}
