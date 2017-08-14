package cn.antke.ezy.product.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.RefreshRecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.home.adapter.ProductsAdapter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.network.entities.StoreDetailEntity;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/17.
 * 店铺详情
 */
public class StoreDetailActivity extends BaseActivity implements View.OnClickListener {

	@ViewInject(R.id.iv_store_back)
	private View ivStoreBack;
	@ViewInject(R.id.sdv_store_pic)
	private SimpleDraweeView sdvStorePic;
	@ViewInject(R.id.et_store_search)
	private EditText etStoreSearch;
	@ViewInject(R.id.sdv_store_logo)
	private SimpleDraweeView sdvStoreLogo;
	@ViewInject(R.id.tv_store_name)
	private TextView tvStoreName;
	@ViewInject(R.id.tv_store_merchant)
	private TextView tvStoreMerchant;
	@ViewInject(R.id.fl_product_sort_num)
	private View flProductSortNum;
	@ViewInject(R.id.tv_product_sort_num_text)
	private TextView tvProductSortNumText;
	@ViewInject(R.id.fl_product_sort_price)
	private View flProductSortPrice;
	@ViewInject(R.id.tv_product_sort_price_text)
	private TextView tvProductSortPriceText;
	@ViewInject(R.id.fl_product_sort_time)
	private View flProductSortTime;
	@ViewInject(R.id.tv_product_sort_time_text)
	private TextView tvProductSortTimeText;
	@ViewInject(R.id.rrv_product_list)
	private RefreshRecyclerView rrvProductList;

	private TextView preSort;
	private boolean isHightToLow = true;
	private String id;
	private String content;
	private ProductsAdapter adapter;
	private String orderFiled;
	private String orderType;

	public static void startStoreDetailActivity(Context context, String id) {
		Intent intent = new Intent(context, StoreDetailActivity.class);
		intent.putExtra(CommonConstant.EXTRA_ID, id);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_store_detail_info);
		ViewInjectUtils.inject(this);
		initView();
		loadData(false);
	}

	private void initView() {
		id = getIntent().getStringExtra(CommonConstant.EXTRA_ID);

		tvProductSortNumText.setSelected(true);
		tvProductSortNumText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.product_down_green_icon, 0);
		preSort = tvProductSortNumText;
		orderFiled = CommonConstant.ORDER_FILED_VOLUME;
		orderType = CommonConstant.ORDER_TYPE_DESC;

		rrvProductList.setHasFixedSize(true);
		rrvProductList.setMode(RefreshRecyclerView.Mode.BOTH);
		rrvProductList.setLayoutManager(new GridLayoutManager(this, 2));
		rrvProductList.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				if (parent.getLayoutManager() instanceof GridLayoutManager) {
					GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
					int spanCount = gridLayoutManager.getSpanCount();//设置的列数
					int headerSize = rrvProductList.getHeaderSize();//头部数量
					int pos = parent.getChildLayoutPosition(view) - headerSize;//减去头部后的下标位置

					if (pos < 0) {//头部
						return;
					}

					//item左右偏移量(左右间距)，仅对2列有效
					if (pos % 2 == 0) {
						outRect.right = CommonTools.dp2px(StoreDetailActivity.this, 4);
					} else {
						outRect.left = CommonTools.dp2px(StoreDetailActivity.this, 4);
					}
					//item上下偏移量(上下间距)
					outRect.top = CommonTools.dp2px(StoreDetailActivity.this, 8);
				}
			}
		});
		rrvProductList.setOnRefreshAndLoadMoreListener(new RefreshRecyclerView.OnRefreshAndLoadMoreListener() {
			@Override
			public void onRefresh() {
				loadData(false);
			}

			@Override
			public void onLoadMore() {
				loadData(true);
			}
		});

		flProductSortNum.setOnClickListener(this);
		flProductSortPrice.setOnClickListener(this);
		flProductSortTime.setOnClickListener(this);

		etStoreSearch.setOnEditorActionListener((v, actionId, event) -> {
			if (EditorInfo.IME_ACTION_SEARCH == actionId) {
				content = etStoreSearch.getText().toString().trim();
				loadData(false);
			}
			return false;
		});
		ivStoreBack.setOnClickListener(this);
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("store_id", id);
		params.put("condition", content);
		params.put("orderField", orderFiled);
		params.put("orderType", orderType);
		params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
		int page = 1;
		int requestCode = CommonConstant.REQUEST_NET_ONE;
		if (isMore) {
			page = adapter.getPage() + 1;
			requestCode = CommonConstant.REQUEST_NET_TWO;
		}
		params.put(CommonConstant.PAGENUM, String.valueOf(page));
		requestHttpData(Constants.Urls.URL_POST_STORE_DETAIL, requestCode, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		rrvProductList.resetStatus();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:{
					StoreDetailEntity storeDetail = Parsers.getStoreDetail(data);

					ImageUtils.setSmallImg(sdvStorePic, storeDetail.getPicUrl());
					ImageUtils.setSmallImg(sdvStoreLogo, storeDetail.getLogo());
					tvStoreName.setText(storeDetail.getName());
					tvStoreMerchant.setText(storeDetail.getMerchant());

					adapter = new ProductsAdapter(storeDetail.getProductEntities(), this);
					rrvProductList.setAdapter(adapter);
					if (storeDetail.getTotalPage() > 1) {
						rrvProductList.setCanAddMore(true);
					} else {
						rrvProductList.setCanAddMore(false);
					}
					break;
				}
				case CommonConstant.REQUEST_NET_TWO:{
					StoreDetailEntity storeDetail = Parsers.getStoreDetail(data);
					adapter.addDatas(storeDetail.getProductEntities());
					if (storeDetail.getTotalPage() <= adapter.getPage()) {
						rrvProductList.setCanAddMore(false);
					}
					break;
				}
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		rrvProductList.resetStatus();
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_store_back:
				finish();
				break;
			case R.id.fl_product_sort_num:
				orderFiled = CommonConstant.ORDER_FILED_VOLUME;
				sort(tvProductSortNumText);
				break;
			case R.id.fl_product_sort_price:
				orderFiled = CommonConstant.ORDER_FILED_PRICE;
				sort(tvProductSortPriceText);
				break;
			case R.id.ll_item_product_layout:
				ProductEntity productEntity = (ProductEntity) v.getTag();
				if (productEntity != null) {
					ProductDetailActivity.startProductDetailActivity(this, productEntity.getGoodsId());
				}
				break;
		}
	}

	private void sort(TextView textView) {
		if (preSort != textView) {
			//重置前一个排序条件,默认一开始都是从高到低
			preSort.setSelected(false);
			preSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.product_down_black_icon, 0);
			isHightToLow = false;//因为需要使排序默认为箭头向下，所以设置上一次箭头是朝上
		}
		if (isHightToLow) {
			textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.product_up_green_icon, 0);
			isHightToLow = false;
			orderType = CommonConstant.ORDER_TYPE_ASC;
		} else {
			textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.product_down_green_icon, 0);
			isHightToLow = true;
			orderType = CommonConstant.ORDER_TYPE_DESC;
		}
		textView.setSelected(true);
		preSort = textView;
		loadData(false);
	}
}
