package com.zs.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 邮件工具类
 * @author MagicBook
 *
 */
@Component
public class MailUtil {
	@Autowired
	private JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String from;

	private final Logger logger = LoggerFactory.getLogger(MailUtil.class);

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
			logger.info("发送邮件to:" + to);
		} catch (MailException e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	/**
	 * 发送html邮件
	 * @param to 收件人
	 * @param subject 标题
	 * @param content 内容
	 * @throws Exception 
	 */
	public void sendHtmlMail(String to, String subject, String content) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		try {
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content, true);

			mailSender.send(mimeMessage);
		} catch (MailException e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		} catch (MessagingException e) {
			logger.error(e.getMessage());
			throw new MessagingException();
		}

	}
	/**
	 * 发送带附件的html文件
	 * @param to
	 * @param subject
	 * @param content
	 * @param files
	 * @throws MessagingException
	 */
	public void sendImageMail(String to, String subject, String content, File... files) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
	
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content, true);

			for (File file : files) {
				mimeMessageHelper.addAttachment(file.getName(), file);
			}
			
			mailSender.send(mimeMessage);
		
		
	}
	
//	public void sendImageMail(String to, String subject, String content, File... files) throws MessagingException {
//		MimeMessage mimeMessage = mailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//
//		mimeMessageHelper.setFrom(from);
//		mimeMessageHelper.setTo(to);
//		mimeMessageHelper.setSubject(subject);
//
//		StringBuffer contentBuffer = new StringBuffer();
//		contentBuffer.append("<html><body>小黄人邮箱验证：");
//
//		for (File file : files) {
//			String name = file.getName();
//			contentBuffer.append("<img src=\'cid:" + name.substring(0,name.lastIndexOf(".")) + "\' >");
//			mimeMessageHelper.addInline(name, file);
//		}
//		contentBuffer.append("</body></html>");
//		logger.debug(contentBuffer.toString());
//		mimeMessageHelper.setText(contentBuffer.toString(), true);
//
//		mailSender.send(mimeMessage);
//
//	}
//	public void test(String to, String subject) throws MessagingException {
//		MimeMessage mimeMessage = mailSender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
//		
//			mimeMessageHelper.setFrom(from);
//			mimeMessageHelper.setTo(to);
//			mimeMessageHelper.setSubject(subject);
//			
//			String imgId="neo006";
//			String content = "<html><body>"  + "<img src=\'cid:" + imgId + "\'/>" + "</body></html>";
//			
//			File file = new File("F:\\Code\\excels\\code.PNG");
//			
//			mimeMessageHelper.setText(content, true);
//			mimeMessageHelper.addInline(imgId, file);
//			mimeMessageHelper.setCc(from);
//			mimeMessageHelper.setBcc(from);
//			mailSender.send(mimeMessage);
//	}
	
}
