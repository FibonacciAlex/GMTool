package com.server.net;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.module.message.KGameMessage;
import com.module.message.KGameProtocol;
import com.module.message.codec.KGameMessageImpl;

/**
 * 系统消息工厂，负责消息新建及发送
 * @author Alex
 * @author 2015年12月14日 下午3:40:11
 */
public class GameMessageFactory {
	
	private final static long ServerStartTime = System.currentTimeMillis();
	/**
	 * 新建一个待发送的消息
	 * 
	 * @param msgType
	 *            消息类型 {@link KGameMessage#MTYPE_PLATFORM} /
	 *            {@link KGameMessage#MTYPE_GAMELOGIC}
	 * @param clientType
	 *            客户端类型 {@link KGameMessage#CTYPE_ANDROID} /
	 *            {@link KGameMessage#CTYPE_IPAD} /
	 *            {@link KGameMessage#CTYPE_IPHONE} /
	 *            {@link KGameMessage#CTYPE_PC}
	 * @param msgID
	 *            消息ID （程序自定义）
	 * @return 一个可操作的消息实例
	 */
	public static KGameMessage newPlatformMessage(int msgID) {
		return new KGameMessageImpl(KGameMessage.MTYPE_PLATFORM, KGameMessage.CTYPE_PC, msgID);
	}
	
	public static ChannelFuture send(Channel channel, KGameMessage msg){
		if(channel != null && channel.isConnected()){
			return channel.write(msg);
		}
		return null;
	}

	public static void handshake(Channel channel) {
		KGameMessage outMsg = newPlatformMessage(KGameProtocol.MID_HANDSHAKE);
		outMsg.writeUtf8String("gms");
		outMsg.writeUtf8String("gms");
		send(channel, outMsg);
	}

	public static void doPing(Channel channel) {
		KGameMessage PING_MSG = newPlatformMessage(KGameProtocol.MID_PING);
		PING_MSG.writeLong(System.currentTimeMillis()-ServerStartTime);
		send(channel, PING_MSG);
	}

}
