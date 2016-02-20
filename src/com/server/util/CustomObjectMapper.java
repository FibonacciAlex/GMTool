package com.server.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.util.StringUtils;

/**
 * <pre>
 * 扩展 Jackson 提供的 ObjectMapper 类
 * 对 Jackson 的序列化行为进行定制，比如，排除值为空属性、进行缩进输出、将驼峰转为下划线、进行日期格式化等
 * </pre>
 * @author Alex
 * @date 2016年2月19日  下午7:59:40
 */
public class CustomObjectMapper extends ObjectMapper {


	private static final long serialVersionUID = 1L;

	private boolean lowcase = false;
	private String dateFormatPattern;



	public void setLowcase(boolean lowcase) {
		this.lowcase = lowcase;
	}



	public void setDateFormatPattern(String dateFormatPattern) {
		this.dateFormatPattern = dateFormatPattern;
	}



	public void init() {
		// 排除值为空属性
		setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// 进行缩进输出
		configure(SerializationFeature.INDENT_OUTPUT, true);
		// 将驼峰转为下划线
		if (lowcase) {
			setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		}
		// 进行日期格式化

		if (StringUtils.hasText(dateFormatPattern)) {
			DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
			setDateFormat(dateFormat);
		}
	}

}
