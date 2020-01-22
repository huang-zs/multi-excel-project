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
		String token = request.getHeader("SecurtToken");
		System.out.println("ip:"+InetAddress.getLocalHost().getHostAddress());
		System.out.println("ip:"+request.getLocalAddr());
		System.out.println(request.getRemoteAddr());
		if(StringUtils.isEmpty(token)) {
			response.setStatus(401);
			response.setContentType("text/html;charset=UTF-8"); 
			response.getWriter().append("你没有登录，请去登录");
			logger.info("请求header缺少SecurtToken");
			return false;
		}else {
			JSONObject jsonUser = TokenUtil.getJsonObjectByToken(token);
			if(jsonUser==null) {
				response.setStatus(401);
				response.getWriter().append("登录失效，请重新登陆");
				logger.info("SecurtToken的redis缓存不存在");
				return false;
			}
		}
	
		return true;
	}
	
	
}
