package com.zs.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.zs.service.MailService;
import com.zs.util.MailUtil;

@Service
public class MailServiceImpl implements MailService {
	@Autowired
	private MailUtil mailUtil;
	
	@Override
	public String sendVerificationCode(String to) throws Exception {
		// TODO Auto-generated method stub
		// 这里要生成随机验证码
		String code = UUID.randomUUID().toString();
		

		String subject = "小黄人邮箱验证码";
		String content = code+"，此验证码15分钟有效";
		try {
			mailUtil.sendSimpleMail(to, subject, content);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return code;
	}

}
