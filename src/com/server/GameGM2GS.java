package com.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jdom.Element;

import com.server.config.NetConfig;
import com.server.net.GameChannelPipelineFactory;
import com.server.net.GameGM2GSHandler;
import com.server.util.ServerLogFactory;

public class GameGM2GS {
	
	private ClientBootstrap bootstrap;
	
	private static GameGM2GS instance = null;
	
	private GameGM2GS(){
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),// Boss线程池
				Executors.newCachedThreadPool()));// Worker线程池
	}
	
	public static GameGM2GS getInstance(){
		if(instance == null){
			instance = new GameGM2GS();
		}
		return instance;
	}

	public void initNetConfig(Element element) {
		// 一个专门用来处理服务器收到的消息（Upstream）的线程池，目的就是提高worker线程的效率
		Element e = element.getChild("ExecutionHandler");
		int corePoolSize = Integer.parseInt(e.getAttributeValue("corePoolSize"));
		long maxChannelMemorySize = Long.parseLong(e.getAttributeValue("maxChannelMemorySize"));
		long maxTotalMemorySize = Long.parseLong(e.getAttributeValue("maxTotalMemorySize"));
		long keepAliveTimeMillis = Long.parseLong(e.getAttributeValue("keepAliveTimeMillis"));
		// 一个专门用来处理服务器收到的消息（Upstream）的线程池，目的就是提高worker线程的效率
		ExecutionHandler executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(corePoolSize,
				maxChannelMemorySize, maxTotalMemorySize, keepAliveTimeMillis, TimeUnit.MILLISECONDS));

		bootstrap.setPipelineFactory(new GameChannelPipelineFactory(new GameGM2GSHandler(), executionHandler, null, null));
	}
			

	public Channel connect(Object attachment, InetSocketAddress socketAddress) throws Exception {

		Channel channel = bootstrap.connect(socketAddress).await().getChannel();

		if (!channel.isConnected()) {
			channel.close();
			ServerLogFactory.netMsgLogger.error("GS({}) 连接失败，Add={}", attachment, socketAddress);
			return null;
		}

		channel.setAttachment(attachment);
		return channel;
	}
	
	
}
