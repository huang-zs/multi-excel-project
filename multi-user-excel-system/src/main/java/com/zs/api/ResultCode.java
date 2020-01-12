package com.zs.api;
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "交易成功"),
    FAILED(250, "交易失败");
    private long code;
    private String message;
 
    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
 
    public long getCode() {
        return code;
    }
 
    public String getMessage() {
        return message;
    }
}
