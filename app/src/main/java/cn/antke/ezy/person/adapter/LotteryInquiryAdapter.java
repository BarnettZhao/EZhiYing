package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.LotteryInquiryEntity;

/**
 * Created by zhaoweiwei on 2017/5/21.
 */

public class LotteryInquiryAdapter extends BaseAdapterNew<LotteryInquiryEntity> {
	private Context context;
	public LotteryInquiryAdapter(Context context, List<LotteryInquiryEntity> mDatas) {
		super(context, mDatas);
		this.context = context;
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_lottery_inquiry;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		LotteryInquiryEntity entity = getItem(position);
		TextView periods = ViewHolder.get(convertView, R.id.item_lottery_periods);
		TextView time = ViewHolder.get(convertView, R.id.item_lottery_time);
		TextView integral = ViewHolder.get(convertView, R.id.item_lottery_integral);
		TextView result = ViewHolder.get(convertView, R.id.item_lottery_result);

		periods.setText(entity.getPeriods());
		time.setText(entity.getTime());
		integral.setText(entity.getIntegral());
		String resultName= "";
		switch (entity.getResult()) {
			case "1":
				resultName = context.getString(R.string.lottery_state_yes);
				break;
			case "2":
				resultName = context.getString(R.string.lottery_state_no);
				break;
			case "3":
				resultName = context.getString(R.string.lottery_state_ing);
				break;
		}
		result.setText(resultName);
	}
}
