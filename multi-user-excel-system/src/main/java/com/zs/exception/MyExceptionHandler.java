package com.zs.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zs.api.CommonResult;

/**
 * 异常捕获处理类
 * @author MagicBook
 *
 */
@ControllerAdvice
public class MyExceptionHandler {
	Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);
	/**
	 * 捕获全局异常处理
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public CommonResult errorHandler(Exception ex) {
		logger.error("收到全局异常");
		StackTraceElement stackTraceElement = ex.getStackTrace()[0];
		String exclass = stackTraceElement.getClassName();
		String method = stackTraceElement.getMethodName();
		logger.error(new Date() + ":" + "[类:" + exclass + "]调用"
		+ method + "时在第" + stackTraceElement.getLineNumber()
		+ "行代码处发生异常!异常类型:" + ex.getClass().getName()+":"+ex.getMessage());
		return CommonResult.wrong(ex.getMessage());
	}
	/**
	 * 捕获自定义异常
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(MyException.class)
	public CommonResult myErrorHandler(Exception ex) {
		return CommonResult.failed(ex.getMessage());
	}
}
