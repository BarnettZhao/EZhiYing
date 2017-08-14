package com.common.ui;

import android.os.Bundle;
import android.view.View;

import com.common.interfaces.IActivityHelper;
import com.common.utils.AnalysisUtil;
import com.common.utils.LogUtil;
import com.common.view.FProgressDialog;

import java.io.File;


/**
 * @author songxudong
 */

public class FBaseActivity extends BaseThreadActivity implements IActivityHelper {

    private static final String BASETAG = FBaseActivity.class.getSimpleName();

    protected FProgressDialog progressDialog;// 加载对话框
    public boolean isHasFragment = false;
    private IActivityHelper activityHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        activityHelper = new ActivityHelperImp(this);
    }

    /**
     * 关闭提示框
     */
    public void closeProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    /**
     * 显示提示框
     */
    public void showProgressDialog() {
        this.showProgressDialog(true);
    }

    /**
     * 显示提示框
     */
    public void showProgressDialog(boolean cancelable) {
        if (!this.isFinishing()) {
            if (this.progressDialog == null) {
                this.progressDialog = new FProgressDialog(this);
            }
            progressDialog.setCancelable(true);
            this.progressDialog.show();
        }
    }

    public void finish() {
        super.finish();
    }

    public void defaultFinish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onResume() invoked!!");
        super.onResume();
        if (!isHasFragment) {
            AnalysisUtil.onPageStart(getClass().getSimpleName());
        }
        AnalysisUtil.onResume(this);
    }

    @Override
    protected void onPause() {
        LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onPause() invoked!!");
        super.onPause();
        if (!isHasFragment) {
            AnalysisUtil.onPageEnd(getClass().getSimpleName());
        }
        AnalysisUtil.onPause(this);
    }

    @Override
    protected void onStop() {
        LogUtil.d(BASETAG, this.getClass().getSimpleName()
                + " onStop() invoked!!");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
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
