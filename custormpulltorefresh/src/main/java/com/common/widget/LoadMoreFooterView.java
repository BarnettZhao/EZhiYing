package com.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.common.widget.custormpulltorefresh.R;

/**
 * Created by Liu_Zhichao on 2016/8/25 10:15.
 * RecycleView加载更多的布局
 */
public class LoadMoreFooterView extends FrameLayout {

	public final static int STATE_LOADING = 0;//加载中
	public final static int STATE_COMPLETE = 1;//加载完成
	public final static int STATE_NOMORE = 2;//正常状态

	public LoadMoreFooterView(Context context) {
		super(context);
		initView(context);
	}

	public LoadMoreFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	/**
	 * 初始化
	 */
	private void initView(Context context) {
		addView(View.inflate(context, R.layout.addmores, null));
	}

	public void setState(int state) {
		switch (state) {
			case STATE_LOADING:
				setVisibility(VISIBLE);
				break;
			case STATE_COMPLETE:
				setVisibility(VISIBLE);
				break;
			case STATE_NOMORE:
				setVisibility(VISIBLE);
				break;
		}
	}
}
