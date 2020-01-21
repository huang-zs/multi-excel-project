package com.zs;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MultiUserExcelSystemApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(UUID.randomUUID().toString());
	}

}
