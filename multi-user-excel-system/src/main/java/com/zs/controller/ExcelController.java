package com.zs.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zs.api.CommonResult;
import com.zs.entity.Excel;
import com.zs.entity.User;
import com.zs.service.ExcelService;
import com.zs.util.RedisUtil;

@RestController
@RequestMapping("/excel")
public class ExcelController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(ExcelController.class);

	@Autowired
	private ExcelService excelService;

	/**
	 * 新建excel
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/create")
	public CommonResult create(@RequestBody JSONObject json) {
		log.debug("excel create");
		LocalDateTime localDateTime = LocalDateTime.now();
		String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(localDateTime);
		// excel的创建时间为现在
		json.put("createDate", now);
		// excel的最后修改时间为现在
		json.put("lastModifyDate", now);
		String userId = getUser().getId();
		// 设置创建人id
		json.put("createrId", userId);
		String id = userId + "-excel-"
				+ DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINA).format(localDateTime);
		// 设置excel唯一id
		json.put("id", id);
		Excel excel = json.toJavaObject(Excel.class);
		int i = excelService.create(excel);
		// 这里获取的json是String类型，前端转完送过来的
		RedisUtil.set(id, json.get("json"));

		return CommonResult.ok(id);
	}

	/**
	 * 根据excelId获取Excel
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/get")
	public CommonResult get(@RequestBody JSONObject json) {
		Excel excel = excelService.get(json.getString("excelId"));
		return CommonResult.ok(excel);
	}

	/**
	 * 保存excel (redis，数据库)
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/save")
	public CommonResult save(@RequestBody JSONObject json) {
		Excel excel = JSONObject.toJavaObject(json, Excel.class);
		excelService.save(excel);
		RedisUtil.set(excel.getId(), excel.getJson());
		return CommonResult.ok();
	}

	/**
	 * 获取 用户 自己创建的excel列表
	 * 
	 * @param json 查询条件
	 * @return
	 */
	@PostMapping("/list")
	public CommonResult getExcelListById(@RequestBody JSONObject json) {
		User user = getUser();
		List<Excel> list = excelService.getExcelListById(user.getId());
		return CommonResult.ok(list);
	}

	/**
	 * 获取用户 与其他用户共享的excel列表
	 * 
	 * @param json 查询条件
	 * @return
	 */
	@PostMapping("/shareList")
	public CommonResult getShareExcelListById(@RequestBody JSONObject json) {
		return CommonResult.ok();
	}
}
