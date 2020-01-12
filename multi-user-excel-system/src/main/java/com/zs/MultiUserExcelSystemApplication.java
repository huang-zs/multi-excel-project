package com.zs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.zs.mapper")
@PropertySource("file:F:\\Java_code\\application.properties")
public class MultiUserExcelSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiUserExcelSystemApplication.class, args);
	}

}
