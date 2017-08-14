package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ShareMemberEntity;

/**
 * Created by zhaoweiwei on 2017/5/17.
 * 分享会员列表（好友列表）
 */

public class ShareMemberAdapter extends BaseAdapterNew<ShareMemberEntity> {
    private List<ShareMemberEntity> mDatas;
    private View.OnClickListener onClickListener;

    public ShareMemberAdapter(Context context, List<ShareMemberEntity> mDatas, View.OnClickListener onClickListener) {
        super(context, mDatas);
        this.mDatas = mDatas;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getResourceId(int resId) {
        return R.layout.item_share_member;
    }

    @Override
    protected void setViewData(View convertView, int position) {
        ShareMemberEntity entity = getItem(position);
        TextView nameTv = ViewHolder.get(convertView, R.id.item_share_member_user);
        TextView catalogTv = ViewHolder.get(convertView, R.id.item_share_member_catalog);

        if (entity != null) {
            //根据position获取首字母作为目录catalog
            String catalog = entity.getFirstLetter();

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(catalog)) {
                catalogTv.setVisibility(View.VISIBLE);
                catalogTv.setText(entity.getFirstLetter().toUpperCase());
            } else {
                catalogTv.setVisibility(View.GONE);
            }

            nameTv.setText(entity.getName());
            nameTv.setTag(entity);
            nameTv.setOnClickListener(onClickListener);
        }
    }

    /**
     * 获取catalog首次出现位置
     */
    private int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mDatas.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }
}
