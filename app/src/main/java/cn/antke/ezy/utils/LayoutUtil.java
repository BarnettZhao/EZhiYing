package cn.antke.ezy.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhaoweiwei on 2017/3/15.
 * 这个类中只有一个方法,设置高度跟宽度一样，宽度获取屏幕宽度均分
 */

public class LayoutUtil {


	/**
	 * @param context context 上下文
	 * @param view    需要设置的view
	 * @param ratio   屏幕宽度均分为几个view
	 * @param space   间隔宽度 dp
	 */
	public static void setHeightAsWidth(Context context, View view, int ratio, int space) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		//获取屏幕宽度和高度
		int width = dm.widthPixels;
		ViewGroup.LayoutParams params = view.getLayoutParams();
		//用屏幕的宽度减去间隔的宽度，然后均等分。
		//CommonTools.dp2px(context, space) * (ratio - 1) 这个是所有间隔的宽度之和
		if (ratio != 0 && space != 0) {
			int length = (width - CommonTools.dp2px(context, space) * (ratio - 1)) / ratio;
			params.width = length;
			params.height = length;//设置当前控件布局的高度
		} else {
			params.height = width;
		}
		view.setLayoutParams(params);//将设置好的布局参数应用到控件中
	}

	public static void setHeightAndWidth(Context context,View view,float ratio) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		//获取屏幕宽度和高度
		int width = dm.widthPixels;
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.width = width;
		params.height = (int) (width * ratio);
		view.setLayoutParams(params);
	}
}
