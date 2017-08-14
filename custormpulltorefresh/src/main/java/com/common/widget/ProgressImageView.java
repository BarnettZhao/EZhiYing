package com.common.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ProgressImageView extends ImageView {

	AnimationDrawable frameAnimation;

	public ProgressImageView(Context context) {
		super(context);
		frameAnimation = (AnimationDrawable) getBackground();
	}

	public ProgressImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		frameAnimation = (AnimationDrawable) getBackground();
	}


	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		startAnimation();

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopAnimation();
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (visibility == GONE || visibility == INVISIBLE) {
			stopAnimation();
		} else {
			startAnimation();
		}
	}

	@Override
	public void setVisibility(int v) {
		if (getVisibility() != v) {
			super.setVisibility(v);

			if (v == GONE || v == INVISIBLE) {
				stopAnimation();
			} else {
				startAnimation();
			}
		}
	}

	private void startAnimation() {
		if (frameAnimation == null) {
			frameAnimation = (AnimationDrawable) getBackground();
		}
		if (frameAnimation != null)
			frameAnimation.start();
	}

	private void stopAnimation() {
		if (frameAnimation == null) {
			frameAnimation = (AnimationDrawable) getBackground();
		}
		if (frameAnimation != null)
			frameAnimation.stop();
	}
}
