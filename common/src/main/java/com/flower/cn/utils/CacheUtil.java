package com.flower.cn.utils;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {
	private static Map<String, Object> cache = new HashMap<>();
	
	public static void setObject(String key, Object object) {
		cache.put(key, object);
	}
	
	public static Object getObject(String key) {
		return cache.get(key);
	}
}
