package com.zs.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
	/**
	 * 用 登录用户生成 token
	 * 
	 * @return
	 */
	public static String createToken(User user,String ip) {
		String format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINA).format(LocalDateTime.now());
		UUID uuid = UUID.nameUUIDFromBytes((user.toString() + format).getBytes());
		String uuidStr=uuid.toString();
		JSONObject jsonUser=JSONObject.parseObject(JSONObject.toJSON(user).toString());
		jsonUser.put("ip",ip);
		RedisUtil.set(uuidStr, jsonUser.toString());
		logger.info("创建token["+uuidStr+","+user+"]");
		return uuidStr;
	}
	
	/**
	 * 取请求头里面的token获取用户对象
	 * @param token
	 * @return
	 */
	public static User getUserByToken(String token) {
		User user = JSONObject.toJavaObject(JSONObject.parseObject((String) RedisUtil.get(token)), User.class);
		logger.info("获取token["+token+","+user+"]");
		return user;
	}
	/**
	 * 获取token所对应的user JsonObject 含ip
	 * @param token
	 * @return
	 */
	public static JSONObject getJsonObjectByToken(String token) {
		JSONObject jsonUser = JSONObject.parseObject((String) RedisUtil.get(token));
		logger.info("获取token["+token+","+jsonUser+"]");
		return jsonUser;
	}
	
	
	public static void destroyToken(String token) {
		logger.info("删除token["+token+"]");
		RedisUtil.del(token);
	}
}
