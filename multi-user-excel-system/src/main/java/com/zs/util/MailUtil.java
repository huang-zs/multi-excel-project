package com.zs.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
/**
 * 邮件工具类
 * @author MagicBook
 *
 */
@Component
public class MailUtil {
	@Autowired
	private MailSender mailSender;
	@Value("${spring.mail.username}")
	private String from;
	/**
	 * 发送简单文本邮件
	 * @param to 收件人
	 * @param subject 标题
	 * @param content 内容
	 * @throws Exception 
	 */
	public void sendSimpleMail(String to, String subject, String content) throws Exception {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setText(content);
		message.setSubject(subject);
		message.setTo(to);
		try {
			mailSender.send(message);
		} catch (MailException e) {
			throw new Exception("邮件发送失败");
		}
	}
}
