package cn.antke.ezy.draw.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.DrawEntity;
import cn.antke.ezy.utils.ImageUtils;

/**
 * Created by liuzhichao on 2017/5/11.
 * 抽奖
 */
public class DrawAdapter extends BaseRecycleAdapter<DrawEntity> {

	private View.OnClickListener onClickListener;

	public DrawAdapter(List<DrawEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_draw);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		DrawEntity drawEntity = getItemData(position);
		ImageUtils.setSmallImg(holder.getView(R.id.sdv_item_draw_pic), drawEntity.getImgUrl());
		holder.setText(R.id.tv_item_draw_name, drawEntity.getName());
		holder.setText(R.id.tv_item_draw_price, holder.getContext().getString(R.string.integral, drawEntity.getPrice()));
		holder.setText(R.id.tv_item_draw_num, holder.getContext().getString(R.string.had_draw, drawEntity.getNum()));
		holder.setText(R.id.tv_item_draw_all, holder.getContext().getString(R.string.draw_people_all_num, drawEntity.getAllNum()));
		holder.setTag(R.id.ll_item_draw, drawEntity);
		holder.setOnClickListener(R.id.ll_item_draw, onClickListener);
	}
}
