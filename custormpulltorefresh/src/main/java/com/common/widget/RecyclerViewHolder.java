package com.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Liu_Zhichao on 2016/8/23 16:52.
 * RecycleView的ViewHolder
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

	private SparseArray<View> mViews;
	private View mConvertView;
	private Context mContext;
	private Object tag;

	public RecyclerViewHolder(Context context, View itemView) {
		super(itemView);
		mContext = context;
		mConvertView = itemView;
		mViews = new SparseArray<>();
	}

	public RecyclerViewHolder(ViewGroup view, int layoutId) {
		super(LayoutInflater.from(view.getContext()).inflate(layoutId, view, false));
		mContext = view.getContext();
		mConvertView = itemView;
		mViews = new SparseArray<>();
	}

	public Context getContext() {
		return mContext;
	}

	/**
	 * 通过viewId获取控件
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}


	/****
	 * 以下为辅助方法
	 *****/

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	/**
	 * 设置TextView的值
	 */
	public RecyclerViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	public RecyclerViewHolder setImageUri(int viewId, String url) {
		ImageView view = getView(viewId);
		view.setImageURI(Uri.parse(url));
		return this;
	}

	public RecyclerViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}

	public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	public RecyclerViewHolder setImageDrawable(int viewId, Drawable drawable) {
		ImageView view = getView(viewId);
		view.setImageDrawable(drawable);
		return this;
	}

	public RecyclerViewHolder setBackgroundColor(int viewId, int color) {
		View view = getView(viewId);
		view.setBackgroundColor(color);
		return this;
	}

	public RecyclerViewHolder setBackgroundRes(int viewId, int backgroundRes) {
		View view = getView(viewId);
		view.setBackgroundResource(backgroundRes);
		return this;
	}

	public RecyclerViewHolder setTextColor(int viewId, int textColor) {
		TextView view = getView(viewId);
		view.setTextColor(textColor);
		return this;
	}

	public RecyclerViewHolder setTextColorRes(int viewId, int textColorRes) {
		TextView view = getView(viewId);
		view.setTextColor(mContext.getResources().getColor(textColorRes));
		return this;
	}

	public RecyclerViewHolder setAlpha(int viewId, float value) {
		getView(viewId).setAlpha(value);
		return this;
	}

	public RecyclerViewHolder setVisible(int viewId, int visible) {
		View view = getView(viewId);
		view.setVisibility(visible);
		return this;
	}

	public RecyclerViewHolder linkify(int viewId) {
		TextView view = getView(viewId);
		Linkify.addLinks(view, Linkify.ALL);
		return this;
	}

	public RecyclerViewHolder setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			TextView view = getView(viewId);
			view.setTypeface(typeface);
			view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
		return this;
	}

	public RecyclerViewHolder setProgress(int viewId, int progress) {
		ProgressBar view = getView(viewId);
		view.setProgress(progress);
		return this;
	}

	public RecyclerViewHolder setProgress(int viewId, int progress, int max) {
		ProgressBar view = getView(viewId);
		view.setMax(max);
		view.setProgress(progress);
		return this;
	}

	public RecyclerViewHolder setMax(int viewId, int max) {
		ProgressBar view = getView(viewId);
		view.setMax(max);
		return this;
	}

	public RecyclerViewHolder setRating(int viewId, float rating) {
		RatingBar view = getView(viewId);
		view.setRating(rating);
		return this;
	}

	public RecyclerViewHolder setRating(int viewId, float rating, int max) {
		RatingBar view = getView(viewId);
		view.setMax(max);
		view.setRating(rating);
		return this;
	}

	public Object getTag(int viewId) {
		return getView(viewId).getTag();
	}

	public Object getTag(int viewId, int key) {
		return getView(viewId).getTag(key);
	}

	public RecyclerViewHolder setTag(int viewId, Object tag) {
		View view = getView(viewId);
		view.setTag(tag);
		return this;
	}

	public RecyclerViewHolder setTag(int viewId, int key, Object tag) {
		View view = getView(viewId);
		view.setTag(key, tag);
		return this;
	}

	public RecyclerViewHolder setChecked(int viewId, boolean checked) {
		Checkable view = getView(viewId);
		view.setChecked(checked);
		return this;
	}

	/**
	 * 关于事件的
	 */
	public RecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
		View view = getView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	public RecyclerViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
		View view = getView(viewId);
		view.setOnTouchListener(listener);
		return this;
	}

	public RecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
		View view = getView(viewId);
		view.setOnLongClickListener(listener);
		return this;
	}
}
