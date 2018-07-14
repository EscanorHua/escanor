package com.flower.cn.exception;

public enum ExceptionEnum {

	IO_ERROR(202, "io流错误"),
	SERVER_ERROR(999, "服务器错误"),
	CONNECTION_ERROR(300, "连接错误"),
	RESULT_CONVERT_ERROR(301,"结果解析错误"),
    CHANNEL_ERROR(302, "查询渠道错误"),
    QUERY_NOT_SUPPORT(303, "渠道暂未支持该查询"),
    LOGIN_ERROR(304, "登陆错误"),
    AUTHORIZATION_ERROR(305, "授权错误"),
    ARGUMENT_ERROR(306, "参数错误"),
	INTERFACE_QUERY_ERROR(307, "接口查询错误"),
	DATATOBASE_ERROR(308,"数据入库错误"),
    COMMON_TRADE_ERROR(999999, "系统异常");





	private final int code;

	private final String msg;

	public int code() {
		return this.code;
	}

	public String msg() {
		return this.msg;
	}

	ExceptionEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
