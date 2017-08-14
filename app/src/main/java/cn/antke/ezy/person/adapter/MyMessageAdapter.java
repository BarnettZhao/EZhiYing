package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.MyMessageEntity;

/**
 * Created by zhaoweiwei on 2017/5/16.
 * 消息列表
 */

public class MyMessageAdapter extends BaseAdapterNew<MyMessageEntity> {
    private Context context;

    public MyMessageAdapter(Context context, List<MyMessageEntity> mDatas) {
        super(context, mDatas);
        this.context = context;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_my_message;
    }

    @Override
    protected void setViewData(View convertView, int position) {

        MyMessageEntity entity = getItem(position);

        TextView timeTv = ViewHolder.get(convertView, R.id.item_message_time);
        TextView typeTv = ViewHolder.get(convertView, R.id.item_message_type);
        SimpleDraweeView pic = ViewHolder.get(convertView, R.id.item_message_pic);
        TextView contentTv = ViewHolder.get(convertView, R.id.item_message_content);
        TextView detailTv = ViewHolder.get(convertView, R.id.item_message_detail);

        if (entity != null) {
            timeTv.setText(entity.getMessData());
            pic.setVisibility(View.GONE);
            contentTv.setText(entity.getMessageContent());
            String type = entity.getMessType();
            String typeName = "";
            //1：系统消息 2: 交易大厅续费 3: 消费服务中心续费 4：赠送审批消息(审核人); 5,赠送审批结果消息(申请人) 6: 积分互转 7: 商品审核 8: 支付成功 9: 退款消息
            if (StringUtil.isEmpty(type)) {
                typeName = "未设置";
            } else {
                switch (type) {
                    case "1"://系统消息
                        typeName = context.getString(R.string.message_push);
                        break;
                    case "2"://交易大厅续费
                        typeName = context.getString(R.string.message_transaction);
                        break;
                    case "3"://消费服务中心续费
                        typeName = context.getString(R.string.message_consumer);
                        break;
                    case "4"://赠送审批消息
                        typeName = context.getString(R.string.message_give);
                        break;
                    case "5"://赠送审批结果消息
                        typeName = context.getString(R.string.message_give_result);
                        break;
                    case "6"://积分互转
                        typeName = context.getString(R.string.message_integral);
                        break;
                    case "7"://商品审核
                        typeName = context.getString(R.string.message_goods);
                        break;
                    case "8"://支付成功
                        typeName = context.getString(R.string.message_pay_success);
                        break;
                    case "9:"://退款消息
                        typeName = context.getString(R.string.message_refund);
                        break;
                    default:
                        typeName = "未设置";
                        break;
                }
            }
            typeTv.setText(entity.getMessTitle());
        }
    }
}
