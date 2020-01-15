package com.zs.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zs.entity.Excel;

@Mapper
public interface ExcelMapper {
	/**
	 * 新建excel
	 * 
	 * @param excel
	 * @return 修改行数
	 */
	int create(@Param("excel") Excel excel);

	/**
	 * 根据excelId获取Excel
	 * 
	 * @param excelId
	 * @return 修改行数
	 */
	Excel get(String excelId);

	/**
	 * 根据excelId修改json
	 * 
	 * @param excel
	 * @return 修改行数
	 */
	int save(@Param("excel") Excel excel);

	/**
	 * 根据createId获取excel列表
	 * 
	 * @param conditionMap 查询条件
	 * @return excel列表
	 */
	List<Excel> getExcelListByCreaterId(Map conditionMap);

	/**
	 * 根据createId获取加入协助的excel列表
	 * 
	 * @param id
	 * @return excel列表
	 */
	List<Excel> getShareExcelListByUserId(Map conditionMap);

	/**
	 * 把excelId和userId存入关联表
	 * @param excelId
	 * @param userId
	 */
	void bindShareExcel(String excelId, String userId);

	/**
	 * 根据userid和excelid返回记录数
	 * @param excelId
	 * @param userId
	 * @return
	 */
	int countByExcelIdAndUserId(String excelId, String userId);

	/**
	 * 根据excelId删除excel
	 * @param excelId
	 * @return
	 */
	void deleteExcelByExcelId(String excelId);

	/**
	 * 根据userid或excelId删除关联表记录
	 * @param map
	 * @return
	 */
	void unbindShareExcel(Map map);

}
