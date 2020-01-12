package com.zs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zs.entity.User;
import com.zs.mapper.UserMapper;
import com.zs.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	
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
	public int existEmail(String email) {
		// TODO Auto-generated method stub
		return userMapper.existEmail(email);
	}

	@Override
	public int create(User user) {
		// TODO Auto-generated method stub
		return userMapper.create(user);
	}
	

}
