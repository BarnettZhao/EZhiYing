package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.ShareEntity;
import cn.antke.ezy.network.entities.ShareMemberEntity;
import cn.antke.ezy.network.entities.ShareMemberPageEntity;
import cn.antke.ezy.person.adapter.ShareMemberAdapter;
import cn.antke.ezy.utils.CommonShareUtil;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.ViewInjectUtils;
import cn.antke.ezy.widget.SideBar;

import static cn.antke.ezy.common.CommonConstant.PAGENUM;
import static cn.antke.ezy.common.CommonConstant.PAGESIZE;
import static cn.antke.ezy.common.CommonConstant.PAGE_SIZE_10;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/21.
 * 好友
 */

public class GoodFriendActivity extends ToolBarActivity implements View.OnClickListener {
    @ViewInject(R.id.good_friend_phone)
    private TextView goodFriendPhone;
    @ViewInject(R.id.good_friend_wx)
    private TextView goodFriendWx;
    @ViewInject(R.id.share_member_list)
    private FootLoadingListView goodFriendList;
    @ViewInject(R.id.share_member_sidebar)
    private SideBar goodFriendSidebar;

    private ShareMemberAdapter adapter;
    private String title, content, img, qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_good_friend);
        setLeftTitle(getString(R.string.person_good_friend));
        ViewInjectUtils.inject(this);
        goodFriendPhone.setOnClickListener(this);
        goodFriendWx.setOnClickListener(this);
        loadData(false);
        goodFriendList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(true);
            }
        });

        //分享信息
        requestHttpData(Constants.Urls.URL_POST_GET_QRCODE, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, null);
    }

    private void loadData(boolean isMore) {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        int page = 1;
        int request = REQUEST_NET_ONE;
        if (isMore) {
            page = adapter.getPage() + 1;
            request = REQUEST_NET_TWO;
        }
        params.put(PAGENUM, String.valueOf(page));
        params.put(PAGESIZE, PAGE_SIZE_10);
        requestHttpData(Constants.Urls.URL_POST_FRIEND_LIST, request, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        goodFriendList.setOnRefreshComplete();
        ShareMemberPageEntity pageEntity = Parsers.getMemberPage(data);
        if (pageEntity != null) {
            List<ShareMemberEntity> shareMemberEntities = pageEntity.getMemberEntities();
            switch (requestCode) {
                case REQUEST_NET_ONE:
                    adapter = new ShareMemberAdapter(this, shareMemberEntities, this);
                    goodFriendList.setAdapter(adapter);
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        goodFriendList.setCanAddMore(true);
                    } else {
                        goodFriendList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_TWO:
                    adapter.addDatas(shareMemberEntities);
                    adapter.notifyDataSetChanged();
                    if (pageEntity.getTotalPage() > adapter.getPage()) {
                        goodFriendList.setCanAddMore(true);
                    } else {
                        goodFriendList.setCanAddMore(false);
                    }
                    break;
                case REQUEST_NET_THREE:
                    closeProgressDialog();
                    ShareEntity entity = Parsers.getShareInfo(data);
                    if (entity != null) {
                        title = entity.getTitle();
                        content = entity.getDesc();
                        img = entity.getImg();
                        qrCode = entity.getUrl();
                    }
                    break;
            }
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        super.mistake(requestCode, status, errorMessage);
        goodFriendList.setOnRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.good_friend_phone:
                sendSMS(title + qrCode);
                break;
            case R.id.good_friend_wx:
                CommonShareUtil.shareToWechat(this, content, title, img, qrCode);
                break;
            case R.id.item_share_member_user:
                ShareMemberEntity entity = (ShareMemberEntity) v.getTag();
                DialogUtils.showFriendDialog(this, entity.getHeadPic(), entity.getName(), entity.getRemuneration(), entity.getFriendCount());
                break;
        }
    }

    private void sendSMS(String smsBody) {
        //"smsto:xxx" xxx是可以指定联系人的
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        //"sms_body"必须一样，smsbody是发送短信内容content
        intent.putExtra("sms_body", smsBody);
        startActivity(intent);

    }
}
