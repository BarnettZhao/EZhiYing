package com.common.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * 友盟统计工具类
 *
 * @author songxudong
 */
public class AnalysisUtil {

	public static boolean OPEN = true;

	public static void init(Context context) {
		if (OPEN) {
			//普通统计场景
//			MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
			//禁止默认的页面统计方式
//			MobclickAgent.openActivityDurationTrack(false);
			//统计日志是否加密
//			MobclickAgent.enableEncrypt(true);
		}
	}

	// 统计时长
	public static void onResume(Context context) {
		if (OPEN) {
//			MobclickAgent.onResume(context);
		}
	}

	// 统计时长
	public static void onPause(Context context) {
		if (OPEN) {
//			MobclickAgent.onPause(context);
		}
	}

	// 统计页面
	public static void onPageStart(String pageName) {
		if (OPEN) {
//			MobclickAgent.onPageStart(pageName);
		}
	}

	// 统计页面
	public static void onPageEnd(String pageName) {
		if (OPEN) {
//			MobclickAgent.onPageEnd(pageName);
		}
	}

	// 统计发生次数
	public static void onEvent(Context context, String eventId) {
		if (OPEN) {
//			MobclickAgent.onEvent(context, eventId);
		}
	}

	/**
	 * 统计点击行为各属性被触发的次数 HashMap<String,String> map = new
	 * HashMap<String,String>(); map.put("type","book");
	 * map.put("quantity","3"); MobclickAgent.onEvent(mContext, "purchase",
	 * map);
	 *
	 * @param context context
	 * @param eventId 事件id
	 * @param map     行为
	 */
	public static void onEvent(Context context, String eventId, HashMap<String, String> map) {
		if (OPEN) {
//			MobclickAgent.onEvent(context, eventId, map);
		}
	}

	/**
	 * @param context context
	 * @param id      为事件ID
	 * @param m       为当前事件的属性和取值
	 * @param du      为当前事件的数值
	 */
	public static void onEventValue(Context context, String id, Map<String, String> m, int du) {
		if (OPEN) {
//			MobclickAgent.onEventValue(context, id, m, du);
		}
	}

	/**
	 * @param auto 禁止默认的页面统计方式，false将不会再自动统计Activity
	 */
	public static void openActivityDurationTrack(boolean auto) {
		if (OPEN) {
//			MobclickAgent.openActivityDurationTrack(auto);
		}
	}

	/**
	 *
	 * @param context
	 * @param hitParameters
	 *            例 HashMap<String, String> hitParameters = new HashMap<String,
	 *            String>(); hitParameters.put(Fields.EVENT_CATEGORY, "xxx");
	 *            hitParameters.put(Fields.EVENT_ACTION, "xxx");
	 *            hitParameters.put(Fields.EVENT_LABEL, "xxx");
	 *            hitParameters.put(Fields.customDimension(1), "xxx");
	 *            hitParameters.put(Fields.SCREEN_NAME, null);
	 */
	/*public static void sendEventToGoogle(Context context,
	        Map<String, String> hitParameters)
	{
		if (OPEN)
		{
			Tracker tracker = GoogleAnalytics.getInstance(context).getTracker(
			        context.getString(R.string.ga_trackingId));
			tracker.send(hitParameters);

		}
	}*/

	/**
	 * Google事件统计
	 *
	 * @param context 上下文对象
	 * @param screenName 屏幕名称
	 * @param dimensions 自定义维度数据{1：page_type，2：page_level}
	 * @param category 事件信息
	 * @param action 事件功能
	 * @param label 事件标签
	 */
	/*public static void sendEventToGoogle(Context context, String screenName,
	        String[] dimensions, String category, String action, String label)
	{
		Map<String, String> params = new HashMap<String, String>();
		if (!TextUtils.isEmpty(screenName))
		{
			params.put(Fields.HIT_TYPE, "appview");
			params.put(Fields.SCREEN_NAME, screenName);
		}

		if (dimensions != null)
		{
			for (int i = 0; i < dimensions.length; i++)
			{
				if (!TextUtils.isEmpty(dimensions[i]))
				{
					params.put(Fields.customDimension(i + 1), dimensions[i]);
				}
			}
		}

		if (!TextUtils.isEmpty(category))
		{
			params.put(Fields.EVENT_CATEGORY, category);
		}

		if (!TextUtils.isEmpty(action))
		{
			params.put(Fields.EVENT_ACTION, action);
		}

		if (!TextUtils.isEmpty(label))
		{
			params.put(Fields.EVENT_LABEL, label);
		}

		sendEventToGoogle(context, params);
	}*/

	/*
	 * private static void sendPageToGoogle(Context context, String view) {
	 * 
	 * if (OPEN) { Tracker tracker =
	 * GoogleAnalytics.getInstance(context).getTracker
	 * (context.getString(R.string.ga_trackingId));
	 * tracker.set(Fields.SCREEN_NAME, view);
	 * tracker.send(MapBuilder.createAppView().build()); } }
	 */
}
