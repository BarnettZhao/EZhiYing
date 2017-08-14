package cn.antke.ezy.home.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseFragment;
import cn.antke.ezy.base.WebViewActivity;
import cn.antke.ezy.common.CommonConfig;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.draw.controller.DrawListActivity;
import cn.antke.ezy.home.adapter.PlateAdapter;
import cn.antke.ezy.home.adapter.ProductsAdapter;
import cn.antke.ezy.home.listener.OnItemHeightGetListener;
import cn.antke.ezy.login.controller.LoginActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.BannerEntity;
import cn.antke.ezy.network.entities.HomeEntity;
import cn.antke.ezy.network.entities.PlateEntity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.product.controller.ProductDetailActivity;
import cn.antke.ezy.product.controller.ProductListActivity;
import cn.antke.ezy.product.controller.ShopCarActivity;
import cn.antke.ezy.product.controller.StoreDetailActivity;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by liuzhichao on 2017/4/27.
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, RefreshRecyclerView.OnRefreshAndLoadMoreListener {

	@ViewInject(R.id.tv_select_language)
	private TextView tvSelectLanguage;
	@ViewInject(R.id.fl_home_search)
	private View flHomeSearch;
	@ViewInject(R.id.iv_home_scan)
	private View ivHomeScan;
	@ViewInject(R.id.rrv_home_list)
	private RefreshRecyclerView rrvHomeList;
	@ViewInject(R.id.cb_home_banner)
	private ConvenientBanner cbHomeBanner;
	@ViewInject(R.id.cb_home_plate)
	private ConvenientBanner cbHomePlate;

	private View view;
	private View topView;

	private List<BannerEntity> bannerEntities;
	private List<PlateEntity> plateEntities;
	private ProductsAdapter productsAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.frag_home, null);
			topView = inflater.inflate(R.layout.layout_home_top, null);
			ViewInjectUtils.inject(this, view);
			ViewInjectUtils.inject(this, topView);
			initView();
		}
		ViewGroup mViewParent = (ViewGroup) view.getParent();
		if (mViewParent != null) {
			mViewParent.removeView(view);
		}
		return view;
	}

	private void initView() {
		rrvHomeList.addHeaderView(topView);
		rrvHomeList.setHasFixedSize(true);
		rrvHomeList.setMode(RefreshRecyclerView.Mode.PULL_FROM_START);
		rrvHomeList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
		rrvHomeList.setOnRefreshAndLoadMoreListener(this);
		rrvHomeList.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				if (parent.getLayoutManager() instanceof GridLayoutManager) {
					GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
					int spanCount = gridLayoutManager.getSpanCount();//设置的列数
					int headerSize = rrvHomeList.getHeaderSize();//头部数量
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

		cbHomeBanner.setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_light})
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
				.startTurning(3000);
		cbHomePlate.setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_light})
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
		cbHomePlate.setCanLoop(false);
		cbHomePlate.setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_light})
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
		cbHomePlate.setcurrentitem(0);
		cbHomeBanner.setOnItemClickListener(position -> {
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

		tvSelectLanguage.setOnClickListener(this);
		flHomeSearch.setOnClickListener(this);
		ivHomeScan.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData();
	}

	private void loadData() {
		requestHttpData(Constants.Urls.URL_POST_HOME, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		tvSelectLanguage.setText(UserCenter.getSiteName(getActivity()));
	}

	@Override
	public void success(int requestCode, String data) {
		rrvHomeList.resetStatus();
		switch (requestCode) {
			case CommonConstant.REQUEST_NET_ONE:{
				HomeEntity homeEntity = Parsers.getHomeEntity(data);
				if (homeEntity != null) {
					if (CommonConstant.REQUEST_NET_SUCCESS.equals(homeEntity.getResultCode())) {
						bannerEntities = homeEntity.getBannerEntities();
						plateEntities = homeEntity.getPlateEntities();
						List<ProductDetailEntity> productEntities = homeEntity.getProductEntities();

						cbHomeBanner.setPages(new CBViewHolderCreator<ImageHolder>() {

							@Override
							public ImageHolder createHolder() {
								return new ImageHolder();
							}
						}, bannerEntities);

						if (plateEntities != null && plateEntities.size() > 0) {
							List<List<PlateEntity>> platePages = new ArrayList<>();
							//以6个为一页来分页
							int pageSize;
							if (plateEntities.size() % CommonConfig.HOME_SPECIAL_ONE_PAGE_NUM == 0) {
								pageSize = plateEntities.size() / CommonConfig.HOME_SPECIAL_ONE_PAGE_NUM;
							} else {
								pageSize = plateEntities.size() / CommonConfig.HOME_SPECIAL_ONE_PAGE_NUM + 1;
							}

							for (int i = 0; i < pageSize; i++) {
								List<PlateEntity> plateEntities2 = new ArrayList<>();
								if (i == pageSize - 1) {
									//最后一页,拿到所有剩余的
									plateEntities2.addAll(plateEntities.subList(i * CommonConfig.HOME_SPECIAL_ONE_PAGE_NUM, plateEntities.size()));
								} else {
									plateEntities2.addAll(plateEntities.subList(i * CommonConfig.HOME_SPECIAL_ONE_PAGE_NUM, (i + 1) * CommonConfig.HOME_SPECIAL_ONE_PAGE_NUM));
								}
								//添加到集合中,每一页的数据
								platePages.add(plateEntities2);
							}
							cbHomePlate.setPages(PlateHolder::new, platePages);
						}

						productsAdapter = new ProductsAdapter(productEntities, this);
						rrvHomeList.setAdapter(productsAdapter);
					} else {
						ToastUtil.shortShow(getActivity(), homeEntity.getResultMsg());
					}
				} else {
					ToastUtil.shortShow(getActivity(), getString(R.string.no_data_now));
				}
				break;
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		rrvHomeList.resetStatus();
		ToastUtil.shortShow(getActivity(), errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_select_language:
				LanguageAndAreaActivity.startLanguageActivity(getActivity(), this, CommonConstant.REQUEST_ACT_ONE);
				break;
			case R.id.fl_home_search:
				SearchActivity.startSearchActivity(getActivity());
				break;
			case R.id.iv_home_scan:
//				ScanActivity.startScanActivity(getActivity());
				if (UserCenter.isLogin(getActivity())) {
					ShopCarActivity.startShopCarActivity(getActivity());
				} else {
					startActivity(new Intent(getActivity(),LoginActivity.class));
				}
				break;
			case R.id.ll_item_home_plate:
				PlateEntity plateEntity = (PlateEntity) v.getTag(R.id.plate_click_tag);
				if (plateEntity != null) {
					switch (plateEntity.getType()) {
						case "3"://抽奖
							DrawListActivity.startDrawListActivity(getActivity(), plateEntity.getName());
							break;
						case "2"://商品列表
							ProductListActivity.startProductListActivity(getActivity(), CommonConstant.FROM_HOME_PRODUCT, plateEntity.getId(), plateEntity.getName(), "");
							break;
						case "1"://板块
							PlateListActivity.startPlateListActivity(getActivity(), plateEntity.getId(), plateEntity.getName());
							break;
					}
				}
				break;
			case R.id.ll_item_product_layout:
				ProductEntity productEntity = (ProductEntity) v.getTag();
				if (productEntity != null) {
					ProductDetailActivity.startProductDetailActivity(getActivity(), productEntity.getGoodsId());
				}
				break;
		}
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@Override
	public void onLoadMore() {
		loadData();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode && CommonConstant.REQUEST_ACT_ONE == requestCode) {
			loadData();
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

	private class PlateHolder implements Holder<List<PlateEntity>>, OnItemHeightGetListener {

		private GridView gvItemHomePlate;

		@Override
		public View createView(Context context) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_home_plate, null);
			gvItemHomePlate = (GridView) view.findViewById(R.id.gv_item_home_plate);
			return view;
		}

		@Override
		public void UpdateUI(Context context, int position, List<PlateEntity> data) {
			PlateAdapter plateAdapter = new PlateAdapter(getActivity(), data, HomeFragment.this);
			plateAdapter.setOnItemHeightGetListener(this);
			gvItemHomePlate.setAdapter(plateAdapter);
		}

		@Override
		public void onItemHeightGet(int itemHeight) {
			//因为专区的ConvenientBanner获取不到高度，所以这里通过先拿到item的高度后再动态设置高度
			if (plateEntities != null) {
				ViewGroup.LayoutParams layoutParams = cbHomePlate.getLayoutParams();
				int height;
				if (plateEntities.size() > 3) {
					height = itemHeight * (CommonConfig.HOME_SPECIAL_ONE_PAGE_NUM / gvItemHomePlate.getNumColumns());
				} else {
					height = itemHeight;
				}
				if (layoutParams.height != height) {
					layoutParams.height = height;
					cbHomePlate.setLayoutParams(layoutParams);
				}
			}
		}
	}
}
