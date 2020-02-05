package com.zs;

import java.io.File;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.zs.util.MailUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MultiUserExcelSystemApplicationTests {
@Autowired
	MailUtil mailUtil;
	@Test
	void contextLoads() {
		String content="<html>\n"+"<body>\n"
                + "<h3>hello world!测试发送html格式邮件</h3>\n"
                +"</body>\n"+"</html>";
		
		String content1="<form action=\"demo_form.asp\" method=\"get\">\r\n" + 
				"Username: <input type=\"text\" name=\"usr_name\" />\r\n" + 
				"Encryption: <keygen name=\"security\" />\r\n" + 
				"<input type=\"submit\" />\r\n" + 
				"</form>";
		
		String rscId = "neo006";
	   File file = new File("F:\\Code\\excels\\code.PNG");
		
		try {
//			mailUtil.sendHtmlMail("2601377840@qq.com", "小黄人邮箱重置", content);
			mailUtil.sendImageMail("2601377840@qq.com", "小黄人邮箱重置", content,file);
//			mailUtil.test("2601377840@qq.com", "多功能excel系统密码重置");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
