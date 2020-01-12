package com.zs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.zs.entity.User;
import com.zs.util.TokenUtil;

@RestController
public class BaseController {
	@Autowired
	private HttpServletRequest request;
	
	public User getUser() {
		String token = request.getHeader("SecurtToken");
		User user = TokenUtil.getUserByToken(token);
		return user;
	}
	public String getToken() {
		String token = request.getHeader("SecurtToken");
		return token;
	}
}
