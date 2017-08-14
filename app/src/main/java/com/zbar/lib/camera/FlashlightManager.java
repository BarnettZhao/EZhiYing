package com.zbar.lib.camera;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 时间: 2014年5月9日 下午12:22:42
 * 版本: V_1.0.0
 * 描述: 闪光灯管理
 */
final class FlashlightManager {

	private static final String TAG = FlashlightManager.class.getSimpleName();

	private static final Object iHardwareService;//硬件管理服务
	private static final Method setFlashEnabledMethod;//闪光灯启用禁用方法

	static {
		iHardwareService = getHardwareService();//获取硬件服务
		setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);
		if (iHardwareService == null) {
			Log.v(TAG, "This device does supports control of a flashlight");
		} else {
			Log.v(TAG, "This device does not support control of a flashlight");
		}
	}

	private FlashlightManager() {
	}

	private static Object getHardwareService() {
		//反射获取硬件服务管理类
		Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
		if (serviceManagerClass == null) {
			return null;
		}
		//反射获取‘获取服务’的方法
		Method getServiceMethod = maybeGetMethod(serviceManagerClass, "getService", String.class);
		if (getServiceMethod == null) {
			return null;
		}
		//调用‘获取服务’方法，得到硬件对象
		Object hardwareService = invoke(getServiceMethod, null, "hardware");
		if (hardwareService == null) {
			return null;
		}
		//反射获取硬件服务的接口类
		Class<?> iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub");
		if (iHardwareServiceStubClass == null) {
			return null;
		}
		//获取接口绑定方法
		Method asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass, "asInterface", IBinder.class);
		if (asInterfaceMethod == null) {
			return null;
		}
		//绑定硬件
		return invoke(asInterfaceMethod, null, hardwareService);
	}

	private static Method getSetFlashEnabledMethod(Object iHardwareService) {
		if (iHardwareService == null) {
			return null;
		}
		Class<?> proxyClass = iHardwareService.getClass();
		//通过硬件服务拿到管理闪光灯启用禁用的方法
		return maybeGetMethod(proxyClass, "setFlashlightEnabled", boolean.class);
	}

	private static Class<?> maybeForName(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException cnfe) {
			// OK
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while finding class " + name, re);
			return null;
		}
	}

	private static Method maybeGetMethod(Class<?> clazz, String name, Class<?>... argClasses) {
		try {
			return clazz.getMethod(name, argClasses);
		} catch (NoSuchMethodException nsme) {
			// OK
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while finding method " + name, re);
			return null;
		}
	}

	private static Object invoke(Method method, Object instance, Object... args) {
		try {
			return method.invoke(instance, args);
		} catch (IllegalAccessException e) {
			Log.w(TAG, "Unexpected error while invoking " + method, e);
			return null;
		} catch (InvocationTargetException e) {
			Log.w(TAG, "Unexpected error while invoking " + method, e.getCause());
			return null;
		} catch (RuntimeException re) {
			Log.w(TAG, "Unexpected error while invoking " + method, re);
			return null;
		}
	}

	static void enableFlashlight() {
		setFlashlight(true);
	}

	static void disableFlashlight() {
		setFlashlight(false);
	}

	/**
	 * @param active 启用或禁用闪光灯
	 */
	private static void setFlashlight(boolean active) {
		if (iHardwareService != null) {
			invoke(setFlashEnabledMethod, iHardwareService, active);
		}
	}
}
