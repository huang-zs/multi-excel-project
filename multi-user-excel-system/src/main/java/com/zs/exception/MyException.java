package com.zs.exception;
/**
 * 自定义的异常类
 * @author MagicBook
 *
 */
public class MyException extends RuntimeException {

	private String errorMsg;

	public MyException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
