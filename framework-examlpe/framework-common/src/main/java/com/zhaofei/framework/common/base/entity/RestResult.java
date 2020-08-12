package com.zhaofei.framework.common.base.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RestResult<T> {
	/**
	 * 消息头部
	 */
	private Header header;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	/** 消息体 */
	private T body;

	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}


	public String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private RestResult() {
	}

	public static <T> RestResult<T> newInstance() {
		return new RestResult<>();
	}

	public RestResult(String retCode, T data, String message) {
		Header header = new Header(retCode, message);
		this.setHeader(header);
		this.setBody(data);
		this.time = System.currentTimeMillis() + "";
	}

	public RestResult(String retCode, String message) {
		Header header = new Header(retCode, message);
		this.setHeader(header);
		this.time = System.currentTimeMillis() + "";
	}

	@Override
	public String toString() {
		return "RestResult{" + "result=" + ", message='" + '\'' + ", data=" + '}';
	}
}