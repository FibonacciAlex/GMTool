package com.server.util;


/**
 * 
 * <pre>
 * 定义响应类
 * </pre>
 * @author Alex
 * @author 2015年12月30日 下午8:15:02
 */
public class Response {

	
	private final static String OK = "ok";
	
	private final static String ERROR = "error";
	
	private Meta meta;
	private Object data;
	
	public Response success(){
		this.meta = new Meta(true, OK);
		return this;
	}
	
	public Response failure(){
		this.meta = new Meta(false,ERROR);
		return this;
	}
	
	public Response failure(String msg){
		this.meta = new Meta(false,msg);
		return this;
	}
	
	
	public Meta getMeta() {
		return meta;
	}

	public Object getData() {
		return data;
	}




	public class Meta{
		private boolean success;
		private String message;
		
		
		
		public Meta(boolean success) {
			super();
			this.success = success;
		}





		public Meta(boolean success, String message) {
			super();
			this.success = success;
			this.message = message;
		}





		public boolean isSuccess() {
			return success;
		}





		public String getMessage() {
			return message;
		}
		
		
		
	}
}
