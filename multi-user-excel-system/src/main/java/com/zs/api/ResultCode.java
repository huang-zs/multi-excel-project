package com.zs.api;
public enum ResultCode implements IErrorCode {
	//业务通过
    SUCCESS(200, "交易成功"),
    //业务不通过
    FAILED(250, "交易失败"),
    //业务异常
	WRONG(500,"交易报错");
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
