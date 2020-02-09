package com.zs.util;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *  加密工具
 * @author MagicBook
 *
 */
@Component
public class AesUtil {

	private static final Logger log = LoggerFactory.getLogger(AesUtil.class);

	// 偏移量
	private static final String IV = "ZS20200207ADDAES";

	// 参数分别代表 算法名称/加密模式/数据填充方式
	private static final String ALGORITHMSTR = "AES/CBC/NoPadding";


	/**
	 *     加密（AES/CBC/NoPadding）
	 * @param data 明文
	 * @param key 密钥
	 * @param iv 偏移量
	 * @return base64密文
	 * @throws Exception
	 */
	public static String encrypt(String data, String key, String iv) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		int blockSize = cipher.getBlockSize();

		byte[] dataBytes = data.getBytes();
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}
		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
		byte[] encrypted = cipher.doFinal(plaintext);
		return new Base64().encodeToString(encrypted).trim();

	}

	/**
	 * 解密（AES/CBC/NoPadding）
	 * @param content 密文
	 * @param key 密钥
	 * @param iv 偏移量
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String content, String key, String iv) throws Exception {

		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes(), 0, 16);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);// 初始化
		byte[] result = cipher.doFinal(new Base64().decode(content));
		return new String(result).trim();
	}

	/**
	 *    根据时间生成密钥
	 * @return
	 * @throws IOException
	 */
	public static String getDateKey() throws IOException {
		LocalDateTime now = LocalDateTime.now();
		String nowStr = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.CHINA).format(now);
		
		
		return nowStr+new StringBuffer(nowStr).reverse().toString();

	}

	/**
	 * 读服务器密钥加密
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content) throws Exception {
		String key = getDateKey();
		return encrypt(content, key, IV);
	}

	/**
	 * 读服务器密钥解密
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content) throws Exception {
		String key = getDateKey();
		return decrypt(content, key, IV);
	}

	public static void main(String[] args) {
		String encrypt;
		try {
			encrypt = encrypt("zshuang");

			System.out.println(encrypt);
			System.out.println(decrypt(encrypt));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
