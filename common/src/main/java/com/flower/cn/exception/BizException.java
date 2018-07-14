package com.flower.cn.exception;

import org.springframework.http.HttpStatus;

public class BizException extends PlatformException {
    private static final long serialVersionUID = 1L;

    public BizException() {
    }

    public BizException(int code, String message) {
        super(code, message);
    }

    public BizException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public BizException(int code, String message, Throwable ex) {
        super(code, message, ex);
    }

    public BizException(String message, Throwable ex) {
        super(message, ex);
    }

    public BizException(Throwable ex) {
        super(ex);
    }
}
