package com.common.network;


/**
 * @author songxudong
 */
public class FProtocol {

	public static enum HttpMethod {
		GET, POST, PUT, DELETE
	}

	// 网络请求协议变量
	public static class NetDataProtocol {
		/**
		 * 直接从网络取数据不需要本地存储
		 * DATA_FROM_NET_NO_CACHE
		 * 直接从网络取数据需要本地存储
		 * DATA_FROM_NET
		 * 直接从本地存储拿数据
		 * DATA_FROM_CACHE
		 * 先从本地存储取数据显示出来 再去网络取数据更新界面并本地存储
		 * DATA_UPDATE_CACHE
		 */
		public static enum DataMode {
			DATA_FROM_NET_NO_CACHE, DATA_FROM_NET, DATA_FROM_CACHE, DATA_UPDATE_CACHE
		}

		/**
		 * LOAD_SUCCESS 下载成功
		 * LOAD_NET_DISCONNENT 网络出错
		 * LOAD_CANCEL 取消
		 * LOAD_MISTAKE 解析出错
		 * LOAD_EXCEPTION 异常
		 */

		public static enum ResponseStatus {
			LOAD_SUCCESS, LOAD_NET_DISCONNENT, LOAD_CANCEL, LOAD_MISTAKE, LOAD_EXCEPTION
		}
	}

}
