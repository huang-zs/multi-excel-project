package com.zs.service;
import java.util.List;

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
	 * 获取当前用户创建 的excel列表
	 * @param string
	 * @return
	 */
	public List<Excel> getExcelListById(String string);
}
