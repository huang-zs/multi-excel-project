package com.zs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.entity.Excel;
import com.zs.mapper.ExcelMapper;
import com.zs.service.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService {
	@Autowired
	private ExcelMapper excelMapper;

	@Override
	public int create(Excel excel) {

		return excelMapper.create(excel);
	}

	@Override
	public Excel get(String excelId) {
		return excelMapper.get(excelId);
	}

	@Override
	public int save(Excel excel) {
		return excelMapper.save(excel);

	}

	@Override
	public PageInfo getExcelListByCreaterId(Map conditionMap,int pageNum,int pageSize) {
		PageInfo info = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(() ->
		excelMapper.getExcelListByCreaterId(conditionMap)
				);
		return info;
	}

	@Override
	public PageInfo getShareExcelListByUserId(Map conditionMap,int pageNum,int pageSize) {
		
		PageInfo info = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(() ->
		excelMapper.getShareExcelListByUserId(conditionMap)
				);
		return info;
	}

	@Override
	public void bindShareExcel(String excelId, String userId) {
		// TODO Auto-generated method stub
		excelMapper.bindShareExcel(excelId, userId);
	}

	@Override
	public boolean ifbindShareExcel(String excelId, String userId) {
		int count=excelMapper.countByExcelIdAndUserId(excelId,userId);
		return count==0?false:true;
	}

	@Override
	public void delete(String excelId) {
		
		excelMapper.updateExcelStatusByExcelId(excelId,"0");
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("excelId", excelId);
		excelMapper.unbindShareExcel(map);
	}
	

	@Override
	public void unbind(String excelId, String userId) {
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("excelId", excelId);
		map.put("userId", userId);
		excelMapper.unbindShareExcel(map);
		
	}

	@Override
	public void recover(String excelId) {
		excelMapper.updateExcelStatusByExcelId(excelId, "1");
		
	}

}
