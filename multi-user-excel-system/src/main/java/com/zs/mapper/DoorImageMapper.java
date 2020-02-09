package com.zs.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zs.entity.DoorImage;
import com.zs.entity.Excel;
/**
 * 门户图片操作
 * @author MagicBook
 *
 */
@Mapper
public interface DoorImageMapper {
	/**
	 * 门户图片列表
	 * @return
	 */
	List<DoorImage> list();

}
