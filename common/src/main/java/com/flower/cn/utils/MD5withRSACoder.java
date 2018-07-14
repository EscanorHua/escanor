package com.flower.cn.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public abstract class MD5withRSACoder {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	public static byte[] decryptBASE64(String privateKey) {
		byte[] output = null;
		output = Base64.decodeBase64(privateKey);
//		try {
//			output = (new BASE64Decoder()).decodeBuffer(privateKey);
//			return output;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return output;
	}

	public static String encryptBASE64(byte[] keyBytes) {
//		String s = (new BASE64Encoder()).encode(keyBytes);
		String s = Base64.encodeBase64String(keyBytes);
		return s;
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);

		return encryptBASE64(signature.sign());
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * 
	 * @return 校验成功返回true 失败返回false
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {

		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);

		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	/**
	 * 解密<br>
	 * 用公钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key)
			throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}

	/**
	 * 加密<br>
	 * 用私钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);

		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);

		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(512);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, Object> keyMap = new HashMap<String, Object>(2);

		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	static String privateKey;
	static String publicKey;

	public static void getKey() throws Exception {
		Map<String, Object> keyMap = MD5withRSACoder.initKey();

		publicKey = MD5withRSACoder.getPublicKey(keyMap);
		privateKey = MD5withRSACoder.getPrivateKey(keyMap);
		System.out.println((Key) keyMap.get(PUBLIC_KEY));
		// 公钥生成
		System.out.println("public : " + publicKey);
		// 私钥生成
		System.err.println("private : " + privateKey);
	}

	public static void main(String[] args) throws Exception {
//		getKey();
		// System.err.println("公钥加密——私钥解密");
		// 将明文转换为字节数组

		
//		boolean a = MD5withRSACoder.verify("001{\"qblon_seq\":\"1234567\",\"instu_cde\":\"900000000\",\"bch_cde\":\"900000000\",\"channel\":\"01\",\"apply_dt\":\"2017-10-31\",\"loan_typ\":\"YGD_002\",\"indiv_mobile\":\"13422222222\",\"apply_amt\":10000,\"apply_tnr\":6,\"apply_tnr_typ\":\"M\",\"mtd_cde\":\"SYS002\",\"loan_freq\":\"1M\",\"price_int_rat\":\"0.0005\",\"price_od_rat\":\"0.0005\",\"id_typ\":\"20\",\"id_no\":\"630101198710310050\",\"cust_name\":\"李四\",\"appl_ac_nos\":\"6214830211111111\",\"appl_ac_nor\":\"6214830211111111\",\"appl_ac_nams\":\"李四\",\"appl_ac_namr\":\"李四\",\"appl_ac_banks\":\"308\",\"appl_ac_bankr\":\"308\",\"appl_ac_bchs\":\"308100000072\",\"appl_ac_bchr\":\"308100000072\",\"appl_ac_typs\":\"01\",\"appl_ac_typr\":\"01\",\"due_day_opt\":\"1\",\"due_day\":\"10\",\"relarray\":[{\"rel_name\":\"张三\",\"rel_relation\":\"03\",\"rel_mobile\":\"18311111111\"},{\"rel_name\":\"李三\",\"rel_relation\":\"03\",\"rel_mobile\":\"18321111111\"}]}1509436502769".getBytes(), "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIvEqetSWeQsj1mlHfKG39aWx8TVqUmbndP1csPm4QmODeNRnIoqCSjt+sps+vlYowhwnOP/vQrY75fID2IHRgECAwEAAQ==", signData);
		// //用私钥解密
		//
//		System.out.println("解密后: " + a);
		
		System.out.println(System.currentTimeMillis());
		String data = "{\"qblon_seq\":\"1234567\",\"instu_cde\":\"900000000\",\"bch_cde\":\"900000000\",\"channel\":\"01\",\"apply_dt\":\"2017-10-31\",\"loan_typ\":\"YGD_002\",\"indiv_mobile\":\"13422222222\",\"apply_amt\":10000,\"apply_tnr\":6,\"apply_tnr_typ\":\"M\",\"mtd_cde\":\"SYS002\",\"loan_freq\":\"1M\",\"price_int_rat\":\"0.0005\",\"price_od_rat\":\"0.0005\",\"id_typ\":\"20\",\"id_no\":\"630101198710310050\",\"cust_name\":\"李四\",\"appl_ac_nos\":\"6214830211111111\",\"appl_ac_nor\":\"6214830211111111\",\"appl_ac_nams\":\"李四\",\"appl_ac_namr\":\"李四\",\"appl_ac_banks\":\"308\",\"appl_ac_bankr\":\"308\",\"appl_ac_bchs\":\"308100000072\",\"appl_ac_bchr\":\"308100000072\",\"appl_ac_typs\":\"01\",\"appl_ac_typr\":\"01\",\"due_day_opt\":\"1\",\"due_day\":\"10\",\"relarray\":[{\"rel_name\":\"张三\",\"rel_relation\":\"03\",\"rel_mobile\":\"18311111111\"},{\"rel_name\":\"李三\",\"rel_relation\":\"03\",\"rel_mobile\":\"18321111111\"}]}";
		String s = encryptBASE64(data.getBytes());
		System.out.println("base64:"+s);
		String unSignData = "001" + data + "1509436502769";
		// 用公钥加密
		String signData = MD5withRSACoder.sign(unSignData.getBytes(), "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAi8Sp61JZ5CyPWaUd8obf1pbHxNWpSZud0/Vyw+bhCY4N41GciioJKO36ymz6+VijCHCc4/+9Ctjvl8gPYgdGAQIDAQABAkEAi7IZ31Ek" +
				"9XEwKfplr0TtLs7vhiXDmQWHvxVhCrqVi/TMCBz+snsXtYlTRQStSGdShtw1pgbkRwu+E3C4LWSD0QIhAMbBl8emDs92cMb7p66YfKD2TIT/Kl30S7GEyIhymFltAiEAtAXaNkEubvu6m+rY+Jz7Jye9" + 
				"JB0sF/ZyFXOULnrrNmUCIQDCOa6DyC+S5vAgUw2ynYq0lNuD4AJb2/4YFGUOicV9JQIgUH35Zp7YWylU89ga8XaHWaeMC3S7vW8k7XCDRhER0H0CIQCgrJikC8eJK91oFDjaIPhYypnYGhi66MTsSNc9" + 
				"CYFsuQ==");
		// 打印加密字符串 为什么使用BASE64Encoder 因为在RSACoder里加密用的是 BASE64Encoder 加密
//		String s = (new BASE64Encoder()).encode(encodedData);
		System.err.println("签名: " + signData);
	}

}