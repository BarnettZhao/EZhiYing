package com.common.network;

import android.content.Context;
import android.text.TextUtils;

import com.common.network.dbcache.SimpleCache;
import com.common.utils.StringUtil;

import org.json.JSONException;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.List;


/**
 * @author songxudong
 *         json处理工具类 仅缓存网络数据
 */
public class DataUtil {
	private static final String TAG = DataUtil.class.getSimpleName();

	/**
	 *
	 * @param url
	 * @param isCache 如果为真会从服务器取数据并且进行本地存储，为假时没有本地存储
	 * @param context
	 * @return
	 */
	/*public static String getJsonFromServer(String url, boolean isCache, Context context) throws JSONException {
		return getJsonFromServer(url, isCache, context, Constants.httpMethod.GET, null);
	}*/

	/**
	 * @param url
	 * @param context
	 * @param method         请求方式
	 * @param postParameters post方式参数
	 * @return
	 * @throws JSONException
	 */
	public static String getJsonFromServer(String url, Context context, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters,  String streamName, File file) throws JSONException {
		// 按照约定当state == 200时说明数据正确

		String json;
		if (method == FProtocol.HttpMethod.POST) {
			if (!StringUtil.isEmpty(streamName) && file.exists() && file.isFile()) {
				json = HttpUtil.httpPost(context, url, postParameters, streamName, file);
			} else {
				json = HttpUtil.httpPost(context, url, postParameters);
			}
		} else if (method == FProtocol.HttpMethod.PUT) {
			json = HttpUtil.httpPut(context, url, postParameters);
		} else if (method == FProtocol.HttpMethod.DELETE) {
			json = HttpUtil.httpDelete(context, url, postParameters);
		} else {
			String etag = "";
			if (postParameters != null) {
				etag = postParameters.get("Etag");
			}
			if (TextUtils.isEmpty(etag)) {
				json = HttpUtil.httpGet(context, url);
			} else {
				json = HttpUtil.httpGet(url, LastModified.getLastModified(context, etag), context);
			}
		}
		return json;
	}

	/**
	 * @param url
	 * @param context
	 * @return
	 */
	public static String getJsonFromCache(String url, Context context) {
		return SimpleCache.getCache(context).get(url.hashCode() + "");
	}

	/**
	 * 本地存储json到shareperference
	 *
	 * @param url
	 * @param json
	 * @param context
	 */
	public static void cacheJson(String url, String json, Context context) {
		SimpleCache.getCache(context).put(url.hashCode() + "", json);
	}
}
