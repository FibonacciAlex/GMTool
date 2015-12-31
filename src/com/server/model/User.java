package com.server.model;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * 用户数据结构
 * @author Alex
 * @author 2015年12月16日 上午11:29:53
 */
public class User {
	
	@JsonProperty("userName")
	private String userName;
	
	@JsonProperty("pwd")
	private String pwd;
	
	@JsonProperty("ca")
	private String ca;//其它属性
	
	
	
	public User() {
	}
	public User(String userName, String pwd, String ca) {
		super();
		this.userName = userName;
		this.pwd = pwd;
		this.ca = ca;
	}
	public String getUserName() {
		return userName;
	}
	public String getPwd() {
		return pwd;
	}
	public String getCa() {
		return ca;
	}

}
