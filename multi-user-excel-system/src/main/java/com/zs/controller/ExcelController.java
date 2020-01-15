package com.zs.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
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
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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
	 * {
	 * name: "tototo",
	 * fileDescribe: "123",
	 * json: "{"version":"13.0.3","customList":[],"sheets":{"Sheet1":{"name":"Sheet1","theme":"Office","data":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"rowHeaderData":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"colHeaderData":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"leftCellIndex":0,"topCellIndex":0,"selections":{"0":{"row":0,"rowCount":1,"col":0,"colCount":1},"length":1},"cellStates":{},"outlineColumnOptions":{},"autoMergeRangeInfos":[],"index":0}}}"
	 * }
	 * 
	 * @return
	 * {
	 * id: "dXNlcjIwMjAwMTE0MjI1OTM2LWV4Y2VsLTIwMjAwMTE1MDg1NDM2",
	 * name: "tototo",
	 * createDate: "2020-01-15",
	 * lastModifyDate: "2020-01-15 08:54:36",
	 * json: "{"version":"13.0.3","customList":[],"sheets":{"Sheet1":{"name":"Sheet1","theme":"Office","data":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"rowHeaderData":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"colHeaderData":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"leftCellIndex":0,"topCellIndex":0,"selections":{"0":{"row":0,"rowCount":1,"col":0,"colCount":1},"length":1},"cellStates":{},"outlineColumnOptions":{},"autoMergeRangeInfos":[],"index":0}}}"
	 * createrId: "user20200114225936",
	 * status: "",
	 * describe: null
	 * }
	 * 
	 */
	@PostMapping("/create")
	public CommonResult create(@RequestBody JSONObject json) {
		log.debug("excel create");
		LocalDateTime localDateTime = LocalDateTime.now();
		String createDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA).format(localDateTime);
		String lastModifyDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(localDateTime);
		// excel的创建时间为现在
		json.put("createDate", createDate);
		// excel的最后修改时间为现在
		json.put("lastModifyDate", lastModifyDate);
		String userId = getUser().getId();
		// 设置创建人id
		json.put("createrId", userId);
		String id = userId + "-excel-"
				+ DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINA).format(localDateTime);
		// base64加密后存进去
		String encodeId = Base64.getEncoder().encodeToString(id.getBytes());
		// 设置excel唯一id
		json.put("id", encodeId);
		Excel excel = json.toJavaObject(Excel.class);
		int i = excelService.create(excel);
		RedisUtil.set(encodeId, JSONObject.toJSONString(excel));

		return CommonResult.ok(excel);
	}

	/**
	 * 根据excelId获取Excel
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/get")
	public CommonResult get(@RequestBody JSONObject json) {
		String excelId = json.getString("excelId");
		Excel excel = null;
		// redis没有就去数据库拿
		if (RedisUtil.hasKey(excelId)) {
			String excelStr = (String) RedisUtil.get(excelId);
			excel = JSONObject.toJavaObject(JSONObject.parseObject(excelStr), Excel.class);
		} else {
			excel = excelService.get(excelId);
		}
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
		String excelId = json.getString("id");
		String excelStr = (String) RedisUtil.get(excelId);
		Excel excel = JSONObject.toJavaObject(JSONObject.parseObject(excelStr), Excel.class);
		excel.setJson(json.getString("json"));
		excelService.save(excel);
		RedisUtil.set(excel.getId(), JSONObject.toJSONString(excel));
		return CommonResult.ok();
	}

	/**
	 * 获取 用户 的excel列表
	 * 
	 * @param json 查询条件
	 * {
	 * name: "" 
	 * type: "all" // mine:自己的 ， other:协助的
	 * date: ["2020-01-10", "2020-02-03"] 
	 * pageSize: 2
	 * pageNum: 2 
	 * }
	 * 
	 * @return
	 */
	@PostMapping("/list")
	public CommonResult getExcelListByCreaterId(@RequestBody JSONObject json) {
		json.put("id", getUser().getId());
		PageInfo pageInfo = null;
		if (json.getString("type").equals("mine")) {// 查询本人
			pageInfo = excelService.getExcelListByCreaterId(json, json.getIntValue("pageNum"),
					json.getIntValue("pageSize"));
		} else {// 查询协助的excel
			pageInfo = excelService.getShareExcelListByUserId(json, json.getIntValue("pageNum"),
					json.getIntValue("pageSize"));
		}
		return CommonResult.ok(pageInfo);
	}

	/**
	 * 检查和绑定协助excel
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/assist")
	public CommonResult checkAndBindExcel(@RequestBody JSONObject json) {
		String excelId = json.getString("excelId");
		Excel excel = excelService.get(excelId);
		// 校验用户上送的邀请码对应的excel是否存在
		if (excel == null) {
			return CommonResult.failed("邀请码无效");
		}
		String userId = getUser().getId();
		// 未绑定的绑定
		if (!excelService.ifbindShareExcel(excelId, userId))
			excelService.bindShareExcel(excelId, userId);

		return CommonResult.ok();
	}

	@PostMapping("/delete")
	public CommonResult delete(@RequestBody JSONObject json) {
		String userId = getUser().getId();
		String excelId = json.getString("excelId");
		Excel excel = excelService.get(excelId);
		// 是否删除自己的excel
		if (userId.equals(excel.getCreaterId())) {
			// 删除excel
			excelService.delete(excelId);
		} else {
			// 解绑excel
			excelService.unbind(excelId, userId);
		}

		return CommonResult.ok();
	}

}
