package com.common.widget;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Liu_Zhichao on 2016/8/28 22:16.
 * RecycleView的基类Adapter
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

	private List<T> datas;
	private int page = 1;

	public BaseRecycleAdapter(List<T> datas) {
		this.datas = datas;
	}

	@Override
	public abstract RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

	@Override
	public abstract void onBindViewHolder(RecyclerViewHolder holder, int position);

	@Override
	public int getItemCount() {
		return datas.size();
	}

	public List<T> getDatas() {
		return datas;
	}

	public T getItemData(int position) {
		return datas.get(position);
	}

	public void addData(T data) {
		datas.add(data);
		notifyDataSetChanged();
	}

	public void addData(int index, T data) {
		datas.add(index, data);
		notifyDataSetChanged();
	}

	public void addDatas(List<T> data) {
		datas.addAll(data);
		page++;
		notifyDataSetChanged();
	}

	public void removeData(T data) {
		datas.remove(data);
		notifyDataSetChanged();
	}

	public void removeData(int index) {
		datas.remove(index);
		notifyDataSetChanged();
	}

	public void removeAll(List<T> data) {
		datas.removeAll(data);
		notifyDataSetChanged();
	}

	public void clearData() {
		datas.clear();
		page = 1;
		notifyDataSetChanged();
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}
