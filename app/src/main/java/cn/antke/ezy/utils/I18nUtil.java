package cn.antke.ezy.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by liuzhichao on 2017/5/19.
 * 处理国际化相关问题
 * 切换语言需要重启APP，首先需要在Application中初始化语言，其次需要在Activity中
 * 处理应用在后台时切换语言，会导致切换回前台时语言变为刚修改的语言的问题
 */
public class I18nUtil {

	/**
	 * 切换到指定语言
	 * @param locale 要切换的语言
	 * new Locale("zh", "CN") 简体中文
	 * new Locale("zh", "HK") 繁体中文-香港
	 * new Locale("ko") 韩文
	 * new Locale("ru") 俄语
	 * new Locale("ms", "MY") 马来语-马来西亚
	 */
	public static void setLanguage(Context context, Locale locale) {
		Resources resources = context.getResources();
		Configuration configuration = resources.getConfiguration();
		configuration.setLocale(locale);
		resources.updateConfiguration(configuration, resources.getDisplayMetrics());
	}

	/**
	 * 重置语言，使应用语言跟随系统
	 */
	public static void resetLanguage(Context context) {
		Resources resources = context.getResources();
		Configuration configuration = resources.getConfiguration();
		configuration.setLocale(Locale.getDefault());
		resources.updateConfiguration(configuration, resources.getDisplayMetrics());
	}
}
