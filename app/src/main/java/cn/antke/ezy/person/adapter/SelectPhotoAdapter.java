package cn.antke.ezy.person.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;

import java.util.Arrays;

import cn.antke.ezy.R;

/**
 * Created by Liu_ZhiChao on 2015/9/8 14:13.
 * 拍照和相册
 */
public class SelectPhotoAdapter extends BaseAdapterNew<String> {

	private static final int[] SELECT_ICONS_LIST = {R.drawable.person_camera_select_icon,R.drawable.person_photo_select_icon};
	private Resources resources;

	public SelectPhotoAdapter(Activity activity) {
		super(activity, Arrays.asList(activity.getString(R.string.personinfo_info_avatar_camera),activity.getString(R.string.personinfo_info_avatar_photo)));
		resources = activity.getResources();
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_person_photo_select;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		TextView personPhotoItemName = (TextView) convertView.findViewById(R.id.person_photo_item_name);
		View personPhotoItemDivide = convertView.findViewById(R.id.person_photo_item_divide);
		personPhotoItemName.setText(getItem(position));
		personPhotoItemName.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(SELECT_ICONS_LIST[position]), null, null, null);
		if (getCount() - 1 > position){
			personPhotoItemDivide.setVisibility(View.VISIBLE);
		}
	}
}
