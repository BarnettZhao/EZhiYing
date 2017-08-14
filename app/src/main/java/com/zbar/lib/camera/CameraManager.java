package com.zbar.lib.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 时间: 2014年5月9日 下午12:22:25
 * 版本: V_1.0.0
 * 描述: 相机管理
 */
public final class CameraManager {
	private static CameraManager cameraManager;

	private final CameraConfigurationManager configManager;
	private Camera camera;
	private boolean initialized;
	private boolean previewing;
	private final PreviewCallback previewCallback;
	private final AutoFocusCallback autoFocusCallback;
	private Parameters parameter;

	public static void init() {
		if (cameraManager == null) {
			cameraManager = new CameraManager();
		}
	}

	public static CameraManager get() {
		return cameraManager;
	}

	private CameraManager() {
		this.configManager = new CameraConfigurationManager();

		previewCallback = new PreviewCallback(configManager);
		autoFocusCallback = new AutoFocusCallback();
	}

	public void openDriver(SurfaceHolder holder, int width, int height) throws IOException {
		if (camera == null) {
			camera = Camera.open();//打开相机
			if (camera == null) {
				throw new IOException();
			}
			camera.setPreviewDisplay(holder);//设置预览显示的位置

			if (!initialized) {
				initialized = true;
				//初始化相机参数
				configManager.initFromCameraParameters(camera, width, height);
			}
			configManager.setDesiredCameraParameters(camera);
			FlashlightManager.enableFlashlight();//启用闪光灯
		}
	}

	public Point getCameraResolution() {
		return configManager.getCameraResolution();
	}

	public void closeDriver() {
		if (camera != null) {
			FlashlightManager.disableFlashlight();
			camera.release();
			camera = null;
		}
	}

	/**
	 * 开始预览
	 */
	public void startPreview() {
		if (camera != null && !previewing) {
			camera.startPreview();
			previewing = true;
		}
	}

	/**
	 * 停止预览，清空handler
	 */
	public void stopPreview() {
		if (camera != null && previewing) {
			camera.stopPreview();
			previewCallback.setHandler(null, 0);
			autoFocusCallback.setHandler(null, 0);
			previewing = false;
		}
	}

	/**
	 * 获取相机预览的每一帧图片
	 */
	public void requestPreviewFrame(Handler handler, int message) {
		if (camera != null && previewing) {
			//设置预览回调处理，onPreviewFrame方法中处理
			previewCallback.setHandler(handler, message);
			camera.setOneShotPreviewCallback(previewCallback);
		}
	}

	/**
	 * 自动获取焦点处理，预览状态下每隔1.5秒发送一次自动对焦消息
	 */
	public void requestAutoFocus(Handler handler, int message) {
		if (camera != null && previewing) {
			autoFocusCallback.setHandler(handler, message);
			camera.autoFocus(autoFocusCallback);
		}
	}

	public void openLight() {
		if (camera != null) {
			parameter = camera.getParameters();
			parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameter);
		}
	}

	public void offLight() {
		if (camera != null) {
			parameter = camera.getParameters();
			parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameter);
		}
	}
}
