package com.zs.util;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.zs.controller.UserController;

/**
 * redis工具类
 * @author MagicBook
 *
 */
@Component
public final class RedisUtil {
	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	private static RedisTemplate<String, Object> template;
	@Autowired
	private RedisTemplate<String, Object> autowiredTemplate;

	@PostConstruct
	public void init() {
		logger.info("redis初始化");
		template = autowiredTemplate;
	}

	/**
	 * 新建缓存
	 * @param key
	 * @param value
	 */
	public static void set(String key, Object value) {
		logger.info("设置缓存["+key+","+value+"]");
		template.opsForValue().set(key, value);
	}

	/**
	 * 新建缓存并设置存在时间
	 * @param key
	 * @param value
	 * @param timeOut  时间（秒）
	 */
	public static void set(String key, Object value, long timeOut) {
		logger.info("设置"+timeOut+"s缓存["+key+","+value+"]");
		template.opsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
	}

	/**
	 * 校验缓存
	 * @param key
	 * @return
	 */
	public static boolean hasKey(String key) {
		Boolean hasKey = template.hasKey(key);
		logger.info("缓存["+key+"]校验结果:"+hasKey);
		return hasKey;
	}

	/**
	 * 删除单个缓存
	 * @param key 
	 */
	public static void del(String key) {
		logger.info("删除缓存["+key+"]");
		template.delete(key);
	}

	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Object object = template.opsForValue().get(key);
		logger.info("获取缓存["+key+","+object+"]");
		return object;
	}
}
