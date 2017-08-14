package cn.antke.ezy.product.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.controller.LoginActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/17.
 * 商品详情
 */
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

	@ViewInject(R.id.iv_product_detail_back)
	private View ivProductDetailBack;
	@ViewInject(R.id.iv_product_detail_car)
	private View ivProductDetailCar;
	@ViewInject(R.id.cb_product_detail_banner)
	private ConvenientBanner cbProductDetailBanner;
	@ViewInject(R.id.tv_product_detail_name)
	private TextView tvProductDetailName;
	@ViewInject(R.id.tv_product_detail_desc)
	private TextView tvProductDetailDesc;
	@ViewInject(R.id.tv_product_detail_price)
	private TextView tvProductDetailPrice;
	@ViewInject(R.id.tv_product_detail_postage)
	private TextView tvProductDetailPostage;
	@ViewInject(R.id.tv_product_detail_num)
	private TextView tvProductDetailNum;
	@ViewInject(R.id.tv_product_detail_city)
	private TextView tvProductDetailCity;
	@ViewInject(R.id.wv_product_detail_content)
	private WebView wvProductDetailContent;
	@ViewInject(R.id.iv_product_store)
	private View ivProductStore;
	@ViewInject(R.id.tv_product_add_car)
	private View tvProductAddCar;
	@ViewInject(R.id.tv_product_buy)
	private View tvProductBuy;

	private String id;
	private String storeId;
	private ProductDetailEntity productDetail;

	public static void startProductDetailActivity(Context context, String id) {
		Intent intent = new Intent(context, ProductDetailActivity.class);
		intent.putExtra(CommonConstant.EXTRA_ID, id);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_product_detail);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		id = getIntent().getStringExtra(CommonConstant.EXTRA_ID);

		cbProductDetailBanner.setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_light})
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

		ivProductDetailBack.setOnClickListener(this);
		ivProductDetailCar.setOnClickListener(this);
		ivProductStore.setOnClickListener(this);
		tvProductAddCar.setOnClickListener(this);
		tvProductBuy.setOnClickListener(this);
	}

	private void loadData() {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("goods_id", id);
		requestHttpData(Constants.Urls.URL_POST_PRODUCT_DETAIL, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:{
					productDetail = Parsers.getProductDetail(data);
					storeId = productDetail.getStoreId();

					tvProductDetailName.setText(productDetail.getGoodsName());
					tvProductDetailDesc.setText(productDetail.getGoodsBrief());

					if (!StringUtil.isEmpty(storeId)) {
						ivProductStore.setVisibility(View.VISIBLE);
					}

					tvProductDetailPrice.setText(InputUtil.formatPrice(productDetail.getSellingIntegral(), productDetail.getSellingPrice()));
					tvProductDetailPostage.setText("邮费：" + InputUtil.formatLogistics(productDetail.getLogisticsIntegral(), productDetail.getLogisticsCost()));
					tvProductDetailNum.setText("销量：" + StringUtil.parseInt(productDetail.getSalesCount(), 0));
					tvProductDetailCity.setText(productDetail.getAddress());
					wvProductDetailContent.loadUrl(productDetail.getContent());

					List<String> bannerEntities = productDetail.getPicList();
					cbProductDetailBanner.setPages(new CBViewHolderCreator<ImageHolder>() {

						@Override
						public ImageHolder createHolder() {
							return new ImageHolder();
						}
					}, bannerEntities);
					break;
				}
				case CommonConstant.REQUEST_NET_TWO:{
					ToastUtil.shortShow(this, getString(R.string.product_had_add_car));
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
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_product_detail_back:
				finish();
				break;
			case R.id.iv_product_detail_car:
				if (UserCenter.isLogin(this)) {
					ShopCarActivity.startShopCarActivity(this);
				} else {
					startActivity(new Intent(this, LoginActivity.class));
				}
				break;
			case R.id.iv_product_store:
				StoreDetailActivity.startStoreDetailActivity(this, storeId);
				break;
			case R.id.tv_product_add_car:
				if (UserCenter.isLogin(this)) {
					if (productDetail != null) {
						NormsActivity.startNormsActivity(this, CommonConstant.REQUEST_ACT_ONE, CommonConstant.FROM_PRODUCT_DETAIL, productDetail.getAttributeList(), -1, -1);
					}
				} else {
					startActivity(new Intent(this, LoginActivity.class));
				}
				break;
			case R.id.tv_product_buy:
				if (UserCenter.isLogin(this)) {
					if (productDetail != null) {
						NormsActivity.startNormsActivity(this, CommonConstant.REQUEST_ACT_TWO, CommonConstant.FROM_PRODUCT_DETAIL, productDetail.getAttributeList(), -1, -1);
					}
				} else {
					startActivity(new Intent(this, LoginActivity.class));
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			String count = data.getStringExtra(CommonConstant.EXTRA_NUM);
			String attrId = data.getStringExtra(CommonConstant.EXTRA_CODE);
			switch (requestCode) {
				case CommonConstant.REQUEST_ACT_ONE:{
					showProgressDialog();
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put("goods_id", productDetail.getGoodsId());
					params.put("attr_relationids", attrId);
					params.put("goods_count", count);
					params.put("store_id", productDetail.getStoreId());
					requestHttpData(Constants.Urls.URL_POST_ADD_SHOP_CAR, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
					break;
				}
				case CommonConstant.REQUEST_ACT_TWO:{
					ConfirmOrderActivity.startConfirmOrderActivity(this, CommonConstant.FROM_PRODUCT_DETAIL, id, productDetail.getStoreId(), attrId, count);
					break;
				}
			}
		}
	}

	private class ImageHolder implements Holder<String> {

		private ImageView sdvBannerPic;

		@Override
		public View createView(Context context) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
			sdvBannerPic = (ImageView) view.findViewById(R.id.sdv_banner_pic);
			return view;
		}

		@Override
		public void UpdateUI(Context context, int position, String data) {
			ImageUtils.setSmallImg(sdvBannerPic, data);
		}
	}
}
