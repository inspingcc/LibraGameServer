package com.insping.libra.sock.net.response;

import com.insping.common.utils.JsonUtil;

public class ReturnObject {
	private int code;
	private int status;
	private String data;

	public ReturnObject() {
		code = 200;
		status = 0;// 0 成功 1:失败
	}

	public void success(int code) {
		this.code = code;
		this.status = 0;
	}

	/**
	 * 成功,并设置data数据
	 * 
	 * @param data
	 */
	public void success(String data) {
		this.status = 0;
		this.data = data;

	}

	public void success(int code, String data) {
		this.code = code;
		this.status = 0;
		this.data = data;
	}

	public void fail(int code) {
		this.code = code;
		this.status = 1;
	}

	public void fail(String data) {
		this.code = 666;
		this.status = 1;
		this.data = data;
	}

	public void fail(int code, String data) {
		this.code = code;
		this.status = 1;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return JsonUtil.ObjectToJsonString(this);
	}

}
