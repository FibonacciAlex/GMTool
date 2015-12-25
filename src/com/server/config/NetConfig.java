package com.server.config;


/**
 * FE网络配置类
 * @author Alex
 * @author 2015年12月12日 下午3:58:16
 */
public class NetConfig {

	private String FEip;
	
	private int port;
	
	private long GSListPeroid;// 获取数据的周期(毫秒)

	//消息处理线程池相关设定 -->
	private int corePoolSize;
	
	private long maxChannelMemorySize;
	
	private long maxTotalMemorySize;
	
	private long keepAliveTimeMillis;
	//------------------------>
	
	public NetConfig() {
	}

	public String getFEip() {
		return FEip;
	}


	public int getPort() {
		return port;
	}


	public long getGSListPeroid() {
		return GSListPeroid;
	}


	public int getCorePoolSize() {
		return corePoolSize;
	}


	public long getMaxChannelMemorySize() {
		return maxChannelMemorySize;
	}


	public long getMaxTotalMemorySize() {
		return maxTotalMemorySize;
	}


	public long getKeepAliveTimeMillis() {
		return keepAliveTimeMillis;
	}

	public void setFEip(String fEip) {
		FEip = fEip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setGSListPeroid(long gSListPeroid) {
		GSListPeroid = gSListPeroid;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public void setMaxChannelMemorySize(long maxChannelMemorySize) {
		this.maxChannelMemorySize = maxChannelMemorySize;
	}

	public void setMaxTotalMemorySize(long maxTotalMemorySize) {
		this.maxTotalMemorySize = maxTotalMemorySize;
	}

	public void setKeepAliveTimeMillis(long keepAliveTimeMillis) {
		this.keepAliveTimeMillis = keepAliveTimeMillis;
	}

	
	
	
		
}
