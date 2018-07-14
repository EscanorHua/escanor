package com.flower.cn.exception;

import lombok.Data;

/**
 * Created by zjs on 2018/3/6.
 */
@Data
public class TradeException extends BizException {


	private String msg;

	public TradeException(ExceptionEnum exceptionEnum) {
		super(exceptionEnum.code(), exceptionEnum.msg());
		this.code = exceptionEnum.code();
		this.msg = exceptionEnum.msg();
	}
	
	public TradeException(ExceptionEnum exceptionEnum, String appendMsg) {
		super(exceptionEnum.code(), appendMsg);
		this.code = exceptionEnum.code();
		this.msg = appendMsg;
	}

	public TradeException(ExceptionEnum exceptionEnum, String msg, Object data) {
		super(exceptionEnum.code(), msg);
	}
	
	@Deprecated
	public TradeException appendMsg(String msg) {
		this.msg = this.msg + msg;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
