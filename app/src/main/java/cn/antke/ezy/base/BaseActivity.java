package cn.antke.ezy.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.interfaces.IActivityHelper;
import com.common.network.FProtocol;
import com.common.ui.FBaseActivity;
import com.common.utils.ToastUtil;

import java.util.IdentityHashMap;

import cn.antke.ezy.MainApplication;
import cn.antke.ezy.R;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.utils.ConfigUtils;
import cn.antke.ezy.utils.ExitManager;
import cn.jpush.android.api.JPushInterface;

/**
 * @author songxudong
 */
public class BaseActivity extends FBaseActivity implements IActivityHelper {

	public static final int REQUEST_UPDATE_VERSION_CODE = -3;
	public static final int REQUEST_PERMISSION_CODE = 0x1;

	protected View mLayoutLoading;
	protected ImageView mImgLoading;
	protected TextView mTxtCardEmpty;
	protected TextView mTxtLoadingEmpty;
	protected TextView mTxtLoadingRetry;
	protected ImageView mImgLoadingRetry;
	protected ImageView mImgLoadingEmpty;

	protected Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//建议在Base中写，解决在系统设置中修改了语言之后，切回应用语言会变成刚修改的语言的问题
		//两种处理方法，一种是在BaseActivity的onCreate方法的super.onCreate()方法之前设置语言
		//第二种方法是重写BaseActivity的getResources()方法，返回新的Resource
		MainApplication.configLanguage(this);
		super.onCreate(savedInstanceState);
		ExitManager.instance.addActivity(this);
		res = getResources();
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void requestHttpData(String path, int requestCode) {
		if (path.contains("?")) {
			path += ("&user_id=" + UserCenter.getUserId(this) + "&site_id=" + UserCenter.getSiteId(this) + "&language=" + ConfigUtils.getPreArea(this));
		} else {
			path += ("?user_id=" + UserCenter.getUserId(this) + "&site_id=" + UserCenter.getSiteId(this) + "&language=" + ConfigUtils.getPreArea(this));
		}
		super.requestHttpData(path, requestCode);
	}

	@Override
	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters) {
		if (postParameters == null) {
			postParameters = new IdentityHashMap<>();
		}
		postParameters.put("user_id", UserCenter.getUserId(this));
		postParameters.put("site_id", UserCenter.getSiteId(this));
		postParameters.put("language", ConfigUtils.getPreLanguage(this));
		super.requestHttpData(path, requestCode, method, postParameters);
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		Entity entity = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
			parseData(requestCode, data);
		} else {
			closeProgressDialog();
			ToastUtil.shortShow(this, entity.getResultMsg());
		}
	}

	/**
	 * 请求成功后实际处理数据的方法
	 */
	protected void parseData(int requestCode, String data) {
		closeProgressDialog();
		setLoadingStatus(LoadingStatus.GONE);
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		if (REQUEST_UPDATE_VERSION_CODE == requestCode) {
			closeProgressDialog();
		}
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	public void onDestroy() {
		ExitManager.instance.remove(this);
		super.onDestroy();
	}

	protected void initLoadingView(View.OnClickListener listener) {
		mLayoutLoading = findViewById(R.id.loading_layout);
		mImgLoading = (ImageView) findViewById(R.id.loading_img_anim);
		mTxtLoadingEmpty = (TextView) findViewById(R.id.loading_txt_empty);
		mTxtLoadingRetry = (TextView) findViewById(R.id.loading_txt_retry);
		mImgLoadingRetry = (ImageView) findViewById(R.id.loading_img_refresh);
		mImgLoadingEmpty = (ImageView) findViewById(R.id.loading_img_empty);
		mTxtCardEmpty = (TextView) findViewById(R.id.loading_btn_card_empty);

		Animation operatingAnim = AnimationUtils.loadAnimation(this, com.common.R.anim.load_operate);
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		operatingAnim.setInterpolator(linearInterpolator);
		if (operatingAnim != null) {
			mImgLoading.startAnimation(operatingAnim);
		}

		if (mTxtCardEmpty != null) {
			mTxtCardEmpty.setOnClickListener(listener);
			mTxtCardEmpty.setClickable(false);
		}
		if (mLayoutLoading != null) {
			mLayoutLoading.setOnClickListener(listener);
			mLayoutLoading.setClickable(false);
		}
	}

	protected void setLoadingStatus(LoadingStatus status) {
		if (mLayoutLoading == null || mImgLoading == null || mImgLoadingEmpty == null
				|| mImgLoadingRetry == null || mTxtLoadingEmpty == null || mTxtLoadingRetry == null) {
			return;
		}
		switch (status) {
			case LOADING: {
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.VISIBLE);
				mImgLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				mTxtCardEmpty.setVisibility(View.GONE);
				mTxtCardEmpty.setClickable(false);
				break;
			}
			case EMPTY: {
				mTxtCardEmpty.setClickable(false);
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoading.clearAnimation();
				mImgLoadingEmpty.setVisibility(View.VISIBLE);
				mTxtLoadingEmpty.setVisibility(View.VISIBLE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				mTxtCardEmpty.setVisibility(View.GONE);
				break;
			}
			case RETRY: {
				mTxtCardEmpty.setClickable(false);
				mLayoutLoading.setClickable(true);
				mLayoutLoading.setVisibility(View.VISIBLE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoading.clearAnimation();
				mImgLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.VISIBLE);
				mTxtLoadingRetry.setVisibility(View.VISIBLE);
				mTxtCardEmpty.setVisibility(View.GONE);
				break;
			}
			case GONE: {
				mTxtCardEmpty.setClickable(false);
				mLayoutLoading.setClickable(false);
				mLayoutLoading.setVisibility(View.GONE);
				mImgLoading.setVisibility(View.GONE);
				mImgLoading.clearAnimation();
				mTxtLoadingEmpty.setVisibility(View.GONE);
				mTxtLoadingRetry.setVisibility(View.GONE);
				mImgLoadingEmpty.setVisibility(View.GONE);
				mImgLoadingRetry.setVisibility(View.GONE);
				mTxtCardEmpty.setVisibility(View.GONE);
				break;
			}
		}
	}

	public enum LoadingStatus {
		LOADING,
		EMPTY,
		RETRY,
		GONE
	}
}
