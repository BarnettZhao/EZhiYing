package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.utils.LogUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2017/5/12.
 * 商品编辑
 */
public class GoodEditActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.store_edit_pic)
	private SimpleDraweeView editPic;
	@ViewInject(R.id.store_edit_add)
	private ImageView editAdd;
	@ViewInject(R.id.store_edit_good_name)
	private TextView editGoodName;
	@ViewInject(R.id.store_edit_good_describe)
	private TextView editGoodDescribe;
	@ViewInject(R.id.store_edit_category_ll)
	private LinearLayout editCategoryLl;
	@ViewInject(R.id.store_edit_category)
	private TextView editCategory;
	@ViewInject(R.id.store_edit_integral)
	private EditText editIntegral;
	@ViewInject(R.id.store_edit_stock)
	private EditText editStock;
	@ViewInject(R.id.store_edit_postage)
	private EditText editPostage;
	@ViewInject(R.id.store_edit_postage_unit)
	private TextView editPostageUnit;
	@ViewInject(R.id.store_edit_specification_ll)
	private LinearLayout editSpecificationLl;
	@ViewInject(R.id.store_edit_specification)
	private TextView editSpecification;
	@ViewInject(R.id.store_edit_limited)
	private EditText editLimited;
	@ViewInject(R.id.store_edit_container)
	private LinearLayout editContainer;
	@ViewInject(R.id.store_edit_add_new_type)
	private LinearLayout editAddNewType;
	@ViewInject(R.id.store_edit_good_detail)
	private TextView editGoodDetail;
	@ViewInject(R.id.store_edit_on_or_off)
	private TextView editOnOff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_store_edit);
		setLeftTitle(getString(R.string.store_good_edit));
		ViewInjectUtils.inject(this);
		editAdd.setOnClickListener(this);
		editCategoryLl.setOnClickListener(this);
		editPostageUnit.setOnClickListener(this);
		editSpecificationLl.setOnClickListener(this);
		editAddNewType.setOnClickListener(this);
		editGoodDetail.setOnClickListener(this);
		editOnOff.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.store_edit_add:
				break;
			case R.id.store_edit_category_ll:
				break;
			case R.id.store_edit_postage_unit:
				break;
			case R.id.store_edit_specification_ll:
				break;
			case R.id.store_edit_add_new_type:
				addView();
				break;
			case R.id.store_edit_good_detail:
				break;
			case R.id.store_edit_on_or_off:
				break;
			case R.id.item_store_edit_specification_ll:
				int itemPosition = (int) v.getTag();
				LogUtil.e("1ssssssssssssssss", itemPosition + "");
				break;
		}
	}

	private void addView() {
		View view = getLayoutInflater().inflate(R.layout.item_store_edit, null);
		EditText itemIntegral = (EditText) view.findViewById(R.id.item_store_edit_integral);
		EditText itemStock = (EditText) view.findViewById(R.id.item_store_edit_stock);
		EditText itemPostage = (EditText) view.findViewById(R.id.item_store_edit_postage);
		TextView itemPostageUnit = (TextView) view.findViewById(R.id.item_store_edit_postage_unit);
		LinearLayout itemSpecificationLl = (LinearLayout) view.findViewById(R.id.item_store_edit_specification_ll);
		TextView itemSpecification = (TextView) view.findViewById(R.id.item_store_edit_specification);
		EditText itemLimited = (EditText) view.findViewById(R.id.item_store_edit_limited);
		ImageView itemDelete = (ImageView) view.findViewById(R.id.item_store_edit_delete);
		editContainer.addView(view);
		int itemPosition = editContainer.indexOfChild(view);
		itemSpecificationLl.setTag(itemPosition);
		itemSpecificationLl.setOnClickListener(this);
	}
}
