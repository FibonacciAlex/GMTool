package com.server.net;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;

import com.module.message.codec.KGameMessageDecoder;
import com.module.message.codec.KGameMessageEncoder;

/**
 * 通道管道生成的工厂类【游戏逻辑无须理会】
 * @author Alex
 * @author 2015年12月14日 上午11:30:23
 */
public final class GameChannelPipelineFactory implements ChannelPipelineFactory{

	private ChannelHandler handler;
	private ExecutionHandler executionHandler;
	private IdleStateHandler idleStateHandler;
	private IdleStateAwareChannelHandler idleAwareHandler;
	
	
	
	
	public GameChannelPipelineFactory(ChannelHandler handler, ExecutionHandler executionHandler,
			IdleStateHandler idleStateHandler, IdleStateAwareChannelHandler idleAwareHandler) {
		super();
		this.handler = handler;
		this.executionHandler = executionHandler;
		this.idleStateHandler = idleStateHandler;
		this.idleAwareHandler = idleAwareHandler;
	}




	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		// IDLE相关设置
		if (idleStateHandler != null) {
			pipeline.addLast("timeout", idleStateHandler);
			pipeline.addLast("idleAware", idleAwareHandler);
		}

		// 自定义编码解码器
		pipeline.addLast("kgame_decoder", new KGameMessageDecoder());
		pipeline.addLast("kgame_encoder", new KGameMessageEncoder());

		// 注意！！！！！在这里加入一个消息处理线程池，这是性能的关键！！！
		if (executionHandler != null) {
			pipeline.addLast("kgame_execution", executionHandler);
		}
		pipeline.addLast("handler", handler);

		return pipeline;
	}

}
