package com.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author songxudong
 */
public class GridViewForInner extends GridView {

	public GridViewForInner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
