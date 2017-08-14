package cn.antke.ezy.product.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.RefreshRecyclerView;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ShopCarEntity;
import cn.antke.ezy.product.adapter.ShopCarAdapter;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/22.
 * 购物车
 */
public class ShopCarActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.rrv_shop_car_list)
	private RefreshRecyclerView rrvShopCarList;
	@ViewInject(R.id.ll_shop_car_total)
	private View llShopCarTotal;
	@ViewInject(R.id.cb_shop_car_all)
	private CheckBox cbShopCarAll;
	@ViewInject(R.id.tv_shop_car_amount)
	private TextView tvShopCarAmount;
	@ViewInject(R.id.tv_shop_car_pay)
	private TextView tvShopCarPay;
	@ViewInject(R.id.tv_shop_car_delete)
	private View tvShopCarDelete;

	private List<ShopCarEntity> shopCarEntities;
	private ShopCarAdapter shopCarAdapter;

	public static void startShopCarActivity(Context context) {
		Intent intent = new Intent(context, ShopCarActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_shop_car);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.shop_car));

		rrvShopCarList.setHasFixedSize(true);
		rrvShopCarList.setMode(RefreshRecyclerView.Mode.BOTH);
		rrvShopCarList.setOnRefreshAndLoadMoreListener(new RefreshRecyclerView.OnRefreshAndLoadMoreListener() {
			@Override
			public void onRefresh() {
				loadData(false);
			}

			@Override
			public void onLoadMore() {
				loadData(true);
			}
		});
		rrvShopCarList.setLayoutManager(new LinearLayoutManager(this));
		rrvShopCarList.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				int headerSize = rrvShopCarList.getHeaderSize();//头部数量
				int pos = parent.getChildLayoutPosition(view) - headerSize;//减去头部后的下标位置

				if (pos < 0) {//头部
					return;
				}

				//item上下偏移量(上下间距)
				outRect.bottom = CommonTools.dp2px(ShopCarActivity.this, 10);
			}
		});

		cbShopCarAll.setOnClickListener(v -> {
			if (shopCarEntities != null) {
				for (ShopCarEntity shopCarEntity : shopCarEntities) {
					shopCarEntity.setChecked(cbShopCarAll.isChecked());
					for (ProductDetailEntity productEntity : shopCarEntity.getProductDetailEntities()) {
						productEntity.setChecked(cbShopCarAll.isChecked());
					}
				}
				updateCount();
				if (shopCarAdapter != null) {
					shopCarAdapter.notifyDataSetChanged();
				}
			}
		});
		tvShopCarPay.setOnClickListener(this);
		tvShopCarDelete.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData(false);
	}

	private void loadData(boolean isMore) {
		showProgressDialog();
		int requestCode = CommonConstant.REQUEST_NET_ONE;
		if (isMore) {
			requestCode = CommonConstant.REQUEST_NET_TWO;
		}
		requestHttpData(Constants.Urls.URL_POST_SHOP_CAR_LIST, requestCode, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void success(int requestCode, String data) {
		rrvShopCarList.resetStatus();
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:{
					PagesEntity<ShopCarEntity> shopCarPage = Parsers.getShopCarPage(data);
					shopCarEntities = shopCarPage.getDatas();
					shopCarAdapter = new ShopCarAdapter(shopCarEntities, this);
					rrvShopCarList.setAdapter(shopCarAdapter);
					rrvShopCarList.smoothScrollToPosition(0);
					updateCount();
					if (shopCarPage.getTotalPage() > 1) {
						rrvShopCarList.setCanAddMore(true);
					} else {
						rrvShopCarList.setCanAddMore(false);
					}
					break;
				}
				case CommonConstant.REQUEST_NET_TWO:{
					PagesEntity<ShopCarEntity> shopCarPage = Parsers.getShopCarPage(data);
					shopCarEntities.addAll(shopCarPage.getDatas());

					shopCarAdapter.notifyDataSetChanged();
					if (shopCarAdapter.getPage() >= shopCarPage.getTotalPage()) {
						rrvShopCarList.setCanAddMore(false);
					}
					break;
				}
				case CommonConstant.REQUEST_NET_THREE:{
					loadData(false);
					break;
				}
				case CommonConstant.REQUEST_NET_FOUR:{
					loadData(false);
					break;
				}
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		rrvShopCarList.resetStatus();
		closeProgressDialog();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_shop_car_pay:{
				//支付
				if (shopCarEntities != null) {
					String cartIds = "";
					for (ShopCarEntity shopCarEntity : shopCarEntities) {
						for (ProductDetailEntity productDetailEntity : shopCarEntity.getProductDetailEntities()) {
							if (productDetailEntity.isChecked()) {
								//记录购买的商品，购买完刷新
								cartIds += (productDetailEntity.getCartId() + ",");
							}
						}
					}
					if (!TextUtils.isEmpty(cartIds)) {
						ConfirmOrderActivity.startConfirmOrderActivity(this, CommonConstant.FROM_SHOP_CAR_LIST, cartIds);
					} else {
						ToastUtil.shortShow(this, "请选择要购买的商品");
					}
				}
				break;
			}
			case R.id.tv_shop_car_delete:{
				//删除商品
				if (shopCarEntities != null) {
					String cartIds = "";
					for (ShopCarEntity shopCarEntity : shopCarEntities) {
						for (ProductDetailEntity productDetailEntity : shopCarEntity.getProductDetailEntities()) {
							if (productDetailEntity.isChecked()) {
								//记录被删除的商品，删完刷新
								cartIds += (productDetailEntity.getCartId() + ",");
							}
						}
					}
					showProgressDialog();
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put("cart_id", cartIds);
					requestHttpData(Constants.Urls.URL_POST_DELETE_SHOP_CAR, CommonConstant.REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
				}
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (CommonConstant.REQUEST_ACT_ONE == requestCode && RESULT_OK == resultCode) {
			String count = data.getStringExtra(CommonConstant.EXTRA_NUM);
			String skuId = data.getStringExtra(CommonConstant.EXTRA_CODE);
			int parentPosition = data.getIntExtra("parentPosition", -1);
			int position = data.getIntExtra("position", -1);

			ShopCarEntity shopCarEntity = shopCarEntities.get(parentPosition);
			ProductDetailEntity productDetailEntity = shopCarEntity.getProductDetailEntities().get(position);

			showProgressDialog();
			IdentityHashMap<String, String> params = new IdentityHashMap<>();
			params.put("store_id", shopCarEntity.getId());
			params.put("del_sku_id", productDetailEntity.getSkuId());
			params.put("attr_relationids", skuId);
			params.put("count", count);
			params.put("goods_id", productDetailEntity.getGoodsId());
			requestHttpData(Constants.Urls.URL_POST_ALTER_SHOP_CAR, CommonConstant.REQUEST_NET_FOUR, FProtocol.HttpMethod.POST, params);
		}
	}

	/**
	 * 更新总计，根据店铺选中数量控制全选状态
	 */
	public void updateCount() {
		boolean hasEditMode = false;
		int count = 0;
		int integral = 0;
		int price = 0;
		if (shopCarEntities != null) {
			int checkNum = 0;
			for (ShopCarEntity shopCarEntity : shopCarEntities) {
				if (shopCarEntity.isEditMode()) {
					hasEditMode = true;
				}
				if (shopCarEntity.isChecked()) {
					checkNum++;
				}
				for (ProductDetailEntity productEntity : shopCarEntity.getProductDetailEntities()) {
					if (productEntity.isChecked()) {
						count++;
						int sellIntegral = StringUtil.parseInt(productEntity.getGoodsIntegral(), 0);
						int sellPrice = StringUtil.parseInt(productEntity.getGoodsPrice(), 0);
						integral += (sellIntegral * productEntity.getCount());
						price += (sellPrice * productEntity.getCount());
					}
				}
			}
			if (shopCarEntities.size() == 0) {
				cbShopCarAll.setChecked(false);
			} else {
				cbShopCarAll.setChecked(checkNum == shopCarEntities.size());
			}
		}
		tvShopCarAmount.setText(getString(R.string.total_integral, InputUtil.formatPrice(integral, price)));
		tvShopCarPay.setText(getString(R.string.go_settlement, String.valueOf(count)));
		editMode(hasEditMode);
	}

	/**
	 * 编辑模式处理
	 */
	public void editMode(boolean isEdit) {
		if (isEdit) {
			cbShopCarAll.setVisibility(View.INVISIBLE);
			llShopCarTotal.setVisibility(View.INVISIBLE);
			tvShopCarPay.setVisibility(View.GONE);
			tvShopCarDelete.setVisibility(View.VISIBLE);
		} else {
			cbShopCarAll.setVisibility(View.VISIBLE);
			llShopCarTotal.setVisibility(View.VISIBLE);
			tvShopCarPay.setVisibility(View.VISIBLE);
			tvShopCarDelete.setVisibility(View.GONE);
		}
	}
}
