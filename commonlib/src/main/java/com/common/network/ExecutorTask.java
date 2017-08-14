package com.common.network;

import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.common.interfaces.IResponseCallBack;
import com.common.utils.CharToUrlTools;
import com.common.utils.NetWorkUtil;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by songxudong
 * on 2015/4/28.
 */
public class ExecutorTask implements Runnable {

	private IResponseCallBack handler;
	private String path;
	private int requestCode;
	private final AtomicBoolean mCancelled = new AtomicBoolean();
	private static final String LOG_TAG = "ExecutorTask";

	/**
	 * 直接从网络取数据不需要本地存储 DATA_FROM_NET_NO_CACHE
	 * <p/>
	 * 直接从网络取数据需要本地存储 DATA_FROM_NET
	 * <p/>
	 * 直接从本地存储拿数据 DATA_FROM_CACHE
	 * <p/>
	 * 先从本地存储取数据显示出来 再去网络取数据更新界面并本地存储 DATA_UPDATE_CACHE
	 */
	private FProtocol.NetDataProtocol.DataMode dataAccessMode = FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE;
	private IResponseJudger judger;
	/**
	 * 为了区别不能的请求，当页面有多个请求时需要改变此变量区别
	 */
	Context mContext;// 由于访问sp，设置dataAccessMode时会被要求传入
	private FProtocol.HttpMethod method = FProtocol.HttpMethod.GET;
	private IdentityHashMap<String, String> postParameters = null;
	private String cachePath = null;

	private boolean isCache = false;
	private String streamName;
	private File file;

	public ExecutorTask(Context context, final IResponseCallBack handler, final String path, final int requestCode, final FProtocol.HttpMethod method, final IdentityHashMap<String, String> postParameters) {
		this.handler = handler;
		this.path = CharToUrlTools.toUtf8String(path);
		this.requestCode = requestCode;
		this.mContext = context;
		this.method = method;
		this.postParameters = postParameters;
		if (method == FProtocol.HttpMethod.GET) {
			cachePath = this.path;
		} else {
			if (postParameters != null) {
				cachePath = this.path + this.postParameters.toString();
			} else {
				cachePath = this.path;
			}
		}
	}

	public ExecutorTask(Context context, IResponseCallBack handler, String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters, String streamName, File file) {
		this.handler = handler;
		this.path = CharToUrlTools.toUtf8String(path);
		this.requestCode = requestCode;
		this.mContext = context;
		this.method = method;
		this.postParameters = postParameters;
		this.streamName = streamName;
		this.file = file;
		if (method == FProtocol.HttpMethod.GET) {
			cachePath = this.path;
		} else {
			String allFileName = "";
			if (postParameters != null) {
				if (this.file != null) {
					cachePath = this.path + this.postParameters.toString() + allFileName;
				} else {
					cachePath = this.path + this.postParameters.toString();
				}
			} else {
				if (this.file != null) {
					cachePath = this.path + allFileName;
				} else {
					cachePath = this.path;
				}
			}
		}
	}

	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		String json;
		try {
			if (isCancelled()) {
				handler = null;
				return;
			}
			if (!NetWorkUtil.isConnect(mContext) && dataAccessMode == FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE) {
				//断网处理
				handler.resultDataMistake(requestCode, FProtocol.NetDataProtocol.ResponseStatus.LOAD_NET_DISCONNENT, "当前网络不可用，请检查网络");
				return;
			}
			switch (dataAccessMode) {
				// 访问网络，不做本地存储
				case DATA_FROM_NET_NO_CACHE:
					json = DataUtil.getJsonFromServer(path, mContext, method, postParameters, streamName, file);
					isCache = false;
					break;
				// 仅访问本地存储
				case DATA_FROM_CACHE:
					// cache的数据永远正确
					json = DataUtil.getJsonFromCache(cachePath, mContext);
					break;
				// 先访问本地存储返回数据展示,再访问网络更新数据并刷新UI,
				case DATA_UPDATE_CACHE:
					json = DataUtil.getJsonFromCache(cachePath, mContext);
					if (!TextUtils.isEmpty(json)) {
						handler.resultDataSuccess(requestCode, json);
					}
				case DATA_FROM_NET:
					json = DataUtil.getJsonFromServer(path, mContext, method, postParameters, streamName, file);
					isCache = true;
					break;
				// 默认访问网络，正常情况不会执行default
				default:
					json = DataUtil.getJsonFromServer(path, mContext, method, postParameters, streamName, file);
					isCache = false;
					break;
			}
			if (!TextUtils.isEmpty(json)) {
				// 返回0再缓存
				//JSONObject responseHeader = new JSONObject(json);
				//if ("0".equals(responseHeader.getString("code"))) {
				if (judger != null && !judger.judge(json)) {
					handler.resultDataMistake(requestCode, FProtocol.NetDataProtocol.ResponseStatus.LOAD_MISTAKE, json);
					handler = null;
					return;
				}
				if (isCache) {
					DataUtil.cacheJson(cachePath, json, mContext);
				}
				handler.resultDataSuccess(requestCode, json);
				handler = null;
				//}
				// 如果没返回0也要返回 为了提取错误消息
			} else {
				handler.resultDataMistake(requestCode, FProtocol.NetDataProtocol.ResponseStatus.LOAD_MISTAKE, "网络异常，请稍后重试");
				handler = null;
			}
		} catch (Exception e) {
			handler.resultDataMistake(requestCode, FProtocol.NetDataProtocol.ResponseStatus.LOAD_EXCEPTION, e.toString());
			handler = null;
		}
	}

	public ExecutorTask setDataAccessMode(FProtocol.NetDataProtocol.DataMode dataAccessMode) {
		this.dataAccessMode = dataAccessMode;
		return this;
	}

	public ExecutorTask setJudger(IResponseJudger judger) {
		this.judger = judger;
		return this;
	}

	public final boolean isCancelled() {
		return mCancelled.get();
	}

	public final void cancel() {
		mCancelled.set(true);
	}

	public void execute() {
		CoreExecutorService.THREAD_POOL_EXECUTOR.submit(this);
	}
}
