package cn.antke.ezy.draw.adapter;

import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.DrawQueryEntity;

/**
 * Created by liuzhichao on 2017/5/15.
 * 中奖
 */
public class DrawListAdapter extends BaseRecycleAdapter<DrawQueryEntity> {

	public DrawListAdapter(List<DrawQueryEntity> datas) {
		super(datas);
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_draw_query);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		DrawQueryEntity drawQueryEntity = getItemData(position);
		holder.setText(R.id.tv_draw_query_no, drawQueryEntity.getNo());
		holder.setText(R.id.tv_draw_query_user, drawQueryEntity.getUserNo());
		holder.setText(R.id.tv_draw_query_prize, drawQueryEntity.getPrizeName());
	}
}
