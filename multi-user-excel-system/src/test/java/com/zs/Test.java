package com.zs;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class Test {
public static void main(String[] args) {
	System.out.println(UUID.randomUUID().toString());
	String email="2601377840@qq.com";
	
    String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(LocalDateTime.now());
    System.out.println("时间是：" + format);
	
    UUID uuid = UUID.nameUUIDFromBytes((email+format).getBytes());
    System.out.println(uuid);
}
}
