package com.zs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zs.api.CommonResult;
import com.zs.entity.User;
import com.zs.service.UserService;
import com.zs.util.AesUtil;
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
	@Value("${zs.tempFileDir}")
	private String tempFileDir;

//	@Autowired
//	private MailService mailService;

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public CommonResult login(@RequestBody JSONObject json) {
		logger.info("收到用户登录请求" + json);
		User user = json.toJavaObject(User.class);
		//解密密码
		try {
			user.setPassword(AesUtil.decrypt(user.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User existUser = userService.login(user);
		if (existUser != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("user", existUser);
			String token = TokenUtil.createToken(existUser, "123");
			jsonObject.put("token", token);
			logger.info("用户登录请求通过" + jsonObject);
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
		logger.info("收到用户注册请求" + json);
		String email = json.getString("email");
		// 判断该邮箱是否已经使用
		if (userService.existEmail(email)) {
			logger.info("邮箱[" + email + "已被使用");
			return CommonResult.failed("该邮箱已被使用");
		}
		// 获取验证码时存入的验证码缓存
		Object backCode = RedisUtil.get("emailCode-" + email);
		if (backCode == null) {
			logger.info("用户邮箱未获取验证码" + json);
			return CommonResult.failed("该邮箱未发送验证码");
		}
		String frontCode = json.getString("frontCode");
		if (!frontCode.equals(backCode)) {
			logger.info("用户邮箱验证码校验不通过，" + frontCode + "！=" + backCode);
			return CommonResult.failed("邮箱验证码不正确");
		}
		User user = json.toJavaObject(User.class);
		//解密密码
		try {
			user.setPassword(AesUtil.decrypt(user.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		logger.info("收到获取邮箱验证码请求" + json);

		// 获取前端送的email
		String email = json.getString("email");

		String code;
		try {
			code = userService.sendVerificationCode(email);
		} catch (Exception e) {
			logger.error("发送邮箱验证码失败");
			return CommonResult.failed("发送邮箱验证码失败,请校验邮箱合法性");
		}
		// 验证码放reids，存货时间15分钟
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
		logger.info("用户登出" + user);
		TokenUtil.destroyToken(getToken());
		return CommonResult.ok();
	}

	/**
	 * 获取验证码图片(废弃)
	 * @param json
	 * @param response
	 * @return
	 * @throws IOException
	 */
//	@GetMapping("/image/code/{email}")
//	public CommonResult getImageCode(@PathVariable("email") String email, HttpServletResponse response)
//			throws IOException {
//
//		try (ServletOutputStream out = response.getOutputStream()) {
//			String imageCode = userService.getImageCode(out);
//			RedisUtil.set("ImageCode-" + email, imageCode);
//			return CommonResult.ok(imageCode);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return CommonResult.failed("获取验证码失败");
//		}
//
//	}
	/**
	 * 重置密码为随机组合，发送密码邮件
	 * @param json
	 * @return
	 */
	@PostMapping("/resetPassword")
	public CommonResult resetPassword(@RequestBody JSONObject json) {
		logger.info("收到重置密码请求" + json);

		// 获取前端送的email
		String email = json.getString("email");
		if (!userService.existEmail(email)) {
			logger.info("收到重置密码请求不通过，不存在的邮箱");
			return CommonResult.failed("发送失败，不存在该邮箱对应的账号");
		}
		// 获取验证码时存入的验证码缓存
		Object backCode = RedisUtil.get("emailCode-" + email);
		if (backCode == null) {
			logger.info("用户邮箱未获取验证码" + json);
			return CommonResult.failed("该邮箱未发送验证码");
		}
		String frontCode = json.getString("frontCode");
		if (!frontCode.equals(backCode)) {
			logger.info("用户邮箱验证码校验不通过，" + frontCode + "！=" + backCode);
			return CommonResult.failed("邮箱验证码不正确");
		}

		String newPassword = userService.resetPassword(email);
		try {
			userService.sendResetPassword(email, newPassword);
		} catch (Exception e) {
			logger.error("发送邮箱验证码失败");
			return CommonResult.failed("发送重置密码邮件失败,请校验邮箱合法性");
		}

		logger.info("收到重置密码请求通过");
		return CommonResult.ok();
	}

	/**
	 * 修改用户信息
	 * @param json
	 * @return
	 */
	@PostMapping("/update")
	public CommonResult update(@RequestBody JSONObject json) {
		logger.info("收到修改用户信息请求" + json);
		if (json.containsKey("email")) {// 有email代表重新绑定
			String email = json.getString("email");
			// 判断该邮箱是否已经使用
			if (userService.existEmail(email)) {
				logger.info("邮箱[" + email + "已被使用");
				return CommonResult.failed("该邮箱已被使用");
			}
			// 获取验证码时存入的验证码缓存
			Object backCode = RedisUtil.get("emailCode-" + email);
			if (backCode == null) {
				logger.info("用户邮箱未获取验证码" + json);
				return CommonResult.failed("该邮箱未发送验证码");
			}
			String frontCode = json.getString("frontCode");
			if (!frontCode.equals(backCode)) {
				logger.info("用户邮箱验证码校验不通过，" + frontCode + "！=" + backCode);
				return CommonResult.failed("邮箱验证码不正确");
			}
			RedisUtil.del("emailCode-" + email);
		}
		User user = json.toJavaObject(User.class);
		//解密密码
		if(json.containsKey("password")) 
		{
			try {
				user.setPassword(AesUtil.decrypt(user.getPassword()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int i = userService.update(user);
		logger.info("修改用户信息请求通过");
		return CommonResult.ok();
	}

	@PostMapping("/fileAdvice")
	public CommonResult fileAdvice(@RequestParam Map<String, String> map,
			@RequestParam("file") MultipartFile[] multipartFiles) throws IllegalStateException, IOException {
		File[] files = new File[multipartFiles.length];
		File file = null;
		FileOutputStream outputStream  =null;
		LocalDateTime now = LocalDateTime.now();
		File fileDir = new File(tempFileDir +File.separator+"adviceFile"+ File.separator + now.getYear() + File.separator + now.getMonthValue()+ File.separator
				+ now.getDayOfMonth());
		if(!fileDir.exists()) fileDir.mkdirs();
		
		for (int i = 0; i < multipartFiles.length; i++) {
			file = new File(fileDir,multipartFiles[i].getOriginalFilename());
			 outputStream = new FileOutputStream(file);
			outputStream.write(multipartFiles[i].getBytes());
			files[i] = file;
		}
		try {
			userService.sendAdvice(map, files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CommonResult.ok();
	}

	@PostMapping("/messageAdvice")
	public CommonResult messageAdvice(@RequestBody JSONObject json) {
		try {
			userService.sendAdvice(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CommonResult.ok();
	}
}
