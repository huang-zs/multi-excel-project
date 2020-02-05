package com.zs.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zs.entity.User;
import com.zs.mapper.UserMapper;
import com.zs.service.UserService;
import com.zs.util.CodeUtil;
import com.zs.util.MailUtil;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CodeUtil codeUtil;
	@Autowired
	private MailUtil mailUtil;
	@Override
	public User login(User user) {
		return userMapper.login(user);
	}

	@Override
	public List<User> userList() {
		// TODO Auto-generated method stub
		return userMapper.userList();
	}

	@Override
	public boolean existEmail(String email) {
		// TODO Auto-generated method stub
		return userMapper.existEmail(email)>0;
	}
	@Override
	public String sendVerificationCode(String to) throws Exception {
		// TODO Auto-generated method stub
		// 这里要生成随机验证码
		String code = UUID.randomUUID().toString();

		String subject = "小黄人邮箱验证码";
		String content = code + "，此验证码15分钟有效";
		mailUtil.sendSimpleMail(to, subject, content);

		return code;
	}

	@Override
	public String sendResetPassword(String to,String password) throws Exception {
		// TODO Auto-generated method stub
		String subject = "小黄人重置密码";
		String content = "重置后密码："+password+" ，请妥善保存好你的密码";
		mailUtil.sendSimpleMail(to, subject, content);
		return null;
	}

	@Override
	public int create(User user) {
		// TODO Auto-generated method stub
		return userMapper.create(user);
	}

	@Override
	public String getImageCode(OutputStream out) throws IOException {
		
		return codeUtil.getRandomCodeImage(out);
	}

	@Override
	public String resetPassword(String email) {
		//生成随机密码
		String newPassword = UUID.randomUUID().toString().substring(0, 8);
		userMapper.resetPassword(email,newPassword);
		
		return newPassword;
		
	}

	@Override
	public int update(User user) {
		return userMapper.update(user);
	}

	@Override
	public User getById(String id) {
		// TODO Auto-generated method stub
		return userMapper.getById(id);
	}

	@Override
	public void sendAdvice(Map<String, String> map, File[] files) throws Exception {
			String to="2601377840@qq.com";
			String subject="小黄人Excel用户意见";
			StringBuffer contentBuffer=new StringBuffer();
			contentBuffer.append("<html><body>");
			contentBuffer.append("用户: <input type='text' value='"+map.get("name")+"'>");
			contentBuffer.append("电话: <input type='text' value='"+map.get("telPhone")+"'>");
			contentBuffer.append("类型: <input type='text' value='"+map.get("type")+"'>");
			contentBuffer.append("<textarea rows='10' cols='30'>" +map.get("describe")+ "</textarea>");
			contentBuffer.append("</html></body>");
			String content=contentBuffer.toString();
			mailUtil.sendImageMail(to, subject, content, files);
			
	}

	@Override
	public void sendAdvice(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		String to="2601377840@qq.com";
		String subject="小黄人Excel用户意见";
		StringBuffer contentBuffer=new StringBuffer();
		contentBuffer.append("<html><body>");
		contentBuffer.append("用户: <input type='text' value='"+json.get("name")+"'>");
		contentBuffer.append("电话: <input type='text' value='"+json.get("telPhone")+"'>");
		contentBuffer.append("类型: <input type='text' value='"+json.get("type")+"'>");
		contentBuffer.append("<textarea rows='10' cols='30'>" +json.get("describe")+ "</textarea>");
		contentBuffer.append("</html></body>");
		String content=contentBuffer.toString();
		mailUtil.sendHtmlMail(to, subject, content);
	}
	

}
