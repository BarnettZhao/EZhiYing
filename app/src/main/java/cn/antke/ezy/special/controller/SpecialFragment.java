package cn.antke.ezy.special.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.RefreshRecyclerView;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseFragment;
import cn.antke.ezy.base.WebViewActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.home.adapter.ProductsAdapter;
import cn.antke.ezy.login.controller.LoginActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.BannerEntity;
import cn.antke.ezy.network.entities.HomeEntity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.person.controller.StoreApplyActivity;
import cn.antke.ezy.product.controller.ProductDetailActivity;
import cn.antke.ezy.product.controller.ProductListActivity;
import cn.antke.ezy.product.controller.StoreDetailActivity;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/4/27.
 * 易购专区
 */
public class SpecialFragment extends BaseFragment implements View.OnClickListener, RefreshRecyclerView.OnRefreshAndLoadMoreListener {

	@ViewInject(R.id.left_button)
	private View leftButton;
	@ViewInject(R.id.toolbar_title)
	private TextView toolbarTitle;
	@ViewInject(R.id.rigth_text)
	private TextView rigthText;
	@ViewInject(R.id.rrv_special_list)
	private RefreshRecyclerView rrvSpecialList;
	@ViewInject(R.id.cb_home_banner)
	private ConvenientBanner cbHomeBanner;
	@ViewInject(R.id.cb_home_plate)
	private ConvenientBanner cbHomePlate;
	@ViewInject(R.id.tv_home_subtitle)
	private TextView tvHomeSubtitle;
	@ViewInject(R.id.tv_special_more)
	private View tvSpecialMore;

	private View view;
	private View topView;

	private List<BannerEntity> bannerEntities;
	private ProductsAdapter productsAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.frag_special, null);
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
		leftButton.setVisibility(View.INVISIBLE);
		toolbarTitle.setText(getString(R.string.main_tab_special));
		toolbarTitle.setVisibility(View.VISIBLE);
		rigthText.setText(getString(R.string.store_apply));
		rigthText.setVisibility(View.VISIBLE);
		rigthText.setOnClickListener(this);
		tvSpecialMore.setOnClickListener(this);

		tvHomeSubtitle.setText(getString(R.string.product_list));
		tvHomeSubtitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		cbHomePlate.setVisibility(View.GONE);

		rrvSpecialList.addHeaderView(topView);
		rrvSpecialList.setHasFixedSize(true);
		rrvSpecialList.setMode(RefreshRecyclerView.Mode.PULL_FROM_START);
		rrvSpecialList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
		rrvSpecialList.setOnRefreshAndLoadMoreListener(this);
		rrvSpecialList.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				if (parent.getLayoutManager() instanceof GridLayoutManager) {
					GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
					int spanCount = gridLayoutManager.getSpanCount();//设置的列数
					int headerSize = rrvSpecialList.getHeaderSize();//头部数量
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
		rrvSpecialList.setCanAddMore(false);

		cbHomeBanner.setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_light})
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
				.startTurning(3000);
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
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData();
	}

	private void loadData() {
		requestHttpData(Constants.Urls.URL_POST_SPECIAL, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(UserCenter.getStoreId(getActivity()))) {
			rigthText.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void success(int requestCode, String data) {
		rrvSpecialList.resetStatus();
		switch (requestCode) {
			case CommonConstant.REQUEST_NET_ONE:{
				HomeEntity homeEntity = Parsers.getHomeEntity(data);
				if (homeEntity != null) {
					if (CommonConstant.REQUEST_NET_SUCCESS.equals(homeEntity.getResultCode())) {
						bannerEntities = homeEntity.getBannerEntities();
//						plateEntities = homeEntity.getPlateEntities();
						List<ProductDetailEntity> productEntities = homeEntity.getProductEntities();

						cbHomeBanner.setPages(new CBViewHolderCreator<ImageHolder>() {

							@Override
							public ImageHolder createHolder() {
								return new ImageHolder();
							}
						}, bannerEntities);

						productsAdapter = new ProductsAdapter(productEntities, this);
						rrvSpecialList.setAdapter(productsAdapter);
					} else {
						ToastUtil.shortShow(getActivity(), homeEntity.getResultMsg());
					}
				} else {
					ToastUtil.shortShow(getActivity(), getString(R.string.no_data_now));
				}
				break;
			}
			case CommonConstant.REQUEST_NET_TWO:{
				HomeEntity homeEntity = Parsers.getHomeEntity(data);
				if (homeEntity != null) {

				} else {
					ToastUtil.shortShow(getActivity(), getString(R.string.no_data_now));
				}
			}
			break;
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		rrvSpecialList.resetStatus();
		ToastUtil.shortShow(getActivity(), errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rigth_text:
				if (UserCenter.isLogin(getActivity())) {
					startActivity(new Intent(getActivity(), StoreApplyActivity.class));
				} else {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
				break;
			case R.id.ll_item_product_layout:
				ProductEntity productEntity = (ProductEntity) v.getTag();
				if (productEntity != null) {
					ProductDetailActivity.startProductDetailActivity(getActivity(), productEntity.getGoodsId());
				}
				break;
			case R.id.tv_special_more:
				ProductListActivity.startProductListActivity(getActivity(), CommonConstant.FROM_SPECIAL_PRODUCT, "", "商品列表", "");
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
