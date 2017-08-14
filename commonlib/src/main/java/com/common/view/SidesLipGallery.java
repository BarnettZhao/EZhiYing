package com.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;

/**
 * 自定义GALLERY实现一次滑动一张
 */
public class SidesLipGallery extends Gallery {

	/**
	 * 默认处理的构造函数
	 * @param context 传进来要显示在哪页面的页面
	 */
	public SidesLipGallery(Context context) {
		super(context);
	}

	public SidesLipGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SidesLipGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		boolean touched = false;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				touched = dispatch(event);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				touched = dispatch(event);
				break;
			case MotionEvent.ACTION_MOVE:
				touched = dispatch(event);
				break;
			default:
				break;
		}

		if (touched) {
			return true;
		} else {
			return super.dispatchTouchEvent(event);
		}

	}

	private boolean dispatch(MotionEvent event) {
		View child = getSelectedView();
		if (child == null)
			return false;
		return child.dispatchTouchEvent(event);
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int keyCode;
		if (e1 == null || e2 == null) {
			return true;
		}
		if (isScrollingLeft(e1, e2)) {
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(keyCode, null);
		return true;
	}
}
