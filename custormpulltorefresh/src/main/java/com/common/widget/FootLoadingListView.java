/**
 *
 */
package com.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;


/**
 * @author songxudong
 *         继承PullToRefreshListView
 *         1.解决setOnItemClickListener错位问题
 *         2.item不足默认数量时停止加载更多
 */
public class FootLoadingListView extends PullToRefreshListView implements OnItemClickListener {

	public FootLoadingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setShowIndicator(false);
	}

	public FootLoadingListView(
			Context context,
			PullToRefreshBase.Mode mode,
			PullToRefreshBase.AnimationStyle style) {
		super(context, mode, style);
	}

	public FootLoadingListView(Context context,
	                           PullToRefreshBase.Mode mode) {
		super(context, mode);
	}

	public FootLoadingListView(Context context) {
		super(context);

	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		/*if (adapter.getCount() < DEFALUT_PAGE_NUM) {
			setCanAddMore(false);
		} else {*/
//			setCanAddMore(true);
		/*}*/
		super.setAdapter(adapter);
	}

	public void setCanAddMore(boolean flag) {
		if (DEBUG) {
			Log.v(LOG_TAG, "setCanAndMore flag : " + flag);
		}
		if (flag) {
			setMode(Mode.BOTH);
		} else {
			if (this.getMode() == Mode.BOTH) {
				setMode(Mode.PULL_FROM_START);
			} else if (this.getMode() == Mode.PULL_FROM_END) {
				setMode(Mode.DISABLED);
			} else {
				setMode(Mode.PULL_FROM_START);
			}
		}

	}

	public void setCanMoreAndUnReFresh(boolean flag) {
		if (DEBUG) {
			Log.v(LOG_TAG, "setCanMoreAndUnReFresh flag : " + flag);
		}
		if (flag) {
			setMode(Mode.PULL_FROM_END);
		} else {
			if (this.getMode() == Mode.BOTH) {
				setMode(Mode.PULL_FROM_START);
			} else if (this.getMode() == Mode.PULL_FROM_END) {
				setMode(Mode.DISABLED);
			} else {
				setMode(Mode.DISABLED);
			}
		}

	}

	public void setOnRefreshComplete() {
		onRefreshComplete();
	}

	public void setOnLoadMoreComplete() {
		onRefreshComplete();
	}

	private OnItemClickListener aListener;

	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		aListener = listener;
		super.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (aListener == null) {
			return;
		}
		try {
			aListener.onItemClick(parent, view, position - getRefreshableView().getHeaderViewsCount(), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
