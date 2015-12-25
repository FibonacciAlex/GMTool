package com.server.util;

public class GMTool {

	
	public static int getGsId(String serverName) {
		if (serverName == null || serverName.length() < 1) {
			return 0;
		}
		return Integer.parseInt(serverName.split("区]")[0].replaceFirst("[", ""));
	}

	public static String genGSName(int gsId) {
		if (gsId < 1)
			return "";
		return "[" + gsId + "区]";
	}
}
