package com.common.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.common.interfaces.IRequestData;
import com.common.interfaces.IResponseCallBack;
import com.common.network.FProtocol;
import com.common.network.IResponseJudger;
import com.common.network.RequestHelper;
import com.common.utils.LogUtil;

import java.io.File;
import java.util.IdentityHashMap;


/**
 * @author songxudong 使用线程加handler方式处理网络请求
 * @hide
 */
public class BaseThreadActivity extends AppCompatActivity implements IRequestData, IResponseCallBack {

	private static final String BASETAG = BaseThreadActivity.class
			.getSimpleName();

	private RequestHelper requestHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onCreate() invoked!!");
		super.onCreate(savedInstanceState);
		requestHelper = new RequestHelper(this, this);
	}

	@Override
	public void onDestroy() {
		LogUtil.d(BASETAG, this.getClass().getSimpleName() + " onDestroy() invoked!!");
		super.onDestroy();
	}


	// 访问网络数据

	/**
	 * @param path        请求url
	 * @param requestCode 请求码
	 */
	public void requestHttpData(String path, int requestCode) {
		requestHelper.requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE);
	}

	public void requestHttpData(String path, int requestCode, IResponseJudger judger) {
		requestHelper.requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, judger);
	}

	/**
	 * @param path        请求url
	 * @param requestCode 请求码
	 */
	public void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode) {
		requestHelper.requestHttpData(path, requestCode, dataAccessMode, FProtocol.HttpMethod.GET, null);
	}

	public void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode, IResponseJudger judger) {
		requestHelper.requestHttpData(path, requestCode, dataAccessMode, FProtocol.HttpMethod.GET, null, judger);
	}

	/**
	 * @param path           请求url
	 * @param requestCode    请求码
	 * @param method         请求方式
	 * @param postParameters post方式参数
	 */

	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters) {
		requestHelper.requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, method, postParameters);
	}

	/**
	 * 新增对流的传输
	 */
	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters, String streamName, File file) {
		requestHelper.requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, method, postParameters, streamName, file);
	}

//	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters, List<String> streamNames, File ... files) {
//		requestHelper.requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, method, postParameters, streamNames, files);
//	}

	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters, IResponseJudger judger) {
		requestHelper.requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, method, postParameters, judger);
	}

	/**
	 * @param path           请求url
	 * @param requestCode    请求码
	 * @param dataAccessMode 缓存方式 默认只从网络取数据不做缓存
	 * @param method         请求方式
	 * @param postParameters post方式参数
	 */
	public void requestHttpData(String path, int requestCode,
	                            FProtocol.NetDataProtocol.DataMode dataAccessMode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters) {
		requestHelper.requestHttpData(path, requestCode, dataAccessMode, method, postParameters, null);
	}


	public void requestHttpData(String path, int requestCode,
	                            FProtocol.NetDataProtocol.DataMode dataAccessMode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters, IResponseJudger judger) {
		requestHelper.requestHttpData(path, requestCode, dataAccessMode, method, postParameters, judger);
	}


	public void resultDataSuccess(final int requestCode, final String data) {
		LogUtil.i(BASETAG, "resultDataSuccess = " + requestCode);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					success(requestCode, data);
				} catch (Exception e) {
					LogUtil.e(BASETAG, "error method resultDataSuccess = " + requestCode + " : error " + e.toString());
				}

			}
		});
	}

	public void resultDataMistake(final int requestCode, final FProtocol.NetDataProtocol.ResponseStatus status, final String errorMessage) {
		LogUtil.e(BASETAG, requestCode + "resultDataMistake = " + errorMessage);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					mistake(requestCode, status, errorMessage);
				} catch (Exception e) {
					LogUtil.e(BASETAG, "error method resultDataMistake = " + requestCode + " : error " + e.toString());
				}
			}
		});
	}

	public void success(int requestCode, String data) {
	}

	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
	}
}
