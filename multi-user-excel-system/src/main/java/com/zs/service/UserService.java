package com.zs.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zs.entity.User;

public interface UserService {
	/**
	 * 用户登录 
	 * @param user
	 * @return
	 */
	public User login(User user);

	public List<User> userList();

	/**
	 * 查询是否存在该邮箱
	 * @param email
	 * @return
	 */
	public boolean existEmail(String email);

	/**
	 * 发送邮箱验证码
	 * @param to 收件人
	 * @return 验证码
	 * @throws Exception
	 */
	public String sendVerificationCode(String to) throws Exception;

	/**
	 * 发送重置后的密码
	 * @param to 收件人
	 * @return 重置后的密码
	 * @throws Exception
	 */
	public String sendResetPassword(String to, String password) throws Exception;

	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public int create(User user);

	/**
	 * 获取图片验证码
	 * @param out 验证码输出流
	 * @return
	 * @throws IOException 
	 */
	public String getImageCode(OutputStream out) throws IOException;

	/**
	 * 重置密码
	 * @param email
	 * @return 重置后的密码
	 */
	public String resetPassword(String email);

	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public int update(User user);

	/**
	 * 用户id查用户信息
	 * @param id
	 * @return
	 */
	public User getById(String id);

	/**
	 * 发送用户建议到邮箱(含文件)
	 * @param map
	 * @param files
	 * @throws Exception 
	 */
	public void sendAdvice(Map<String, String> map, File[] files) throws Exception;

	/**
	 * 发送用户建议到邮箱(不含文件)
	 * @param json
	 * @throws Exception
	 */
	public void sendAdvice(JSONObject json) throws Exception;
}
