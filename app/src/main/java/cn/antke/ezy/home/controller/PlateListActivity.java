package cn.antke.ezy.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.home.adapter.PlateGridAdapter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PlateDetailEntity;
import cn.antke.ezy.product.controller.ProductListActivity;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/11.
 * 板块列表
 */
public class PlateListActivity extends ToolBarActivity {

	@ViewInject(R.id.gv_plate_grid)
	private GridView gvPlateGrid;

	private List<PlateDetailEntity> plateEntities;
	private String id;

	public static void startPlateListActivity(Context context, String id, String title) {
		Intent intent = new Intent(context, PlateListActivity.class);
		intent.putExtra(CommonConstant.EXTRA_ID, id);
		intent.putExtra(CommonConstant.EXTRA_TITLE, title);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_plate_list);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		id = getIntent().getStringExtra(CommonConstant.EXTRA_ID);
		String title = getIntent().getStringExtra(CommonConstant.EXTRA_TITLE);
		setLeftTitle(title);

		gvPlateGrid.setOnItemClickListener((parent, view, position, id) -> {
			if (plateEntities != null && plateEntities.size() > position) {
				PlateDetailEntity plateDetail = plateEntities.get(position);
				ProductListActivity.startProductListActivity(this, CommonConstant.FROM_PLATE_PRODUCT, plateDetail.getId(), plateDetail.getName(), "");
			}
		});
	}

	private void loadData() {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("channel_id", id);
		requestHttpData(Constants.Urls.URL_POST_PLATE_LIST, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			plateEntities = Parsers.getPlateDetailList(data);
			if (plateEntities != null && plateEntities.size() > 0) {
				PlateGridAdapter adapter = new PlateGridAdapter(this, plateEntities);
				gvPlateGrid.setAdapter(adapter);
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		ToastUtil.shortShow(this, errorMessage);
	}
}
