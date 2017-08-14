package cn.antke.ezy.deal.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.SaleEntity;

/**
 * Created by liuzhichao on 2017/5/23.
 * 卖出adapter
 */
public class SaleAdapter extends BaseRecycleAdapter<SaleEntity> {

	private View.OnClickListener onClickListener;

	public SaleAdapter(List<SaleEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_sale);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		SaleEntity saleEntity = getItemData(position);
		holder.setText(R.id.tv_sale_integral, holder.getContext().getString(R.string.product_sell_integral, saleEntity.getSaleIntegral()));
		holder.setText(R.id.tv_sale_price, holder.getContext().getString(R.string.multifunctional_integral, saleEntity.getSalePrice()));
		holder.setText(R.id.tv_sale_date, saleEntity.getDate());
		holder.setTag(R.id.tv_sale_cancel, saleEntity);
		holder.setOnClickListener(R.id.tv_sale_cancel, onClickListener);
		if (saleEntity.getIsUndo() == 1) {
			holder.setVisible(R.id.tv_sale_cancel, View.VISIBLE);
		} else {
			holder.setVisible(R.id.tv_sale_cancel, View.GONE);
		}
	}
}
