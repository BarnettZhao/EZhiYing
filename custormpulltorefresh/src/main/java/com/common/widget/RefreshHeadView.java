package com.common.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.common.widget.custormpulltorefresh.R;

/**
 * Created by Liu_Zhichao on 2016/8/25 10:15.
 * RecycleView刷新的布局
 */
public class RefreshHeadView extends FrameLayout {

	public static final int STATE_REFRESHING = 2;//正在刷新的状态
	public static final int STATE_DONE = 3;//刷新结束的状态
	public static final int STATE_NORMAL = 0;//没有任何的状态
	public static final int STATE_RELEASE_TO_REFRESH = 1;//准备刷新的状态
	public static final int STATE_PULL = -1;//下拉中

	private RefreshRecyclerView.OnRefreshStateChangedListener onRefreshStateChangedListener;
	private View mContentView;//刷新头布局
	private int mMeasuredHeight;
	private int mState;

	public RefreshHeadView(Context context) {
		super(context);
		initView(context);
	}

	public RefreshHeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public RefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	/**
	 * 初始化
	 */
	private void initView(Context context) {
		mContentView = View.inflate(context, R.layout.fpull_to_refresh_header_vertical, null);
		//宽高
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//距离父布局的距离
		lp.setMargins(0, 0, 0, 0);
		//设置布局的宽高的属性
		setLayoutParams(lp);
		//设置内容距离布局的边界的尺寸
		setPadding(0, 0, 0, 0);
		//添加内容布局并且设置宽是屏幕的宽.高为0
		addView(mContentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
		//测量
		measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//获取高度
		mMeasuredHeight = getMeasuredHeight();
	}

	public void setOnRefreshStateChangedListener(RefreshRecyclerView.OnRefreshStateChangedListener onRefreshStateChangedListener) {
		this.onRefreshStateChangedListener = onRefreshStateChangedListener;
	}

	public void setState(int state) {
		mState = state;
		if (onRefreshStateChangedListener != null) {
			onRefreshStateChangedListener.onRefreshStateChanged(state);
		}
	}

	public int getmState() {
		return mState;
	}

	/**
	 * 释放意图
	 */
	public boolean releaseAction() {
		//是否在刷新
		boolean isOnRefresh = false;
		//获取显示的高度
		int height = getVisibleHeight();
		if (height == 0) // not visible.
			isOnRefresh = false;//没有刷新
		if (height > mMeasuredHeight && mState < STATE_REFRESHING) {
			setState(STATE_REFRESHING);
			isOnRefresh = true;//正在刷新
		}
		// refreshing and header isn't shown fully. do nothing.
//		if (mState == STATE_REFRESHING && height <= mMeasuredHeight) {
		//return;
//		}
		int destHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mState == STATE_REFRESHING) {
			destHeight = mMeasuredHeight;
		}
		smoothScrollTo(destHeight, isOnRefresh);
		if (!isOnRefresh) {
			setState(STATE_PULL);
		}

		return isOnRefresh;
	}

	/**
	 * 移动距离
	 */
	public void onMove(RefreshRecyclerView refreshRecyclerView, float delta) {
		if (getVisibleHeight() > 0 || delta > 0) {
			int dHeight = (int) delta + getVisibleHeight();
			setVisibleHeight(dHeight);
			//处理可能会产生下拉刷新时，刷新头不显示的问题，实际头的高度已经有了但是却看不到，需要滚动RecycleView来显示头部
			//这是在加了单独的头部view时才出现的，暂时不确定是否会对其他地方有影响
			refreshRecyclerView.scrollBy(0, -dHeight);
			if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态
				if (getVisibleHeight() > mMeasuredHeight) {
					setState(STATE_RELEASE_TO_REFRESH);
				} else {
					setState(STATE_PULL);
				}
			}
		} else {
			setState(STATE_NORMAL);
		}
	}

	/**
	 * 这是一个让布局高度改变的方法
	 */
	public void smoothScrollTo(int Height, final boolean isOnRefresh) {
		ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), Height);
		animator.setDuration(300).start();
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				setVisibleHeight((int) animation.getAnimatedValue());
			}
		});
		animator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!isOnRefresh) {
					setState(STATE_NORMAL);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		animator.start();
	}

	/**
	 * 设置布局高度为0,并设置状态为正常状态
	 */
	public void reset() {
		smoothScrollTo(0, false);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				setState(STATE_NORMAL);
			}
		}, 500);
	}

	/**
	 * 刷新完成
	 */
	public void refreshComplete() {
		setState(STATE_DONE);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				reset();
			}
		}, 200);
	}

	/**
	 * 获取显示的高度
	 */
	public int getVisibleHeight() {
		ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
		return layoutParams.height;
	}

	/**
	 * 设置显示的高度
	 */
	public void setVisibleHeight(int height) {
		if (height < 0) height = 0;
		ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
		layoutParams.height = height;
		mContentView.setLayoutParams(layoutParams);
	}
}
