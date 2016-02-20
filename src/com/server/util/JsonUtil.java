package com.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {
	
	private static ObjectMapper mapper = new CustomObjectMapper();
	

	/**
	 * 将pojo转换成json字符串
	 * @param pojo
	 * @return
	 */
	public static <T> String generateJsonString(T pojo){
		String json;
		try {
			
			json = mapper.writeValueAsString(pojo);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return json;
	}

	
	/**
	 * 将json字符串转为pojo对象
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T parseFromJson(String json, Class<T> type){
		T pojo;
		try {
			pojo = mapper.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return pojo;
	}
}
