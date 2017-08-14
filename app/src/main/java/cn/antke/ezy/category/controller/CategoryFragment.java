package cn.antke.ezy.category.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.RefreshRecyclerView;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseFragment;
import cn.antke.ezy.base.WebViewActivity;
import cn.antke.ezy.category.adapter.CategoryAdapter;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.home.adapter.ProductsAdapter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.BannerEntity;
import cn.antke.ezy.network.entities.CategoryEntity;
import cn.antke.ezy.network.entities.CategoryItemEntity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.product.controller.ProductDetailActivity;
import cn.antke.ezy.product.controller.StoreDetailActivity;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by liuzhichao on 2017/4/27.
 * 分类
 */
public class CategoryFragment extends BaseFragment implements AdapterView.OnItemClickListener, RefreshRecyclerView.OnRefreshAndLoadMoreListener, View.OnClickListener {

	@ViewInject(R.id.categrory_categrory)
	private ListView categrory;
	@ViewInject(R.id.categrory_banner)
	private ConvenientBanner banner;
	@ViewInject(R.id.categrory_goods)
	private RefreshRecyclerView recyclerView;

	private View view;
	private CategoryAdapter categoryAdapter;
	private String currentCategoryId;
	private ProductsAdapter productsAdapter;
	private List<CategoryItemEntity> categoryMainEntities;
	private List<BannerEntity> bannerEntities;
	private int requestCode;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.frag_categrory, null);
			ViewInjectUtils.inject(this, view);
			initView();
		}
		getCategory();
		ViewGroup mViewParent = (ViewGroup) view.getParent();
		if (mViewParent != null) {
			mViewParent.removeView(view);
		}
		return view;
	}

	private void initView() {
		initRecycleView();
		initBanner();
	}

	private void initBanner() {
		banner.setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_light})
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
				.startTurning(3000);
		banner.setOnItemClickListener(position -> {
			if (bannerEntities != null && bannerEntities.size() > position) {
				BannerEntity bannerEntity = bannerEntities.get(position);
				switch (bannerEntity.getType()) {
					case "1"://网页
						Intent intent = new Intent(getActivity(), WebViewActivity.class);
						intent.putExtra(CommonConstant.EXTRA_TITLE, bannerEntity.getTitle());
						intent.putExtra(CommonConstant.EXTRA_URL, bannerEntity.getLinkUrl());
						startActivity(intent);
						break;
					case "2"://店铺
						StoreDetailActivity.startStoreDetailActivity(getActivity(), bannerEntity.getLinkUrl());
						break;
					case "3"://商品
						ProductDetailActivity.startProductDetailActivity(getActivity(), bannerEntity.getLinkUrl());
						break;
				}
			}
		});
	}

	private void initRecycleView() {
		recyclerView.setMode(RefreshRecyclerView.Mode.BOTH);
		recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
		recyclerView.setOnRefreshAndLoadMoreListener(this);
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				if (parent.getLayoutManager() instanceof GridLayoutManager) {
					GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
					int spanCount = gridLayoutManager.getSpanCount();//设置的列数
					int headerSize = recyclerView.getHeaderSize();//头部数量
					int pos = parent.getChildLayoutPosition(view) - headerSize;//减去头部后的下标位置

					if (pos < 0) {//头部
						return;
					}

					//item左右偏移量(左右间距)，仅对2列有效
					if (pos % 2 == 0) {
						outRect.right = CommonTools.dp2px(getActivity(), 4);
					} else {
						outRect.left = CommonTools.dp2px(getActivity(), 4);
					}
					//item上下偏移量(上下间距)
					if (pos >= spanCount) {
						outRect.top = CommonTools.dp2px(getActivity(), 8);
					}
				}
			}
		});
		recyclerView.setCanAddMore(false);
		recyclerView.setOnRefreshAndLoadMoreListener(this);
	}

	private void getCategory() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_CATEGORY, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CategoryItemEntity checkEntity = categoryMainEntities.get(position);
		if (checkEntity.isChecked()) {
			return;
		}
		for (CategoryItemEntity entity : categoryMainEntities) {
			entity.setChecked(false);
		}
		checkEntity.setChecked(true);
		categoryAdapter.notifyDataSetChanged();
		currentCategoryId = checkEntity.getCategoryId();
//        productsAdapter.clearData();
		getGoods(false);
	}

	@Override
	public void parseData(int requestCode, String data) {
		super.parseData(requestCode, data);
		recyclerView.resetStatus();
		switch (requestCode) {
			case REQUEST_NET_ONE://默认选择第一个
				CategoryEntity categoryEntity = Parsers.getCategory(data);
				if (categoryEntity != null) {
					categoryMainEntities = categoryEntity.getCategoryItemEntities();
					bannerEntities = categoryEntity.getBannerEntities();
				} else {
					return;
				}

				banner.setPages(new CBViewHolderCreator<ImageHolder>() {

					@Override
					public ImageHolder createHolder() {
						return new ImageHolder();
					}
				}, bannerEntities);
				if (categoryMainEntities.size() > 0) {
					categoryMainEntities.get(0).setChecked(true);
					currentCategoryId = categoryMainEntities.get(0).getCategoryId();
					categoryAdapter = new CategoryAdapter(getActivity(), categoryMainEntities);
					categrory.setAdapter(categoryAdapter);
					categrory.setOnItemClickListener(this);
				}
				getGoods(false);

				break;
			case REQUEST_NET_TWO://选择某一个分类
			case REQUEST_NET_THREE://加载更多
				setGoods(data);
				break;
		}
	}

	@NonNull
	private void getGoods(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("category_id", currentCategoryId);
		params.put("is_easyBuyGoods", String.valueOf(2));
		int page = 1;
		requestCode = REQUEST_NET_TWO;
		if (isMore) {
			page = productsAdapter.getPage() + 1;
			requestCode = REQUEST_NET_THREE;
		}

		params.put(CommonConstant.PAGENUM, String.valueOf(page));
		params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
		requestHttpData(Constants.Urls.URL_POST_PLATE_PRODUCT_LIST, requestCode, FProtocol.HttpMethod.POST, params);
	}

	private void setGoods(String data) {
		PagesEntity<ProductDetailEntity> productPage = Parsers.getProductPage(data);
		if (productPage != null) {
			//添加商品的测试数据
			List<ProductDetailEntity> productEntities = productPage.getDatas();
			if (productsAdapter == null) {
				productsAdapter = new ProductsAdapter(productEntities, this);
				recyclerView.setAdapter(productsAdapter);
			} else {
				if (REQUEST_NET_TWO == requestCode) {
					productsAdapter.clearData();
				}
				productsAdapter.addDatas(productEntities);
				productsAdapter.notifyDataSetChanged();
			}
			if (productPage.getTotalPage() > productsAdapter.getPage()) {
				recyclerView.setCanAddMore(true);
			} else {
				recyclerView.setCanAddMore(false);
			}
		} else {
			ToastUtil.shortShow(getActivity(), getString(R.string.no_data_now));
		}
	}

	@Override
	public void onRefresh() {
		getGoods(false);
	}

	@Override
	public void onLoadMore() {
		getGoods(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_item_product_layout:
				ProductEntity productEntity = (ProductEntity) v.getTag();
				if (productEntity != null) {
					ProductDetailActivity.startProductDetailActivity(getActivity(), productEntity.getGoodsId());
				}
				break;
		}
	}

	private class ImageHolder implements Holder<BannerEntity> {

		private ImageView sdvBannerPic;

		@Override
		public View createView(Context context) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
			sdvBannerPic = (ImageView) view.findViewById(R.id.sdv_banner_pic);
			return view;
		}

		@Override
		public void UpdateUI(Context context, int position, BannerEntity data) {
			ImageUtils.setSmallImg(sdvBannerPic, data.getImgUrl());
		}
	}
}
