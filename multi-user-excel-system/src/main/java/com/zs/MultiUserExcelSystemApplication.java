package com.zs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.zs.mapper")
public class MultiUserExcelSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(MultiUserExcelSystemApplication.class, args);
	}

}
