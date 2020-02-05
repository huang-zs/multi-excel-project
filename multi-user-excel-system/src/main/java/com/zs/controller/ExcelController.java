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

	private  final Logger logger = LoggerFactory.getLogger(ExcelController.class);
	//excel模板
	private final String excelJsonModel="{\"version\":\"13.0.3\",\"customList\":[],\"sheets\":{\"Sheet1\":{\"name\":\"Sheet1\",\"theme\":\"Office\",\"data\":{\"defaultDataNode\":{\"style\":{\"themeFont\":\"Body\"}}},\"rowHeaderData\":{\"defaultDataNode\":{\"style\":{\"themeFont\":\"Body\"}}},\"colHeaderData\":{\"defaultDataNode\":{\"style\":{\"themeFont\":\"Body\"}}},\"leftCellIndex\":0,\"topCellIndex\":0,\"selections\":{\"0\":{\"row\":0,\"rowCount\":1,\"col\":0,\"colCount\":1},\"length\":1},\"cellStates\":{},\"outlineColumnOptions\":{},\"autoMergeRangeInfos\":[],\"index\":0}}}";

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
		logger.info("收到新建excel请求"+json);
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
		
		if(json.getString("type").equals("new")) {//新建的放model，导入的前端会传
			json.put("json", excelJsonModel);
		}
		Excel excel = json.toJavaObject(Excel.class);
		int i = excelService.create(excel);
//		RedisUtil.set(encodeId, JSONObject.toJSONString(excel));
		logger.info("新建excel请求通过,return:"+excel);
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
		logger.info("收到获取excel请求"+json);
		String excelId = json.getString("excelId");
		Excel excel = null;
		// redis没有就去数据库拿
		if (RedisUtil.hasKey(excelId)) {
			String excelStr = (String) RedisUtil.get(excelId);
			excel = JSONObject.toJavaObject(JSONObject.parseObject(excelStr), Excel.class);
		} else {
			excel = excelService.get(excelId);
			//放进redis
			RedisUtil.set(excelId, JSONObject.toJSONString(excel));
		}
		logger.info("获取excel请求结束,return:"+excel);
		return CommonResult.ok(excel);
	}

	/**
	 * 保存excel (redis，数据库)
	 * 
	 * @param json
	 * {
	 * id: "dXNlcjIwMjAwMTE0MjI1OTM2LWV4Y2VsLTIwMjAwMTE1MDk0OTA1"
	 * name: "313"
	 * createDate: "2020-01-15"
	 * lastModifyDate: "2020-01-15 09:49:05"
	 * json: "{"version":"13.0.3","customList":[],"sheets":{"Sheet1":{"name":"Sheet1","activeRow":3,"activeCol":5,"theme":"Office","data":{"dataTable":{"6":{"3":{"value":"kkk"}}},"defaultDataNode":{"style":{"themeFont":"Body"}}},"rowHeaderData":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"colHeaderData":{"defaultDataNode":{"style":{"themeFont":"Body"}}},"leftCellIndex":0,"topCellIndex":0,"selections":{"0":{"row":3,"rowCount":1,"col":5,"colCount":1},"length":1},"cellStates":{},"outlineColumnOptions":{},"autoMergeRangeInfos":[],"index":0}}}"
	 * createrId: "user20200114225936"
	 * status: ""
	 * describe: null
	 * }
	 * 
	 * @return
	 */
	@PostMapping("/hardSave")
	public CommonResult hardSave(@RequestBody JSONObject json) {
		logger.info("收到保存excel请求"+json);
		Excel excel = JSONObject.toJavaObject(json, Excel.class);
		excelService.save(excel);
		RedisUtil.set(excel.getId(), JSONObject.toJSONString(excel));
		logger.info("保存excel请求通过");
		return CommonResult.ok();
	}
	/**
	 * 保存excel (redis)
	 * @param json
	 * @return
	 */
	@PostMapping("/easySave")
	public CommonResult easySave(@RequestBody JSONObject json) {
		logger.info("收到简单保存excel请求"+json);
		Excel excel = JSONObject.toJavaObject(json, Excel.class);
		RedisUtil.set(excel.getId(), JSONObject.toJSONString(excel));
		logger.info("简单保存excel请求通过");
		return CommonResult.ok();
	}

	/**
	 * 获取 用户 的excel列表
	 * 
	 * @param json 查询条件
	 * {
	 * name: "" 
	 * type: "1" // 1:自己的 ， 2:协助的,  0:删除的
	 * date: ["2020-01-10", "2020-02-03"] 
	 * pageSize: 2
	 * pageNum: 2 
	 * }
	 * 
	 * @return
	 */
	@PostMapping("/list")
	public CommonResult getExcelListByCreaterId(@RequestBody JSONObject json) {
		logger.info("收到获取excel列表请求"+json);
		json.put("id", getUser().getId());
		PageInfo pageInfo = null;
		String type = json.getString("type");
		switch (type) {
		case "1"://查自己的
			pageInfo = excelService.getExcelListByCreaterId(json, json.getIntValue("pageNum"),
					json.getIntValue("pageSize"));
			break;
		case "2"://查协助的
			pageInfo = excelService.getShareExcelListByUserId(json, json.getIntValue("pageNum"),
					json.getIntValue("pageSize"));
			break;
		case "0"://查删除的
			pageInfo = excelService.getExcelListByCreaterId(json, json.getIntValue("pageNum"),
					json.getIntValue("pageSize"));
			break;

		default:
			break;
		}
		logger.info("获取excel列表请求通过,return:"+pageInfo);
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
		logger.info("收到检查和绑定协助excel请求"+json);
		String excelId = json.getString("excelId");
		Excel excel = excelService.get(excelId);
		// 校验用户上送的邀请码对应的excel是否存在
		if (excel == null) {
			logger.info("检查和绑定协助excel请求失败,邀请码无效");
			return CommonResult.failed("邀请码无效");
		}
		String userId = getUser().getId();
		// 未绑定的绑定
		if (!excelService.ifbindShareExcel(excelId, userId))
			excelService.bindShareExcel(excelId, userId);
		logger.info("检查和绑定协助excel请求通过");
		return CommonResult.ok();
	}
	/**
	 * 删除excel
	 * @param json
	 * @return
	 */
	@PostMapping("/delete")
	public CommonResult delete(@RequestBody JSONObject json) {
		logger.info("收到删除excel请求"+json);
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
		logger.info("收到删除excel请求通过");
		return CommonResult.ok();
	}
	/**
	 * excel还原
	 * @param json
	 * @return
	 */
	@PostMapping("/recover")
	public CommonResult recover(@RequestBody JSONObject json) {
		logger.info("收到还原excel请求"+json);
		excelService.recover(json.getString("excelId"));
		logger.info("还原excel请求通过");
		return CommonResult.ok();
	}

}
