package com.flower.cn.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;

public class SignatureUtils {
    private static final Splitter.MapSplitter urlParamSplitter = Splitter.on("&").trimResults().omitEmptyStrings().withKeyValueSeparator("=");
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    
    public static String genParamJsonByMap(Map<String,String> params) {
    	String json = gson.toJson(params);
    	return  json;
    }
    
    public static Gson getGson() {
		return gson;
	}

	/**
     * @param params 名值对格式的http请求参数 如： name=chenyan&age=24&
     * @param json
     * @return
     */
    public static Map<String, String> genParamMap(String params, String json) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.putAll(genParamMapFromNameValuePair(params));
        paramMap.putAll(genParamMapFromJson(json));
        return paramMap;
    }

    public static Map<String, String> genParamMap(String params, String json, ParamFilter paramFilter) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.putAll(genParamMapFromNameValuePair(params, paramFilter));
        paramMap.putAll(genParamMapFromJson(json, paramFilter));
        return paramMap;
    }

    public static Map<String, String> genParamMapFromNameValuePair(String params) {
        Map<String, String> paramMap = Maps.newHashMap();
        if (StringUtils.isEmpty(params) || !params.contains("=")) {
            return paramMap;
        }
        paramMap.putAll(urlParamSplitter.split(params));
        return paramMap;
    }

    public static Map<String, String> genParamMapFromNameValuePair(String params, ParamFilter paramFilter) {
        Map<String, String> paramMap = Maps.newHashMap();
        if (StringUtils.isEmpty(params) || !params.contains("=")) {
            return paramMap;
        }
//        jdk8写法
//        urlParamSplitter.split(params).forEach((k, v) -> {
//            if (paramFilter.doFilter(k, v)) {
//                paramMap.put(k, v);
//            }
//        });
        //jdk7写法
        Map<String, String> param =urlParamSplitter.split(params);
        Set<Entry<String, String>> entries = param.entrySet();
        for (Entry<String, String> entry : entries) {
        	String key =entry.getKey();
        	String value=entry.getValue();
        	if(paramFilter.doFilter(key,value)) {
        		 paramMap.put(key, value);
            }
        }
        return paramMap;
    }

    public static Map<String, String> genParamMapFromJson(String json) {
        Map<String, String> paramMap = Maps.newHashMap();
        if (StringUtils.isEmpty(json) || !json.startsWith("{")) {
            return paramMap;
        }
        for (Entry<String, JsonElement> entry : gson.fromJson(json, JsonObject.class).entrySet()) {
            paramMap.put(entry.getKey(), entry.getValue().isJsonPrimitive() ? entry.getValue().getAsString() : entry.getValue().toString());
        }
        return paramMap;
    }

    /**
     * @param json
     * @param paramFilter 返回 true 表示允许添加到结果中
     * @return
     */
    public static Map<String, String> genParamMapFromJson(String json, ParamFilter paramFilter) {
        Map<String, String> paramMap = Maps.newHashMap();
        if (StringUtils.isEmpty(json) || !json.startsWith("{")) {
            return paramMap;
        }
        for (Entry<String, JsonElement> entry : gson.fromJson(json, JsonObject.class).entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue().isJsonPrimitive() ? entry.getValue().getAsString() : entry.getValue().toString();
            if (paramFilter.doFilter(k, v)) {
                paramMap.put(k, v);
                ;
            }
        }
        return paramMap;
    }

    public interface ParamFilter {
        boolean doFilter(String k, String v);
    }

    private static String _genSignature(List<String> params) {
// 排序，拼接
        Collections.sort(params);
        StringBuilder content = new StringBuilder();
        for (String param : params) {
            content.append(param);
        }
// md5
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            String contentString = content.toString();
            System.out.println("md5 前的字符串:" + contentString);
            byte[] byteArray = contentString.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                    ;
                }
                hexValue.append(Integer.toHexString(val));
            }
            String result = hexValue.toString().toUpperCase();
            return result;
        } catch (Exception e) {
            System.out.println("生成签名发生异常, 参数:" + params);
            throw new RuntimeException("生成签名发生异常", e);
        }
    }

    public static String genSignature(Map<String, String> params, String clientKey) {
        /*if (!params.containsKey("capitalId")) {
            System.out.println("生成签名失败, capitalId, params:" + primes);
            throw new IllegalArgumentException("生成签名时,参数中缺少必需参数: capitalId");
        }*/
        if (StringUtils.isEmpty(clientKey)) {
            System.out.println("生成签名失败, 缺少必要的clientKey参数");
            throw new IllegalArgumentException("生成签名失败, 缺少必要的clientKey参数");
        }
        // 取"值"
        params.remove("signature");
//        List<String> paramValues = params.values().stream()
//                .filter(value -> value != null)
//                .collect(Collectors.toList());
        List<String> paramValues= new ArrayList<String>();
        Collection<String> values =params.values();
        Iterator<String> it = values.iterator();
        while(it.hasNext()) {  
        	String value =it.next();
        	if(value!=null) {
        		
        		paramValues.add(value);  
        	}
        }  
        paramValues.add(clientKey);
        return _genSignature(paramValues);
    }

    public static void main(String[] args) {

        //资方从闪银开放平台获取的资方秘钥
        String clientKey = "a5e01d8d-85c9-4935-a514-8606ee719ddd1500978754858capital_test";

        //生成签名的所有参数
        String params = "{\n" +
                "    \"capitalId\":\"capital_test\",\n" +
                "    \"loanOrderNo\":\"Wecash7516768\",\n" +
                "    \"timestamp\":\"1486612432767\"\n" +
                "}";

        //生成签名
        String signature = SignatureUtils.genSignature(genParamMapFromJson(params), clientKey);
        System.out.println("签名结果: " + signature);
    }
}
