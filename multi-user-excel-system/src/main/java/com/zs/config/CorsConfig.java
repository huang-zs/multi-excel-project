package com.zs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 解决跨域问题
 * @author MagicBook
 *
 */
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class CorsConfig {
private CorsConfiguration buildConfig() {
	CorsConfiguration corsConfiguration = new CorsConfiguration();
	corsConfiguration.addAllowedHeader("*");
	corsConfiguration.addAllowedMethod("*");
	corsConfiguration.addAllowedOrigin("*");
	return corsConfiguration;
}
@Bean
public CorsFilter corsFilter() {
	UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
	urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", buildConfig());
	return new CorsFilter(urlBasedCorsConfigurationSource);
}
}
