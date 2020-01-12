package com.zs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<Excel> getExcelListById(String createrId) {
		// TODO Auto-generated method stub
		return excelMapper.getExcelListById(createrId);
	}

}
