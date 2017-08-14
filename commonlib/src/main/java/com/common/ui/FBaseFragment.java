package com.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.common.interfaces.IActivityHelper;
import com.common.utils.AnalysisUtil;
import com.common.utils.LogUtil;

import java.io.File;

/**
 * @author songxudong
 */
public class FBaseFragment extends BaseThreadFragment implements IActivityHelper {

	private static final String BASETAG = FBaseFragment.class.getSimpleName();

	private IActivityHelper activityHelper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityHelper = new ActivityHelperImp(getActivity());
	}

	/**
	 * 关闭提示框
	 */
	public void closeProgressDialog() {
		if (getActivity() != null && !getActivity().isFinishing()) {
			if (getActivity() instanceof FBaseActivity) {
				((FBaseActivity) getActivity()).closeProgressDialog();
			}
		}
	}

	/**
	 * 显示提示框
	 */
	public void showProgressDialog() {
		LogUtil.i(BASETAG, "getActivity().isFinishing()" + getActivity().isFinishing());
		this.showProgressDialog(true);

	}

	/**
	 * 显示提示框
	 */
	public void showProgressDialog(boolean cancelable) {
		LogUtil.i(BASETAG, "getActivity().isFinishing()" + getActivity().isFinishing());
		if (!getActivity().isFinishing()) {
			if (getActivity() instanceof FBaseActivity) {
				((FBaseActivity) getActivity()).showProgressDialog(cancelable);
			}
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onActivityCreated() invoked!!");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onAttach() invoked!!");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onDestroyView() invoked!!");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onDetach() invoked!!");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onStart() invoked!!");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onResume() invoked!!");
		AnalysisUtil.onPageStart(getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onPause() invoked!!");
		AnalysisUtil.onPageEnd(getClass().getSimpleName());
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onStop() invoked!!");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onDestroy() invoked!!");
		super.onDestroy();
	}

	@Override
	public void goHome() {
		activityHelper.goHome();
	}

	@Override
	public void tell(String telePhoneNum) {
		activityHelper.tell(telePhoneNum);
	}

	@Override
	public void sendSms(String telePhoneNum, String msg) {
		activityHelper.sendSms(telePhoneNum, msg);
	}

	@Override
	public void installApp(File file) {
		activityHelper.installApp(file);
	}

	public void recommandToYourFriend(String url, String shareTitle) {
		activityHelper.recommandToYourFriend(url, shareTitle);
	}

	public void hideKeyboard(View view) {
		activityHelper.hideKeyboard(view);
	}

	@Override
	public void killApp() {
		activityHelper.killApp();
	}

}
