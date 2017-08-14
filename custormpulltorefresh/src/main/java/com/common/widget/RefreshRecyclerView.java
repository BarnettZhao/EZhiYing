package com.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/8/23 10:36.
 * 带刷新加载的RecycleView
 */
public class RefreshRecyclerView extends RecyclerView {

	private static final String TAG = RefreshRecyclerView.class.getSimpleName();

	public static final int REFRESHING = 0x1;//刷新中
	public static final int NORMAL = 0x2;

	private OnRefreshAndLoadMoreListener onRefreshAndLoadMoreListener;
	private ArrayList<View> mHeaderViews = new ArrayList<>();//头布局集合
	private ArrayList<View> mFootViews = new ArrayList<>();//脚布局集合
	private static List<Integer> headerTypes = new ArrayList<>();//头布局类型
	private static List<Integer> footerTypes = new ArrayList<>();//脚布局类型
	private static final int BASE_ITEM_TYPE_HEADER = 100000;//头布局type起始值
	private static final int BASE_ITEM_TYPE_FOOTER = 200000;//脚布局type起始值
	private RefreshHeadView mHeadView;//刷新头布局
	private LoadMoreFooterView mFooterView;//加载脚布局
	private int lastPos;
	private int currentState;
	private static final float DRAG_RATE = 2;//阻率
	private View mEmptyView;
	private float mLastY = -1;//最后的y坐标
	private Mode mode = Mode.DISABLED;//刷新模式
	private WrapperAdapter mWrapAdapter;
	private AdapterDataObserver mDataObserver;
	private boolean canAddMore = true;

	public RefreshRecyclerView(Context context) {
		super(context);
		init();
	}

	public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化
	 * 添加刷新加载布局
	 */
	private void init() {
		RefreshHeadView refreshHeader = new RefreshHeadView(getContext());
		addRefreshHeader(refreshHeader);
		mHeadView = refreshHeader;

		LoadMoreFooterView footView = new LoadMoreFooterView(getContext());
		addLoadMoreFooter(footView);
		mFooterView = footView;

		mDataObserver = new DataObserver();
	}

	@Override
	public void setAdapter(Adapter adapter) {
		mWrapAdapter = new WrapperAdapter(adapter);
		super.setAdapter(mWrapAdapter);
		adapter.registerAdapterDataObserver(mDataObserver);
		mDataObserver.onChanged();
	}

	@Override
	public void onScrollStateChanged(int state) {
		if (state == RecyclerView.SCROLL_STATE_IDLE && canLoadMore() && canAddMore && onRefreshAndLoadMoreListener != null && currentState != REFRESHING && isOnBottom()) {
			LayoutManager layoutManager = getLayoutManager();
			if (layoutManager instanceof GridLayoutManager) {
				lastPos = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
			} else if (layoutManager instanceof StaggeredGridLayoutManager) {
				StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
				int[] auto = new int[staggeredGridLayoutManager.getSpanCount()];
				staggeredGridLayoutManager.findLastVisibleItemPositions(auto);
				lastPos = findMax(auto);
			} else {
				lastPos = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
			}
			if (layoutManager.getChildCount() > 0 && lastPos >= layoutManager.getChildCount() - 1) {
				currentState = REFRESHING;
				View footView = mFootViews.get(mFootViews.size() - 1);
				if (footView instanceof LoadMoreFooterView) {
					((LoadMoreFooterView) footView).setState(LoadMoreFooterView.STATE_LOADING);
				} else {
					footView.setVisibility(VISIBLE);
				}
				onRefreshAndLoadMoreListener.onLoadMore();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (mLastY == -1) {
			mLastY = e.getRawY();
		}
		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = e.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float deltaY = e.getRawY() - mLastY;
				mLastY = e.getRawY();
				if (canRefresh()) {
					if (isOnTop()) {
//						scrollBy(0, (int) (deltaY / DRAG_RATE));
						mHeadView.onMove(this, deltaY / DRAG_RATE);
						if (mHeadView.getVisibleHeight() > 0 && mHeadView.getmState() < RefreshHeadView.STATE_REFRESHING) {
							return false;
						}
					} else {
						mHeadView.onMove(this, 0);
					}
				}
				break;
			default:
				mLastY = -1;
				if (isOnTop() && canRefresh() && onRefreshAndLoadMoreListener != null && mHeadView.releaseAction()) {
					onRefreshAndLoadMoreListener.onRefresh();
				}
				break;
		}
		return super.onTouchEvent(e);
	}

	/**
	 * 是否是顶部
	 */
	/*public boolean isOnTop() {
		LayoutManager layoutManager = getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			int firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
			return 0 == firstVisibleItemPosition;
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
			int[] into = new int[staggeredGridLayoutManager.getSpanCount()];
			staggeredGridLayoutManager.findFirstVisibleItemPositions(into);
			return 0 == findMin(into);
		} else {
			return 0 == ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
		}
	}*/
	public boolean isOnTop() {
		return !(mHeaderViews == null || mHeaderViews.isEmpty()) && mHeaderViews.get(0).getParent() != null;
	}

	public boolean isOnBottom() {
		if (mWrapAdapter != null) {
			LayoutManager layoutManager = getLayoutManager();
			if (layoutManager instanceof GridLayoutManager) {
				int lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
				return lastVisibleItemPosition == (mWrapAdapter.getItemCount() - 1);
			} else if (layoutManager instanceof StaggeredGridLayoutManager) {
				StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
				int[] into = new int[staggeredGridLayoutManager.getSpanCount()];
				staggeredGridLayoutManager.findFirstVisibleItemPositions(into);
				return (mWrapAdapter.getItemCount() - 1) == findMax(into);
			} else {
				return (mWrapAdapter.getItemCount() - 1) == ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
			}
		}
		return false;
	}

	private int findMax(int[] into) {
		int max = into[0];
		for (int value : into) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	private int findMin(int[] into) {
		int min = into[0];
		for (int value : into) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	private boolean canRefresh() {
		return mode == Mode.PULL_FROM_START || mode == Mode.BOTH;
	}

	private boolean canLoadMore() {
		return mode == Mode.PULL_FROM_END || mode == Mode.BOTH;
	}

	public void setCanAddMore(boolean canAddMore) {
		this.canAddMore = canAddMore;
		if (!canAddMore) {
			removeLoadMoreFooter();
		} else {
			addLoadMoreFooter(mFooterView);
		}
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		addOnItemTouchListener(new OnRecyclerItemClickListener(this, onItemClickListener));
	}

	public void setOnRefreshAndLoadMoreListener(OnRefreshAndLoadMoreListener onRefreshAndLoadMoreListener) {
		this.onRefreshAndLoadMoreListener = onRefreshAndLoadMoreListener;
	}

	public void setOnRefreshStateChangedListener(OnRefreshStateChangedListener onRefreshStateChangedListener) {
		if (mHeadView != null && canRefresh()) {
			mHeadView.setOnRefreshStateChangedListener(onRefreshStateChangedListener);
		}
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		if (!canLoadMore()) {
			setCanAddMore(false);
		}
	}

	public void addRefreshHeader(RefreshHeadView view) {
		if (mHeaderViews.size() <= 0) {
			mHeaderViews.add(view);
		} else {
			if (mHeaderViews.get(0) instanceof RefreshHeadView) {
				mHeaderViews.remove(0);
			}
			mHeaderViews.add(0, view);
		}
	}

	public void addLoadMoreFooter(LoadMoreFooterView view) {
		if (mFootViews.size() <= 0) {
			mFootViews.add(view);
		} else {
			if (mFootViews.get(mFootViews.size() - 1) instanceof LoadMoreFooterView) {
				mFootViews.remove(mFootViews.size() - 1);
			}
			mFootViews.add(view);
		}
	}

	public void removeLoadMoreFooter() {
		if (mFootViews.size() > 0) {
			View view = mFootViews.get(mFootViews.size() - 1);
			if (view instanceof LoadMoreFooterView) {
				mFootViews.remove(view);
				if (mWrapAdapter != null) {
					mWrapAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	public int getHeaderSize() {
		return mWrapAdapter.getHeadersCount();
	}

	public int getFooterSize() {
		return mWrapAdapter.getFootersCount();
	}

	/**
	 * @param view 添加头布局
	 */
	public void addHeaderView(View view) {
		headerTypes.add(BASE_ITEM_TYPE_HEADER + mHeaderViews.size());
		if (mHeaderViews.contains(view)) {
			Log.e(TAG, "RecyclerView不支持添加相同view对象");
			return;
		}
		mHeaderViews.add(view);
	}

	/**
	 * @param view 添加底部布局
	 */
	public void addFooterView(View view) {
		mFootViews.add(view);
	}

	/**
	 * DISABLED 禁止下拉上拉
	 * PULL_FROM_START 支持下拉刷新
	 * PULL_FROM_END 支持上拉加载
	 * BOTH 支持下拉刷新上拉加载
	 * //MANUAL_REFRESH_ONLY 仅支持手动触发
	 */
	public enum Mode {
		DISABLED, PULL_FROM_START, PULL_FROM_END, BOTH
	}

	public interface OnRefreshAndLoadMoreListener {
		void onRefresh();

		void onLoadMore();
	}

	public interface OnItemClickListener {
		void onItemClick(RecyclerViewHolder holder);
	}

	public interface OnRefreshStateChangedListener {
		void onRefreshStateChanged(int state);
	}

	class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		private static final int ITEM_TYPE_REFRESH_HEADER = 0x1;
		private static final int ITEM_TYPE_LOADMORE_FOOTER = 0x2;
		private static final int ITEM_TYPE_NORMAL = 0;

		private RecyclerView.Adapter mInnerAdapter;

		WrapperAdapter(RecyclerView.Adapter mInnerAdapter) {
			this.mInnerAdapter = mInnerAdapter;
		}

		@Override
		public int getItemViewType(int position) {
			if (isRefreshHeader(position)) {
				return ITEM_TYPE_REFRESH_HEADER;
			}

			if (isContentHeader(position)) {
				return headerTypes.get(--position);
			}
			/*else if (isContentFooter(position)) {
				return footerTypes.get(position);
				return ITEM_TYPE_NORMAL;
			}*/
			if (isLoadMoreFooter(position)) {
				return ITEM_TYPE_LOADMORE_FOOTER;
			}

			int adjPosition = position - getHeadersCount();
			if (mInnerAdapter != null && adjPosition < mInnerAdapter.getItemCount()) {
				return mInnerAdapter.getItemViewType(adjPosition);
			}

			return ITEM_TYPE_NORMAL;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if (viewType == ITEM_TYPE_REFRESH_HEADER) {
				return new RecyclerViewHolder(parent.getContext(), mHeaderViews.get(0));
			}

			if (headerTypes.contains(viewType)) {
				return new RecyclerViewHolder(parent.getContext(), mHeaderViews.get(headerTypes.indexOf(viewType) + 1));
			}

			if (viewType == ITEM_TYPE_LOADMORE_FOOTER) {
				View view = mFootViews.get(mFootViews.size() - 1);
				ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				view.setLayoutParams(layoutParams);
				return new RecyclerViewHolder(parent.getContext(), view);
			}

			return mInnerAdapter.onCreateViewHolder(parent, viewType);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			if (isHeader(position) || isFooter(position) || mInnerAdapter == null) {
				return;
			}
			if (position >= getHeadersCount())
				mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
		}

		/**
		 * 当发现layoutManager为GridLayoutManager时，通过设置SpanSizeLookup，
		 * 对其getSpanSize方法，返回值设置为layoutManager.getSpanCount();
		 */
		@Override
		public void onAttachedToRecyclerView(RecyclerView recyclerView) {
			if (mInnerAdapter == null) {
				return;
			}
			mInnerAdapter.onAttachedToRecyclerView(recyclerView);

			RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
			if (layoutManager instanceof GridLayoutManager) {
				final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
				gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
					/**
					 * @param position 设置下标position位置的View占用几个原有位置
					 */
					@Override
					public int getSpanSize(int position) {
						return isHeader(position) || isFooter(position) ? gridLayoutManager.getSpanCount() : 1;
					}
				});
			}
		}

		/**
		 * 瀑布流添加头尾布局不能占满一行的处理
		 */
		@Override
		public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
			if (mInnerAdapter != null) {
				mInnerAdapter.onViewAttachedToWindow(holder);
				int position = holder.getLayoutPosition();
				if (isRefreshHeader(position) || isLoadMoreFooter(position)) {
					ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
					if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
						((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
					}
				}
			}
		}

		@Override
		public long getItemId(int position) {
			if (mInnerAdapter != null && position >= getHeadersCount()) {
				int adjPosition = position - getHeadersCount();
				if (adjPosition < mInnerAdapter.getItemCount()) {
					return mInnerAdapter.getItemId(adjPosition);
				}
			}
			return -1;
		}

		@Override
		public int getItemCount() {
			return getHeadersCount() + getFootersCount() + getRealItemCount();
		}

		private int getRealItemCount() {
			if (mInnerAdapter != null) {
				return mInnerAdapter.getItemCount();
			}
			return 0;
		}

		int getHeadersCount() {
			return mHeaderViews.size();
		}

		int getFootersCount() {
			return mFootViews.size();
		}

		private boolean isHeader(int position) {
			return position >= 0 && position < mHeaderViews.size();
		}

		private boolean isFooter(int position) {
			return position < getItemCount() && position >= getItemCount() - mFootViews.size();
		}

		private boolean isRefreshHeader(int position) {
			return position == 0;
		}

		private boolean isContentHeader(int position) {
			return position > 0 && position < getHeadersCount();
		}

		private boolean isContentFooter(int position) {
			return position >= getItemCount() - mFootViews.size() && position < getItemCount();
		}

		private boolean isLoadMoreFooter(int position) {
			return canAddMore && position == getItemCount() - 1;
		}
	}

	/**
	 * @return 获取一个空布局
	 */
	public View getmEmptyView() {
		return mEmptyView;
	}

	/**
	 * 设置空布局
	 */
	public void setEmptyView(View emptyView) {
		mEmptyView = emptyView;
		//数据改变
		mDataObserver.onChanged();
	}

	/**
	 * 还原所有的状态
	 */
	public void resetStatus() {
		setReFreshComplete();
		setloadMoreComplete();
	}

	/**
	 * 设置加载更多完成 use resetStatus() instead
	 */
	private void setloadMoreComplete() {
		currentState = NORMAL;
		if (mWrapAdapter != null && mFootViews.size() > 0) {
			View footView = mFootViews.get(mWrapAdapter.getFootersCount() - 1);
			if (footView instanceof LoadMoreFooterView) {
				((LoadMoreFooterView) footView).setState(LoadMoreFooterView.STATE_COMPLETE);
			} else {
				footView.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置刷新完成, use resetStatus() instead
	 */
	private void setReFreshComplete() {
		if (mHeadView != null) {
			mHeadView.refreshComplete();
		}
	}

	/**
	 * @param refreshing 设置是否刷新ing状态
	 */
	public void setRefreshing(boolean refreshing) {
		if (refreshing && canRefresh() && onRefreshAndLoadMoreListener != null) {
			mHeadView.setState(RefreshHeadView.STATE_REFRESHING);
			mHeadView.onMove(this, mHeadView.getMeasuredHeight());
			onRefreshAndLoadMoreListener.onRefresh();
		}
	}

	/**
	 * 这是一个数据观察者
	 */
	private class DataObserver extends RecyclerView.AdapterDataObserver {
		@Override
		public void onChanged() {
			Adapter<?> adapter = getAdapter();
			if (adapter != null && mEmptyView != null) {
				int emptyCount = 0;
				if (canRefresh()) {
					emptyCount++;
				}
				if (canLoadMore() && canAddMore) {
					emptyCount++;
				}
				if (adapter.getItemCount() == emptyCount) {
					mEmptyView.setVisibility(View.VISIBLE);
					RefreshRecyclerView.this.setVisibility(View.GONE);
				} else {
					mEmptyView.setVisibility(View.GONE);
					RefreshRecyclerView.this.setVisibility(View.VISIBLE);
				}
			}
			if (mWrapAdapter != null) {
				mWrapAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
		}
	}
}
