package com.zs.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zs.entity.User;

/**
 * token的工具类
 * 
 * @author MagicBook
 *
 */
@Component
public class TokenUtil {
	/**
	 * 用 登录用户生成 token
	 * 
	 * @return
	 */
	public static String createToken(User user) {
		String format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINA).format(LocalDateTime.now());
		UUID uuid = UUID.nameUUIDFromBytes((user.toString() + format).getBytes());
		String uuidStr=uuid.toString();
		RedisUtil.set(uuidStr, JSONObject.toJSONString(user));
		return uuidStr;
	}
	
	/**
	 * 取请求头里面的token获取用户对象
	 * @param token
	 * @return
	 */
	public static User getUserByToken(String token) {
		User user = JSONObject.toJavaObject(JSONObject.parseObject((String) RedisUtil.get(token)), User.class);
		return user;
	}
	
	public static void destroyToken(String token) {
		RedisUtil.del(token);
	}
}
