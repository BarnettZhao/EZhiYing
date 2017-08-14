package com.zbar.lib.decode;

import android.app.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 时间: 2014年5月9日 下午12:25:12
 * 版本: V_1.0.0
 * 5分钟之后关闭扫描界面的activity
 */
public final class InactivityTimer {

	private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;

	//定时服务
	private final ScheduledExecutorService inactivityTimer = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
	private final Activity activity;
	private ScheduledFuture<?> inactivityFuture;//定时任务

	public InactivityTimer(Activity activity) {
		this.activity = activity;
		onActivity();
	}

	public void onActivity() {
		cancel();
		//执行定时任务，第一个参数是要执行的任务，第二个参数是延迟多久之后执行，第三个参数是时间单位
		inactivityFuture = inactivityTimer.schedule(new FinishListener(activity), INACTIVITY_DELAY_SECONDS, TimeUnit.SECONDS);
	}

	private void cancel() {
		if (inactivityFuture != null) {
			inactivityFuture.cancel(true);
			inactivityFuture = null;
		}
	}

	public void shutdown() {
		cancel();
		inactivityTimer.shutdown();
	}

	private static final class DaemonThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable runnable) {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);//设置为守护线程
			return thread;
		}
	}
}
