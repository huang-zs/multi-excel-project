package com.zs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zs.api.CommonResult;
import com.zs.entity.User;
import com.zs.service.MailService;
import com.zs.service.UserService;
import com.zs.util.RedisUtil;
import com.zs.util.TokenUtil;


/**
 * 用户相关的controller
 * 
 * @author MagicBook
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public CommonResult login(@RequestBody JSONObject json) {
		logger.info("收到用户登录请求"+json);
		User user = json.toJavaObject(User.class);
		User existUser = userService.login(user);
		if (existUser != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("user", existUser);
			String token = TokenUtil.createToken(existUser,"123");
			jsonObject.put("token", token);
			logger.info("用户登录请求通过"+jsonObject);
			return CommonResult.ok(jsonObject);
		} else {
			logger.info("用户登录请求校验失败");
			return CommonResult.failed("用户名或者密码有误");
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @param code
	 * @return
	 */
	@PostMapping("/create")
	public CommonResult create(@RequestBody JSONObject json) {
		logger.info("收到用户注册请求"+json);
		String email = json.getString("email");
		// 获取验证码时存入的验证码缓存
		Object backCode = RedisUtil.get("emailCode-" + email);
		if (backCode == null) {
			logger.info("用户邮箱未获取验证码"+json);
			return CommonResult.failed("该邮箱未发送验证码");
		}
		String frontCode=json.getString("frontCode");
		if (!frontCode.equals(backCode)) {
			logger.info("用户邮箱验证码校验不通过，"+frontCode+"！="+backCode);
			return CommonResult.failed("邮箱验证码不正确");
		}
		User user = json.toJavaObject(User.class);
		int i = userService.create(user);
		RedisUtil.del("emailCode-" + email);
		logger.info("用户注册请求通过");
		return CommonResult.ok();

	}

	/**
	 * 判断邮箱是否已经使用，是返回报错，否发送邮箱验证码
	 * 
	 * @param to
	 * @return
	 */
	@PostMapping("/mail/code")
	public CommonResult getCode(@RequestBody JSONObject json) {
		logger.info("收到获取邮箱验证码请求"+json);
		String email = json.getString("email");
		// 判断该邮箱是否已经使用
		int i = userService.existEmail(email);
		if (i > 0) {
			logger.info("邮箱["+email+"已被使用");
			return CommonResult.failed("该邮箱已被使用");
		}
		
		String code;
		try {
			code = mailService.sendVerificationCode(email);
		} catch (Exception e) {
			logger.error("发送邮箱验证码失败");
			return CommonResult.failed("发送邮箱验证码失败,请校验邮箱合法性");
		}
		//验证码放reids，存货时间15分钟
		RedisUtil.set("emailCode-" + email, code, 900);
		logger.info("用户获取邮箱验证码请求通过");
		return CommonResult.ok();
	}

	/**
	 * 用户登出
	 * 
	 * @param email
	 * @return
	 */
	@PostMapping("/logout")
	public CommonResult logout() {
		User user = getUser();
		logger.info("用户登出"+user);
		TokenUtil.destroyToken(getToken());
		return CommonResult.ok();
	}
}
