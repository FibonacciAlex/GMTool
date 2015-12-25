package com.server.net;


import javax.management.timer.Timer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.module.message.KGameMessage;
import com.module.message.KGameProtocol;
import com.server.gs.GsSession;
import com.server.util.ServerLogFactory;


/**
 *  GS的通信层处理器。<br>
 * ·通道（连接）的建立、断开等事件，从而关系到通信通道{@link org.jboss.netty.channel.Channel}的缓存；<br>
 * ·收到底层消息（以{@link org.jboss.netty.buffer.ChannelBuffer}为载体）内容后，
 * 我们需要转换成特定的消息格式并根据消息类型作出处理（或平台层处理或传递到游戏逻辑层处理）；<br>
 * ·异常处理
 * @author Alex
 * @author 2015年12月14日 下午12:26:02
 */
public class GameGM2GSHandler extends SimpleChannelHandler implements KGameProtocol{

	public GameGM2GSHandler() {
	}

	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		
		try{
			if (!(e.getMessage() instanceof KGameMessage)) {
				// 非法消息，logging & disconnect
				ServerLogFactory.netMsgLogger.error("GS Illegal Message Type!!" + e.getMessage());
				return;
			}
			
			Channel channel = e.getChannel();
			KGameMessage kmsg = (KGameMessage) e.getMessage();
			
			GsSession gss = (GsSession) channel.getAttachment();
			if (gss == null) {
				ServerLogFactory.netMsgLogger.error("GS Channel 不存在 GsSession!! msg={}", e);
				channel.close();
				return;
			}
			
	
			// 根据消息类型做处理
			if (kmsg.getMsgType() == KGameMessage.MTYPE_PLATFORM) {
				// 平台消息消息直接处理//////////////////////////////////////////////////////////
				int msgID = kmsg.getMsgID();
				switch (msgID) {
	
				/* 客户端心跳消息 */
				case MID_PING:
//					gss.nextCommonCheckTime = System.currentTimeMillis() + Timer.ONE_MINUTE;
					break;
	
				/* 握手消息，比较关键，每个连接必须先进行握手才是合法 */
				case MID_HANDSHAKE:
					String tips = kmsg.readUtf8String();
					ServerLogFactory.netMsgLogger.debug("GS({}) 握手反馈={}", gss.toString(), tips);
//					gss.onHankshak();
					break;
	
				/* 通过用户名登录GS */
				case MID_LOGIN_BY_NAME:
//					gss.onLoginBack(kmsg);
					break;
	
				/* 登出GS */
				case MID_LOGOUT:
					ServerLogFactory.netMsgLogger.warn("GS({}) Logout Msg", gss.toString());
					channel.close();
					break;
	
				/* 系统公告 */
				case MID_SYSTEM_NOTICE:
					String notice = kmsg.readUtf8String();
					ServerLogFactory.netMsgLogger.warn("GS({}) 系统公告={}", gss.toString(), notice);
					break;
	
				/* 服务器关闭for test */
				case MID_SHUTDOWN:// XXX test shutdown
					ServerLogFactory.netMsgLogger.error("GS({}) 服务器关机！", gss.toString());
					break;
	
				/* 获取GS服务器状态，可以是任意客户端 */
				case MID_SERVERSTATUS:
					// CTODO GMS-会有什么内容？？
					break;
	
				/* 测试消息 */
				case MID_DEBUG:// XXX DEBUG消息解析发送
					break;
	
				/* 其它没处理到的消息最大的可能是其它平台模块（如GS系统）的消息 */
				default:
					// TODO GM等模块的消息的分发
					break;
				}
			} else {
				// 游戏逻辑消息分发//////////////////////////////////////////////////////////
//				GMSAndGSMsgProcessor.onGSTCPMessage(gss, kmsg);
			}
		} catch (Exception ex) {
			ServerLogFactory.netMsgLogger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel channel = ctx.getChannel();
		if (channel == null) {
			return;
		}

		GsSession gss = (GsSession) channel.getAttachment();
		channel.setAttachment(null);
		channel.close();

		if (gss != null) {
//			gss.onChannelClosed(channel);
		}
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		this.channelClosed(ctx, e);
	}
}
