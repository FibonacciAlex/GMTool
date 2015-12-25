package com.server.gs;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.module.message.KGameMessage;
import com.module.message.KGameProtocol;
import com.server.util.ServerLogFactory;


/**
 * <pre>
 * 游戏服务器管理器
 * 
 * 注意:GS名称需要数据库保存,但是允许保存失败;
 * 数据库保存名称,仅为下次启动GMS时,可以根据保存的GS名称加载相关数据,不用于其它操作时验证是否存在相应GS.
 * </pre>
 * @author Alex
 * @author 2015年12月14日 下午8:59:34
 */
public final class GSManager {
	
	private String gmUser;
	private String gmPwd;
	
	private boolean multiThreadLoad;
	
	private int roleMailHistoryShowDays;
	
	private int GMOpLogShowDays;

	public void setGmUser(String gmUser) {
		this.gmUser = gmUser;
	}


	public void setGmPwd(String gmPwd) {
		this.gmPwd = gmPwd;
	}


	public void setMultiThreadLoad(boolean multiThreadLoad) {
		this.multiThreadLoad = multiThreadLoad;
	}


	public void setRoleMailHistoryShowDays(int roleMailHistoryShowDays) {
		this.roleMailHistoryShowDays = roleMailHistoryShowDays;
	}


	public void setGMOpLogShowDays(int gMOpLogShowDays) {
		GMOpLogShowDays = gMOpLogShowDays;
	}


	public String getGmUser() {
		return gmUser;
	}


	public String getGmPwd() {
		return gmPwd;
	}


	public boolean isMultiThreadLoad() {
		return multiThreadLoad;
	}


	public int getRoleMailHistoryShowDays() {
		return roleMailHistoryShowDays;
	}


	public int getGMOpLogShowDays() {
		return GMOpLogShowDays;
	}

	
	
	
	
	

	/**
	 * 刷新服务器列表
	 * @param kmsg
	 */
	public void refreshGSList(KGameMessage msg) {
		int result = msg.readInt();
		if(result != KGameProtocol.PL_GET_GSLIST_SUCCEED){
			ServerLogFactory.mainLogger.error("GMS GET GS LIST FROM FE FAILED! CODE={}",result);
			return;
		}
		
		Map<Integer, GSData> newGsMap = new LinkedHashMap<Integer, GSData>();
		byte zonesize = msg.readByte();
		for (int i = 0; i < zonesize; i++) {
			msg.readInt();
			msg.readUtf8String();
			byte gssize = msg.readByte();
			for (int j = 0; j < gssize; j++) {
				readGsData(msg, newGsMap);
			}
		}

		List<GSData> newGSList = new ArrayList<GSData>(newGsMap.values());

		newGsMap.clear();
		for (GSData data : newGSList) {
			newGsMap.put(data.gsId, data);
		}
		updateGSList(newGsMap);
		

	}
	
	/**
	 * <pre>
	 * 根据新的gsMap，增加还没有的，关闭已经存在的
	 * </pre>
	 * @param newGsMap
	 */
	private void updateGSList(Map<Integer, GSData> newGsMap) {
		
	}

	/**
	 * 读取gs服务器信息
	 * @param msg
	 * @param map
	 */
	private void readGsData(KGameMessage msg, Map<Integer, GSData> map) {
		GSData data = new GSData();
		data.gsId = msg.readInt();
		msg.readUtf8String();
		msg.readUtf8String();
		data.gslabel = msg.readUtf8String();
		msg.readUtf8String();
		data.gsstatus = msg.readByte();
		msg.readByte();
		msg.readInt();
		data.gsonline = msg.readInt();
		String ip= msg.readUtf8String();
		data.newAddress = new InetSocketAddress(ip, msg.readInt());
		byte rolesize = msg.readByte();
		for (int k = 0; k < rolesize; k++) {
			msg.readInt();
			msg.readInt();
		}

		if (!map.containsKey(data.gsId)) {
			map.put(data.gsId, data);
		}
	}


	public class GSData {
		public int gsId;
		public String gslabel;
		public byte gsstatus;
		public int gsonline;
		public InetSocketAddress newAddress;
	}

}
