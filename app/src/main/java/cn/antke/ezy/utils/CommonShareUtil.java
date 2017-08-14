package cn.antke.ezy.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.common.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ShareEntity;
import cn.antke.ezy.person.adapter.ShareGridAdapter;
import cn.antke.ezy.widget.dialogplus.DialogPlus;
import cn.antke.ezy.widget.dialogplus.GridHolder;
import cn.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Liu_ZhiChao on 2015/9/19 15:19.
 * 公共的分享工具
 */
public final class CommonShareUtil {

	private static List<ShareEntity> shareEntityList = new ArrayList<>();

	/**
	 * @param text     分享内容
	 * @param title    分享标题
	 * @param imgUrl   分享图片，所有平台都有的(可选)
	 * @param titleUrl 分享url，qq是titleUrl，微信和朋友圈是url，新浪没有(可选)
	 */
	public static void share(final Activity activity, final String text, final String title, final String imgUrl, final String titleUrl) {
		Resources resources = activity.getResources();
		//准备分享平台
		if (0 == shareEntityList.size()) {
			shareEntityList.add(new ShareEntity(activity.getString(R.string.person_friend_share_qq), resources.getDrawable(R.drawable.share_qq_friend)));
			shareEntityList.add(new ShareEntity(activity.getString(R.string.person_friend_share_wx), resources.getDrawable(R.drawable.share_wx_friend)));
//			shareEntityList.add(new ShareEntity(activity.getString(R.string.person_friend_share_circle), resources.getDrawable(R.drawable.share_wx_circle)));
			shareEntityList.add(new ShareEntity(activity.getString(R.string.person_friend_share_sina), resources.getDrawable(R.drawable.share_sina)));
			shareEntityList.add(new ShareEntity(activity.getString(R.string.person_friend_share_sms), resources.getDrawable(R.drawable.share_sms)));
		}

		TextView textView = new TextView(activity);
		textView.setText("分享到");
		textView.setPadding(0, DeviceUtil.dp_to_px(activity, 15), 0, DeviceUtil.dp_to_px(activity, 20));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextColor(resources.getColor(R.color.middle_gray));
		textView.setBackgroundColor(Color.WHITE);
		DialogPlus dialogPlus = DialogPlus.newDialog(activity).setAdapter(new ShareGridAdapter(activity, shareEntityList))
				.setOnItemClickListener((dialog, item, view, position) -> showShare(dialog, activity, position, text, title, imgUrl, titleUrl))
				.setHeader(textView).setContentHolder(new GridHolder(4)).create();
		dialogPlus.show();
	}

	public static void shareToWechat(final Activity activity, final String text, final String title, final String imgUrl, final String titleUrl) {
		showShare(null, activity, 1, text, title, imgUrl, titleUrl);
	}

	private static void showShare(DialogPlus dialog, Activity activity, int position, String text, String title, String imgUrl, String titleUrl) {
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();
		switch (position) {
			case 0:
				oks.setPlatform(QQ.NAME);
				break;
			case 1:
				oks.setPlatform(Wechat.NAME);
				break;
			case 2:
				oks.setPlatform(SinaWeibo.NAME);
				break;
			case 3://短信
				String smsBody = title + titleUrl;
				Uri smsToUri = Uri.parse("smsto:");
				Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
				//sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
				sendIntent.putExtra("sms_body", smsBody);
				sendIntent.setType("vnd.android-dir/mms-sms");
				activity.startActivity(sendIntent);
				if (dialog != null) {
					dialog.dismiss();
				}
				return;
		}
		// title标题
		if (TextUtils.isEmpty(title)) {
			title = activity.getResources().getString(R.string.app_name);
		}
		oks.setTitle(title);
		if (TextUtils.isEmpty(text)) {
			text = "暂时没有分享内容";
		}
		// text是分享文本，所有平台都需要这个字段
		oks.setText(text == null ? "" : text);

		oks.setSilent(true);//隐藏编辑页面

		if (!TextUtils.isEmpty(imgUrl)) {
			oks.setImageUrl(imgUrl);
		} else {
			oks.setImageUrl("http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg");
		}
		if (TextUtils.isEmpty(titleUrl)) {
			//QQ分享必须得要
			titleUrl = "http://www.baidu.com";
		}
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(titleUrl);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(titleUrl);
		if (position == 3) {//微博分享无法获取网页连接需要拼接在内容后面
			text = text == null ? "" + titleUrl : text + titleUrl;
			oks.setText(text);
		}
		oks.show(activity);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
