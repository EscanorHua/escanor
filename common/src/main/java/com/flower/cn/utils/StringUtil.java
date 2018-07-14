package com.flower.cn.utils;

import java.math.BigDecimal;

public class StringUtil {

	/**
	 * 用特定字符填充字符串
	 * 
	 * @param sSrc
	 *            要填充的字符串
	 * @param ch
	 *            用于填充的特定字符
	 * @param nLen
	 *            要填充到的长度
	 * @param bLeft
	 *            要填充的方向：true:左边；false:右边
	 * @return 填充好的字符串
	 */
	public static String fill(String sSrc, char ch, int nLen, boolean bLeft) {
		if (sSrc == null || sSrc.equals("")) {
			StringBuffer sbRet = new StringBuffer();
			for (int i = 0; i < nLen; i++)
				sbRet.append(ch);
			return sbRet.toString();
		}
		byte[] bySrc = sSrc.getBytes();
		int nSrcLen = bySrc.length;
		if (nSrcLen >= nLen) {
			return sSrc;
		}
		byte[] byRet = new byte[nLen];
		if (bLeft) {
			for (int i = 0, n = nLen - nSrcLen; i < n; i++)
				byRet[i] = (byte) ch;
			for (int i = nLen - nSrcLen, n = nLen; i < n; i++)
				byRet[i] = bySrc[i - nLen + nSrcLen];
		} else {
			for (int i = 0, n = nSrcLen; i < n; i++)
				byRet[i] = bySrc[i];
			for (int i = nSrcLen, n = nLen; i < n; i++)
				byRet[i] = (byte) ch;
		}
		return new String(byRet);
	}
	
	public static String fen2Yuan(Long amount) {
		return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).toString();
	}

	/**
	 * 是否是空串
	 * null 、 "" 返回 true
	 * */
	public  static  boolean isEmptyString(Object string){
		return string==null||string.toString().trim().equals("");
	}

	public static boolean isNotBlank(String input){
		if("".equals(input) ||input == null)
			return false;
		return true;
	}

}
