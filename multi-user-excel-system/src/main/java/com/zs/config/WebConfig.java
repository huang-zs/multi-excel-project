package com.zs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zs.intercepter.LoginIntercepter;
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	private LoginIntercepter loginIntercepter;
@Override
public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(loginIntercepter)
	.addPathPatterns("/**")
	.excludePathPatterns("/user/**")
	.excludePathPatterns("/test/**")
	.excludePathPatterns("/door/**");
}
}
