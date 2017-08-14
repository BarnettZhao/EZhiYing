package com.common.widget;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Liu_Zhichao on 2016/8/26 01:47.
 * RecycleView的item点击事件
 */
public class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

	private GestureDetectorCompat mGestureDetector;
	private RecyclerView recyclerView;
	private RefreshRecyclerView.OnItemClickListener onItemClickListener;

	public OnRecyclerItemClickListener(RecyclerView recyclerView, RefreshRecyclerView.OnItemClickListener onItemClickListener) {
		this.recyclerView = recyclerView;
		this.onItemClickListener = onItemClickListener;
		mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		mGestureDetector.onTouchEvent(e);
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent e) {
		mGestureDetector.onTouchEvent(e);
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
	}

	private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
			if (child != null) {
				RecyclerViewHolder holder = (RecyclerViewHolder) recyclerView.getChildViewHolder(child);
				onItemClickListener.onItemClick(holder);
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
			if (child != null) {
				RecyclerViewHolder holder = (RecyclerViewHolder) recyclerView.getChildViewHolder(child);
//				onItemLongClick(vh);
			}
		}
	}
}