package cn.antke.ezy.product.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import cn.antke.ezy.home.adapter.ProductsAdapter;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/11.
 * 商品列表
 */
public class ProductListActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.fl_product_sort_num)
	private View flProductSortNum;
	@ViewInject(R.id.tv_product_sort_num_text)
	private TextView tvProductSortNumText;
	@ViewInject(R.id.fl_product_sort_price)
	private View flProductSortPrice;
	@ViewInject(R.id.tv_product_sort_price_text)
	private TextView tvProductSortPriceText;
	@ViewInject(R.id.rrv_product_list)
	private RefreshRecyclerView rrvProductList;

	private TextView preSort;
	private boolean isHightToLow;
	private String id;
	private int from;
	private ProductsAdapter adapter;
	private String orderFiled;
	private String orderType;
	private String content;

	public static void startProductListActivity(Context context, int from, String id, String title, String content) {
		Intent intent = new Intent(context, ProductListActivity.class);
		intent.putExtra(CommonConstant.EXTRA_FROM, from);
		intent.putExtra(CommonConstant.EXTRA_ID, id);
		intent.putExtra(CommonConstant.EXTRA_TITLE, title);
		intent.putExtra("content", content);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_product_list);
		ViewInjectUtils.inject(this);
		initView();
		loadData(false);
	}

	private void initView() {
		from = getIntent().getIntExtra(CommonConstant.EXTRA_FROM, 0);
		id = getIntent().getStringExtra(CommonConstant.EXTRA_ID);
		content = getIntent().getStringExtra("content");
		String title = getIntent().getStringExtra(CommonConstant.EXTRA_TITLE);
		if (StringUtil.isEmpty(title)) {
			title = getString(R.string.product_list);
		}
		setLeftTitle(title);

		tvProductSortNumText.setSelected(true);
		tvProductSortNumText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.product_down_green_icon, 0);
		isHightToLow = true;
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
						outRect.right = CommonTools.dp2px(ProductListActivity.this, 4);
					} else {
						outRect.left = CommonTools.dp2px(ProductListActivity.this, 4);
					}
					//item上下偏移量(上下间距)
					outRect.top = CommonTools.dp2px(ProductListActivity.this, 8);
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
	}

	private void loadData(boolean isMore) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);

		int pageNum = 1;
		int requestCode = CommonConstant.REQUEST_NET_ONE;
		if (isMore) {
			pageNum = adapter.getPage() + 1;
			requestCode = CommonConstant.REQUEST_NET_TWO;
		}
		params.put(CommonConstant.PAGENUM, String.valueOf(pageNum));

		params.put("orderField", orderFiled);
		params.put("orderType", orderType);
		params.put("condition", content);
		String requestUrl;
		if (from == CommonConstant.FROM_HOME_PRODUCT) {
			params.put("channel_id", id);
			requestUrl = Constants.Urls.URL_POST_HOME_PRODUCT_LIST;
		} else if (from == CommonConstant.FROM_PERSONAL_PRODUCT) {
			params.put("store_id", UserCenter.getStoreId(this));
			requestUrl = Constants.Urls.URL_POST_STORE_PRODUCT;
		}else if (from == CommonConstant.FROM_SPECIAL_PRODUCT){
			params.put("category_id", id);
			params.put("is_easyBuyGoods", String.valueOf(1));
			requestUrl = Constants.Urls.URL_POST_PLATE_PRODUCT_LIST;
		} else {
			params.put("category_id", id);
			requestUrl = Constants.Urls.URL_POST_PLATE_PRODUCT_LIST;
		}
		requestHttpData(requestUrl, requestCode, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		rrvProductList.resetStatus();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:{
					PagesEntity<ProductDetailEntity> productPage = Parsers.getProductPage(data);
					if (productPage != null) {
						//添加商品的测试数据
						List<ProductDetailEntity> productEntities = productPage.getDatas();
						if (from == CommonConstant.FROM_PERSONAL_PRODUCT) {
							adapter = new ProductsAdapter(productEntities, null);
						} else {
							adapter = new ProductsAdapter(productEntities, this);
						}
						rrvProductList.setAdapter(adapter);
						if (productPage.getTotalPage() > 1) {
							rrvProductList.setCanAddMore(true);
						} else {
							rrvProductList.setCanAddMore(false);
						}
					} else {
						ToastUtil.shortShow(this, getString(R.string.no_data_now));
					}
					break;
				}
				case CommonConstant.REQUEST_NET_TWO:{
					PagesEntity<ProductDetailEntity> productPage = Parsers.getProductPage(data);
					if (productPage != null) {
						List<ProductDetailEntity> productEntities = productPage.getDatas();
						adapter.addDatas(productEntities);
						if (productPage.getTotalPage() <= adapter.getPage()) {
							rrvProductList.setCanAddMore(false);
						}
					} else {
						ToastUtil.shortShow(this, getString(R.string.no_data_now));
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
		closeProgressDialog();
		rrvProductList.resetStatus();
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
