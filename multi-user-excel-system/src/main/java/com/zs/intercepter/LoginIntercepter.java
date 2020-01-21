package com.zs.intercepter;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;
import com.zs.entity.User;
import com.zs.util.TokenUtil;

@Component
public class LoginIntercepter implements HandlerInterceptor {
	Logger logger = LoggerFactory.getLogger(LoginIntercepter.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		StringBuffer requestURL = request.getRequestURL();
		logger.debug(requestURI);
		logger.debug(requestURL.toString());
		logger.debug(request.getServletPath());
		logger.debug("拦截器");
		String token = request.getHeader("token");
		System.out.println("ip:"+InetAddress.getLocalHost().getHostAddress());
		System.out.println("ip:"+request.getLocalAddr());
//		if(StringUtils.isEmpty(token)) {
//			JSONObject jsonUser = TokenUtil.getJsonObjectByToken(token);
//			
//			
//			response.setStatus(401);
//			response.getWriter().append("你没有登录，请去登录");
//			return false;
//		}
	
		return true;
	}
	
	
}
