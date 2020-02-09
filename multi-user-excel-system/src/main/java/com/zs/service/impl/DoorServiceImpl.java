package com.zs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zs.entity.DoorImage;
import com.zs.mapper.DoorImageMapper;
import com.zs.service.DoorService;
@Service
public class DoorServiceImpl implements DoorService {

	@Autowired
	private DoorImageMapper doorImageMapper;
	@Override
	public List<DoorImage> getDoorImages() {
		// TODO Auto-generated method stub
		return doorImageMapper.list();
	}

}
