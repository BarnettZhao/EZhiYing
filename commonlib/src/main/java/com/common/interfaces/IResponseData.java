package com.common.interfaces;

import com.common.network.FProtocol;

/**
 * @author songxudong
 */
public interface IResponseData {

	void resultDataSuccess(int requestCode, String data);

	void resultDataMistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus responseStatus, String errorMessage);
}
