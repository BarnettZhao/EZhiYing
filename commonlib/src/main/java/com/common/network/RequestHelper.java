package com.common.network;

import android.content.Context;

import com.common.interfaces.IRequestData;
import com.common.interfaces.IResponseCallBack;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by songxudong
 * on 2015/6/19.
 */
public class RequestHelper implements IRequestData {
	private Context mContext;
	private IResponseCallBack responseDataCallBack;

	public RequestHelper(Context context, IResponseCallBack callBack) {
		mContext = context.getApplicationContext();
		responseDataCallBack = callBack;
	}
// 访问网络数据

	/**
	 * @param path        请求url
	 * @param requestCode 请求码
	 */
	public void requestHttpData(String path, int requestCode) {
		requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE);
	}

	public void requestHttpData(String path, int requestCode, IResponseJudger judger) {
		requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, judger);
	}

	/**
	 * @param path        请求url
	 * @param requestCode 请求码
	 */
	public void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode) {
		requestHttpData(path, requestCode, dataAccessMode, FProtocol.HttpMethod.GET, null);
	}

	public void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode, IResponseJudger judger) {
		requestHttpData(path, requestCode, dataAccessMode, FProtocol.HttpMethod.GET, null, judger);
	}

	/**
	 * @param path           请求url
	 * @param requestCode    请求码
	 * @param method         请求方式
	 * @param postParameters post方式参数
	 */

	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters) {
		requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, method, postParameters);
	}

	public void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters, IResponseJudger judger) {
		requestHttpData(path, requestCode, FProtocol.NetDataProtocol.DataMode.DATA_FROM_NET_NO_CACHE, method, postParameters, judger);
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
		requestHttpData(path, requestCode, dataAccessMode, method, postParameters, null);
	}

	public void requestHttpData(String path, int requestCode,
	                            FProtocol.NetDataProtocol.DataMode dataAccessMode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters, IResponseJudger judger) {
		ExecutorTask jsonTask = new ExecutorTask(mContext,
				responseDataCallBack, path, requestCode, method, postParameters);
		jsonTask.setDataAccessMode(dataAccessMode).setJudger(judger);
		jsonTask.execute();
	}

	public void requestHttpData(String path, int requestCode,
	                            FProtocol.NetDataProtocol.DataMode dataAccessMode, FProtocol.HttpMethod method,
	                            IdentityHashMap<String, String> postParameters, String streamName, File file) {
		ExecutorTask jsonTask = new ExecutorTask(mContext, responseDataCallBack, path, requestCode, method, postParameters, streamName, file);
		jsonTask.setDataAccessMode(dataAccessMode).setJudger(null);
		jsonTask.execute();
	}
//
//	public void requestHttpData(String path, int requestCode,
//	                            FProtocol.NetDataProtocol.DataMode dataAccessMode, FProtocol.HttpMethod method,
//	                            IdentityHashMap<String, String> postParameters, List<String> streamNames, File ... files) {
//		ExecutorTask jsonTask = new ExecutorTask(mContext, responseDataCallBack, path, requestCode, method, postParameters, streamNames, files);
//		jsonTask.setDataAccessMode(dataAccessMode).setJudger(null);
//		jsonTask.execute();
//	}
}
