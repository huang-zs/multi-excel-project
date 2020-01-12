package com.zs.service;

public interface MailService {
	/**
	 * 发送邮箱验证码
	 * @param to 收件人
	 * @return 验证码
	 * @throws Exception
	 */
	public String sendVerificationCode (String to) throws Exception;
}