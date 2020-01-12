package com.zs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zs.entity.User;

@Mapper
public interface UserMapper {
	User login(@Param("user") User user);
	List<User> userList();
	int existEmail(@Param("email") String email);
	int create(@Param("user")User user);
}
