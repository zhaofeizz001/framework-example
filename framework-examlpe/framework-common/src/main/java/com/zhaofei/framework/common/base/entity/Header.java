package com.zhaofei.framework.common.base.entity;

public class Header {
	/**
	 * 消息结果编码
	 */
	public String retCode = null;
	
	/**
	 * 消息结果
	 */
	public String retMsg = null;
	public Header(){
		
	}
	public Header(String retCode,String retMsg){
		this.retCode = retCode;
		this.retMsg = retMsg;
	}
	public Header(String retCode){
		this.retCode = retCode;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
}
