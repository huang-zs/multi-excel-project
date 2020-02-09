package com.zs.controller;

import java.util.List;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zs.api.CommonResult;
import com.zs.entity.DoorImage;
import com.zs.service.DoorService;

/**
 * 门户
 * @author MagicBook
 *
 */
@RestController
@RequestMapping("/door")
public class DoorController {
	@Autowired
	private DoorService doorService;
	
	/**
	 * 获取门户显示图片
	 * @return
	 */
	@RequestMapping("/image/list")
	public CommonResult getDoorImages() {
		List<DoorImage> doorImages = doorService.getDoorImages();
		return CommonResult.ok(doorImages);
		
	}
	
}
