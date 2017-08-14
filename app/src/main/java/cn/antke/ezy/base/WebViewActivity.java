package cn.antke.ezy.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.antke.ezy.R;
import cn.antke.ezy.common.CommonConstant;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;

/**
 * 公用WebView加载页
 */
public class WebViewActivity extends ToolBarActivity {

	private ProgressBar mHorizontalProgress;
	private WebView mWebView;
	private ImageView backBtn;
	private boolean mIsImmediateBack = false;
	private boolean mIsLeftBtnDisplay = true;
	private boolean mIsRightBtnDisplay = true;
	private int from;
	private String url;
	private String title;
	private Handler refreshProgressHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					if (msg.arg1 >= 100) {
						if (mHorizontalProgress != null) {
							mHorizontalProgress.setVisibility(View.GONE);
						}
					} else {
						if (mHorizontalProgress != null && msg.arg1 >= 0) {
							mHorizontalProgress.setVisibility(View.VISIBLE);
							mHorizontalProgress.setProgress(msg.arg1);
						}
					}
					break;
			}
		}
	};

	@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.act_webview);

		url = getIntent().getStringExtra(CommonConstant.EXTRA_URL);
		//url = "file:///android_asset/test.html";
		title = getIntent().getStringExtra(CommonConstant.EXTRA_TITLE);
		from = getIntent().getIntExtra(EXTRA_FROM, 0);
		if (title == null) {
			title = "";
		}
		setLeftTitle(title);

		mHorizontalProgress = (ProgressBar) findViewById(R.id.progress_horizontal);
		mWebView = (WebView) findViewById(R.id.webview);
		// 设置支持JavaScript
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(null, "vjia");
		webSettings.setSupportZoom(true);
//		webSettings.setDatabaseEnabled(true);
//		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setGeolocationEnabled(true);
//		webSettings.setGeolocationDatabasePath(dir);
		webSettings.setDomStorageEnabled(true);
		webSettings.setUseWideViewPort(true);//图片调整到适合WebView的大小
		webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小

		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend, Message resend) {
				resend.sendToTarget();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(view.getContext(), getString(R.string.custom_net_error), Toast.LENGTH_SHORT).show();
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (refreshProgressHandler != null) {
					if (refreshProgressHandler.hasMessages(0)) {
						refreshProgressHandler.removeMessages(0);
					}
					Message mMessage = refreshProgressHandler.obtainMessage(0, newProgress, 0, null);
					refreshProgressHandler.sendMessageDelayed(mMessage, 100);
				}
			}

			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});

		mWebView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		mWebView.loadUrl(url);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
		if (backBtn != null) {
			if (mIsLeftBtnDisplay) {
				backBtn.setVisibility(View.VISIBLE);
			} else {
				backBtn.setVisibility(View.GONE);
			}

			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.hideSoftInputFromWindow(backBtn.getApplicationWindowToken(), 0);

					if (mIsImmediateBack) {
						onBackPressed();
					} else {
						if (mWebView.canGoBack()) {
							mWebView.goBack();
						} else {
							onBackPressed();
						}
					}
				}
			});
		}

		final TextView closeBtn = null;
		if (closeBtn != null) {
			if (mIsRightBtnDisplay) {
				closeBtn.setVisibility(View.VISIBLE);
			} else {
				closeBtn.setVisibility(View.GONE);
			}
			closeBtn.setText(getString(R.string.close));
			closeBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.hideSoftInputFromWindow(closeBtn.getApplicationWindowToken(), 0);
					onBackPressed();
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				onBackPressed();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		mWebView.destroy();
		super.onDestroy();
	}
}
