package com.zs.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zs.entity.Excel;

public interface ExcelService {
	/**
	 * 新建excel
	 * @param excel
	 * @return
	 */
	public int create(Excel excel);

	/**
	 * 根据excelId的获取excel
	 * @param excelId
	 * @return
	 */
	public Excel get(String excelId);

	/**
	 *   根据ExcelId 保存Excel 对象
	 * @param excel
	 */
	public int save(Excel excel);

	/**
	 * 获取 用户创建 的excel列表
	 * @param map 查询条件
	 * @return
	 */
//	public List<Excel> getExcelListByCreaterId(String id);
	public PageInfo getExcelListByCreaterId(Map conditionMap, int pageNum, int pageSize);

	/**
	 * 获取用户协助的excel
	 * @param id
	 * @return
	 */
	public PageInfo getShareExcelListByUserId(Map conditionMap, int pageNum, int pageSize);

	/**
	 * 把用户和excel绑定起来
	 * @param excelId
	 * @param userId
	 */
	public void bindShareExcel(String excelId, String userId);

	/**
	 * 判断userId或excel是否已经绑定
	 * @param excelId
	 * @param userId
	 */
	public boolean ifbindShareExcel(String excelId, String userId);

	/**
	 * 逻辑删除excel和物理删除所有协助绑定记录
	 * @param excelId
	 */
	public void delete(String excelId);

	/**
	 * 解除绑定记录
	 * @param excelId
	 * @param userId
	 */
	public void unbind(String excelId, String userId);
	/**
	 * excel删除恢复正常
	 * @param excelId
	 */
	public void recover(String excelId);

}
