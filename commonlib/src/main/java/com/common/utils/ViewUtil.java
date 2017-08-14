/**
 * 
 */
package com.common.utils;

import android.graphics.Bitmap;
import android.view.View;

/**
 * @author songxudong
 * 把view变成图片工具类
 */
public class ViewUtil {
	public static Bitmap getBitmapFromView(View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache(true);
	}
}
