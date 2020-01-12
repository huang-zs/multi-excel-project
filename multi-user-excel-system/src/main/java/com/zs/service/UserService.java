package com.zs.service;

import java.util.List;

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
	public int existEmail(String email);
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public int create(User user); 
}
