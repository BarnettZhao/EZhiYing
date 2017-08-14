package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.zbar.lib.camera.CameraManager;

import cn.antke.ezy.R;
import cn.antke.ezy.home.controller.ScanActivity;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 时间: 2014年5月9日 下午12:23:32
 * 版本: V_1.0.0
 * 描述: 扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {

	DecodeThread decodeThread;
	ScanActivity fragment;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureActivityHandler(ScanActivity fragment) {
		this.fragment = fragment;
		decodeThread = new DecodeThread(fragment);
		decodeThread.start();
		state = State.SUCCESS;
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
			case R.id.auto_focus://自动对焦
				if (state == State.PREVIEW) {
					CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
				}
				break;
			case R.id.restart_preview://重新预览，扫描
				restartPreviewAndDecode();
				break;
			case R.id.decode_succeeded://解码成功
				state = State.SUCCESS;
				fragment.handleDecode((String) message.obj);// 解析成功，回调
				break;
			case R.id.decode_failed://解码失败
				state = State.PREVIEW;
				CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
				break;
		}
	}

	/**
	 * 停止相机预览，停止处理消息，清空消息
	 */
	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
		removeMessages(R.id.decode);
		removeMessages(R.id.auto_focus);
	}

	/**
	 * 重新开始预览和解码
	 */
	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			//通过DecodeHandler来解码
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
			//自动对焦
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
		}
	}
}
