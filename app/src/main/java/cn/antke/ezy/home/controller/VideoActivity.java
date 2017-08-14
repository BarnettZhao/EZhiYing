package cn.antke.ezy.home.controller;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.common.utils.LogUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import cn.antke.ezy.R;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/4/28.
 * 视频处理
 */
public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

	@ViewInject(R.id.vv_video_view)
	private VideoView vvVideoView;
	@ViewInject(R.id.b_choose_video)
	private View bChooseVideo;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_video);
		ViewInjectUtils.inject(this);

		bChooseVideo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
				//intent.setType("image/*");//选择图片
				// intent.setType("audio/*"); //选择音频
				intent.setType("video/*.mp4"); //选择视频 （mp4 3gp 是android支持的视频格式）
				// intent.setType("video/*;image/*");//同时选择视频和图片

				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				startActivityForResult(intent, 1);
			}
		});

		//本地的视频  需要在手机SD卡根目录添加一个 fl1234.mp4 视频
		String videoUrl1 = Environment.getExternalStorageDirectory().getPath() + "/fl1234.mp4";

		//设置视频控制器
		vvVideoView.setMediaController(new MediaController(this));
		//播放完成回调
		vvVideoView.setOnCompletionListener(this);
		//设置视频路径
		vvVideoView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
		//开始播放视频
		vvVideoView.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		ToastUtil.shortShow(this, "播放完成了");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 选取图片的返回值
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				cursor.moveToFirst();
				// String imgNo = cursor.getString(0); // 图片编号
				String v_path = cursor.getString(1); // 图片文件路径
				String v_size = cursor.getString(2); // 图片大小
				String v_name = cursor.getString(3); // 图片文件名
				LogUtil.e("TEST", "v_path=" + v_path);
				LogUtil.e("TEST", "v_size=" + v_size);
				LogUtil.e("TEST", "v_name=" + v_name);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
