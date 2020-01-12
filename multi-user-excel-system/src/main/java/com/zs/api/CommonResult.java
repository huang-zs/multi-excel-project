package com.zs.api;

public class CommonResult<T> {
	private long code;
	private String msg;
	//返回数据
	private T data;
	
	
	
	public CommonResult() {
	}
	public CommonResult(long code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public static <T> CommonResult<T> ok(String msg,T data){
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(),msg,data);
	}
	public static <T> CommonResult<T> ok(T data){
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),data);
	}
	public static <T> CommonResult<T> ok(){
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),null);
	}
	public static <T> CommonResult<T> failed(String msg){
		return new CommonResult<T>(ResultCode.FAILED.getCode(),msg,null);
	}
	public static <T> CommonResult<T> failed(){
		return new CommonResult<T>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMessage(),null);
	}
	
	
	public long getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
	
	
}
