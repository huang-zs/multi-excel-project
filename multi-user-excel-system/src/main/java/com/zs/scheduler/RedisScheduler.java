package com.zs.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(RedisScheduler.class);

//	@Scheduled(fixedRate=10000)
	public void test() {
		log.debug("test");
	}
}
