package cn.antke.ezy.home.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

import java.io.IOException;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseActivity;

/**
 * Created by liuzhichao on 2016/12/16.
 * 扫一扫
 */
public class ScanActivity extends BaseActivity implements SurfaceHolder.Callback {

	private static final long VIBRATE_DURATION = 200L;//震动时长
	private static final float BEEP_VOLUME = 0.50f;//音量

	private InactivityTimer inactivityTimer;
	private RelativeLayout mContainer;//全布局
	private RelativeLayout mCropLayout;//裁剪范围
	private SurfaceView surfaceView;//相机显示范围
	private MediaPlayer mediaPlayer;//媒体播放器，播放扫码成功后的声音
	private CaptureActivityHandler handler;//扫描信息处理
	private boolean hasSurface;
	private boolean flag = true;//闪光灯
	private boolean playBeep;//播放声音
	private boolean vibrate = false;//震动
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private boolean isNeedCapture;//是否需要截图，不截图也可以正常扫码

	public static void startScanActivity(Context context) {
		Intent intent = new Intent(context, ScanActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 音频播放结束回调
	 */
	private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_scan);
		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		surfaceView = (SurfaceView) findViewById(R.id.capture_preview);

		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.95f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
		// 初始化 CameraManager
		CameraManager.init();
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			//第一次时执行，通过回调将hasSurface修改为true
			surfaceHolder.addCallback(this);
		}
		playBeep = true;
		//AudioManager类提供访问音量和振铃模式的控制。
		AudioManager audioService = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		// RINGER_MODE_NORMAL 振铃模式可以是听得见的或振动的。
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();//初始化声音播放
		vibrate = true;//震动
	}

	@Override
	public void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	/**
	 * 初始化声音播放
	 */
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepareAsync();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(surfaceHolder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		hasSurface = false;
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			int rootWidth = mContainer.getWidth();
			int rootHeight = mContainer.getHeight();
			//打开相机，并设置预览宽高(不是最终的，会选一个最接近系统预置的预览尺寸)
			CameraManager.get().openDriver(surfaceHolder, rootWidth, rootHeight);

			//拿到相机分辨率，计算屏幕截图范围
			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / rootWidth;
			int y = mCropLayout.getTop() * height / rootHeight;

			int cropWidth = mCropLayout.getWidth() * width / rootWidth;
			int cropHeight = mCropLayout.getHeight() * height / rootHeight;

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);
		} catch (IOException | RuntimeException ioe) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.msg_camera_framework_bug));
			builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			builder.show();
			return;
		}
		if (handler == null) {
			//开启相机预览，解码
			handler = new CaptureActivityHandler(this);
		}
	}

	/**
	 * 管理闪光灯
	 */
	protected void light() {
		if (flag) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}
	}

	/**
	 * 扫描结果处理
	 */
	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		if (TextUtils.isEmpty(result)) {
			ToastUtil.shortShow(this, "无效二维码");
			// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
			handler.sendEmptyMessage(R.id.restart_preview);
		} else {
			ToastUtil.shortShow(this, result);
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		ToastUtil.shortShow(this, errorMessage);
	}

	/**
	 * 播放声音和震动
	 */
	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean needCapture) {
		isNeedCapture = needCapture;
	}

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
}
