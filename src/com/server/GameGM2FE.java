package com.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.timer.Timer;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jdom.Element;

import com.module.message.KGameMessage;
import com.module.message.KGameProtocol;
import com.server.config.NetConfig;
import com.server.gs.GSManager;
import com.server.net.GameChannelPipelineFactory;
import com.server.net.GameMessageFactory;
import com.server.util.ServerLogFactory;

public class GameGM2FE extends SimpleChannelUpstreamHandler{

	private final static int CONN_NEW = 1;//新建连接标示
	private final static int CONN_FINE = 2;//连接正常标志
	
	
	private ClientBootstrap bootstrap;
	private Channel channel;
	private String feip;
	private int feport;
	private GSManager gsManager;
	
	
	private long GSPeroid;// 获取数据的周期(毫秒)
	private long nextGSListTime;//下次获取数据的时刻(毫秒)
	private AtomicBoolean isLogin = new AtomicBoolean(false);//是否已经登录
	
	private long nextCommonCheckTime = System.currentTimeMillis();//用于检查服务器通讯是否正常
	
	private static final long PingPeroid = 20*Timer.ONE_SECOND;// PING的周期(毫秒)
	private long nextPingTime;// 下次PING的时刻(毫秒)
	
	private static GameGM2FE instance = null;
	
	public static GameGM2FE getInstance(){
		if(instance == null){
			instance = new GameGM2FE();
		}
		return instance;
	}

	/**
	 * 初始化网络配置    注意这时还没有开始连接 
	 * 下一步应该调用{@link #start()}
	 * @param element
	 * @throws Exception
	 */
	public void initNetConfig(Element element) throws Exception {
		ServerLogFactory.netMsgLogger.info(">>>>>>>GM2FE  net init");
		feip = element.getChildTextTrim("LanIP");
		feport = Integer.parseInt(element.getChildTextTrim("SocketPort"));
		GSPeroid = Long.parseLong(element.getChildTextTrim("getGSListPeroid")) * Timer.ONE_SECOND;
		if (feip == null || feip.length() == 0 || feport == 0) {
			throw new Exception("feIP==null||feIP.length()==0||fePort==null||fePort.length()==0");
		}
		
		bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newSingleThreadExecutor(),
						Executors.newSingleThreadExecutor()));
		bootstrap.setPipelineFactory(new GameChannelPipelineFactory(this, null, null, null));
	}
	
	/**
	 * 检查是否已经登录FE
	 * @return
	 */
	public boolean checkLogin(){
		return isLogin.get();
	}
	
	public void stop(){
		if(channel != null){
			channel.disconnect();
			channel.close();
		}
		if(bootstrap != null){
			bootstrap.releaseExternalResources();
		}
		ServerLogFactory.netMsgLogger.info(">>>>>>>GMS2FE STOP!!!");
	}
	
	public void start(GSManager gsm){
		this.gsManager = gsm;
		tryToEnsureConnection();
		ServerLogFactory.netMsgLogger.info(">>>>>> GMS2FE start!");
	}

	
	private  int tryToEnsureConnection(){
		
		//如果没有连接就进行建立
		long nowTime = System.currentTimeMillis();
		if(channel == null || !channel.isConnected() || nowTime > nextCommonCheckTime){
			if(channel != null){
				channel.close();//如果不为空，先关闭了再重新连接
			}
			ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(feip,feport));
			channel = channelFuture.awaitUninterruptibly().getChannel();
			isLogin.set(false);
			
			ServerLogFactory.netMsgLogger.info("> GMS2FE CONNECT={}",toString());
			GameMessageFactory.handshake(channel);
			ServerLogFactory.netMsgLogger.info("> GMS2FE SHAKE HAND={}",toString());
			
			nextCommonCheckTime = System.currentTimeMillis() + Timer.ONE_MINUTE;
			return CONN_NEW;
			
		}else{
			return CONN_FINE;
		}
			
	}
	
	/**
	 * <pre>
	 * 心跳定时器调用
	 * 此任务负责连接、握手、心跳、获取列表
	 * </pre>
	 */
	public void onTimeSignal() {
		try {
			if(tryToEnsureConnection() == CONN_NEW){
				//新建的连接，不进行其他操作
				return;
			}
			//已经连接   检查并发送心跳信号
			long nowTime = System.currentTimeMillis();
			if(nowTime > nextPingTime){
				nextPingTime = nowTime + PingPeroid;
				GameMessageFactory.doPing(channel);
			}
			
			//检查是否已经登录  如果登录则获取服务器列表
			if(isLogin.get()){
				getGsList();
			}
			
		} catch (Exception e) {
			ServerLogFactory.netMsgLogger.error("GMS2FE heartbeat exception={}",e.getMessage());
		}
		
	}

	private void getGsList() {
		long nowTime = System.currentTimeMillis();
		if(nowTime > nextGSListTime){
			nextGSListTime = nowTime + GSPeroid;
			ServerLogFactory.netMsgLogger.debug("FE request gs list~");
			KGameMessage msg = GameMessageFactory.newPlatformMessage(KGameProtocol.MID_GET_GSLIST);
			GameMessageFactory.send(channel, msg);
		}
		
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		try {
			
			if(!(e.getMessage() instanceof KGameMessage)){
				ServerLogFactory.netMsgLogger.error("FE illegal Message Type!!{}",e.getMessage());
				return;
			}
			KGameMessage kmsg = (KGameMessage) e.getMessage();
			if (kmsg.getMsgType() == KGameMessage.MTYPE_PLATFORM) {
				//平台消息消息直接处理
				int msgID = kmsg.getMsgID();
				switch (msgID) {
				case KGameProtocol.MID_PING:
					break;
				case KGameProtocol.MID_HANDSHAKE:
					String tips = kmsg.readUtf8String();
					ServerLogFactory.netMsgLogger.info("FE hand shake response={}, try to valify identification~", tips);
					tryLoginFE();
					break;
				case KGameProtocol.MID_PASSPORT_VERIFY:
					kmsg.readUtf8String();
					int code = kmsg.readInt();
					tips = kmsg.readUtf8String();
					if (code == KGameProtocol.PL_PASSPORT_VERIFY_SUCCEED) {
						ServerLogFactory.netMsgLogger.warn("FE 身份验证成功");
						isLogin.compareAndSet(false, true);
					} else {
						ServerLogFactory.netMsgLogger.error("FE 身份验证失败={}", tips);
					}
					break;
				case KGameProtocol.MID_GET_GSLIST:
					ServerLogFactory.netMsgLogger.debug("FE return GS list");
					gsManager.refreshGSList(kmsg);
					nextCommonCheckTime = System.currentTimeMillis() + Timer.ONE_MINUTE;
					break;
				default:
					//其他对应逻辑信息
					
					break;
				}
			}
			
			
		} catch (Exception e2) {
			ServerLogFactory.mainLogger.error("rec FE to GMS msg exception={}",e2.getMessage());
			if(channel != null){
				channel.close();
				channel = null;
				ServerLogFactory.netMsgLogger.error("disconnet FE~");
			}
			throw e2;
		}
	}

	private void tryLoginFE() {
		KGameMessage msg = GameMessageFactory.newPlatformMessage(KGameProtocol.MID_PASSPORT_VERIFY);
		msg.writeUtf8String(gsManager.getGmUser());
		msg.writeUtf8String(gsManager.getGmPwd());
		GameMessageFactory.send(channel, msg);
	}

	@Override
	public String toString() {
		return channel == null ? "null" : channel.toString();
	}
	
	
	
}
