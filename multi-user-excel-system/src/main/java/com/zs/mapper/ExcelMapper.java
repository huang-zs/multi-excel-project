package com.zs.mapper;

import java.util.List;

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
	 * @param createrId
	 * @return excel列表
	 */
	List<Excel> getExcelListById(String createrId);
}
