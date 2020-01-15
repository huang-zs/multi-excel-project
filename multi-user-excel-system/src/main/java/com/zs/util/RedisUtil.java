package com.zs.util;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
/**
 * redis工具类
 * @author MagicBook
 *
 */
@Component
public final class RedisUtil {
	
private static RedisTemplate<String, Object> template;
@Autowired
private RedisTemplate<String, Object> autowiredTemplate;

@PostConstruct
public void init() {
	template = autowiredTemplate;
}

/**
 * 新建缓存
 * @param key
 * @param value
 */
public static void set(String key,Object value) {
	template.opsForValue().set(key, value);
}
/**
 * 新建缓存并设置存在时间
 * @param key
 * @param value
 * @param timeOut  时间（秒）
 */
public void set(String key,Object value,long timeOut) {
	template.opsForValue().set(key, value,timeOut,TimeUnit.SECONDS);
}

/**
 * 校验缓存
 * @param key
 * @return
 */
public static boolean hasKey(String key) {
	return template.hasKey(key);
}
/**
 * 删除单个缓存
 * @param key 
 */
public static void  del(String key) {
	template.delete(key);
}	
/**
 * 获取缓存
 * @param key
 * @return
 */
public static Object get(String key) {
	return template.opsForValue().get(key);
}
}
