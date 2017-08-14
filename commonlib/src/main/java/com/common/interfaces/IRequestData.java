package com.common.interfaces;

import com.common.network.FProtocol;
import com.common.network.IResponseJudger;

import java.util.IdentityHashMap;

/**
 * @author songxudong
 */
public interface IRequestData {

	void requestHttpData(String path, int requestCode);

	void requestHttpData(String path, int requestCode, IResponseJudger judger);

	void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode);

	void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode, IResponseJudger judger);

	void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters);

	void requestHttpData(String path, int requestCode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters, IResponseJudger judger);

	void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters);

	void requestHttpData(String path, int requestCode, FProtocol.NetDataProtocol.DataMode dataAccessMode, FProtocol.HttpMethod method, IdentityHashMap<String, String> postParameters, IResponseJudger judger);
}
