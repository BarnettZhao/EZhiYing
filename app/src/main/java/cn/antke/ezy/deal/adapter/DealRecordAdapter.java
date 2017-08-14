package cn.antke.ezy.deal.adapter;

import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.DealRecordEntity;

/**
 * Created by liuzhichao on 2017/5/12.
 * 交易记录
 */
public class DealRecordAdapter extends BaseRecycleAdapter<DealRecordEntity> {

	public DealRecordAdapter(List<DealRecordEntity> datas) {
		super(datas);
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_deal_record);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		DealRecordEntity recordEntity = getItemData(position);
		holder.setText(R.id.tv_record_consume_num, holder.getContext().getString(R.string.product_sell_integral, recordEntity.getConsumeNum()));
		holder.setText(R.id.tv_record_buy_num, "+" + holder.getContext().getString(R.string.multifunctional_integral, recordEntity.getBuyNum()));
		holder.setText(R.id.tv_record_date, recordEntity.getDate());
	}
}
