package com.zs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import net.bytebuddy.asm.Advice.Exit;

/**
 * 用户相关的controller
 * 
 * @author MagicBook
 *
 */

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
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
		User user = json.toJavaObject(User.class);
		User existUser = userService.login(user);
		if (existUser != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("user", existUser);
			String token = TokenUtil.createToken(existUser);
			jsonObject.put("token", token);
			return CommonResult.ok(jsonObject);
		} else {
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
		String email = json.getString("email");
		// 获取验证码时存入的验证码缓存
		Object backCode = RedisUtil.get("emailCode-" + email);
		if (backCode == null) {
			return CommonResult.failed("该邮箱未发送验证码");
		}
		if (!json.getString("frontCode").equals(backCode)) {
			return CommonResult.failed("邮箱验证码不正确");
		}
		User user = json.toJavaObject(User.class);
		int i = userService.create(user);
		RedisUtil.del("emailCode-" + email);
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
		String email = json.getString("email");
		// 判断该邮箱是否已经使用
		int i = userService.existEmail(email);
		if (i > 0) {
			return CommonResult.failed("该邮箱已被使用");
		}

		String code;
		try {
			code = mailService.sendVerificationCode(email);
		} catch (Exception e) {
			return CommonResult.failed(e.getMessage());
		}
		RedisUtil.set("emailCode-" + email, code);

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
		TokenUtil.destroyToken(getToken());
		return CommonResult.ok();
	}
}
