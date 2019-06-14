package com.rpc.rsf.base.netty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.rpc.rsf.base.Result;
import com.rpc.rsf.base.RpcRequest;
import com.rpc.rsf.base.client.RpcClientHandel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class RpcNettyClientHandel implements RpcClientHandel{
    Log logger = LogFactory.getLog(this.getClass());
    private EventLoopGroup group = new NioEventLoopGroup(1);
    private Bootstrap bootstrap ;
    NettyClientMessageHandler clientHandler =new NettyClientMessageHandler();
   
    public void initBoot() {
    	bootstrap= new Bootstrap();
    	bootstrap.group(group)
    	.channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<SocketChannel>() {
            //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new IdleStateHandler(0, 0, 60));
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));
				pipeline.addLast(clientHandler);
            }
        });
    }
    
   
    public Channel doConnect(InetSocketAddress address) throws InterruptedException {
    	if(bootstrap==null) {
    		initBoot();
    		bootstrap.remoteAddress(address);
    	}
        ChannelFuture future = bootstrap.connect();
        Channel channel = future.sync().channel();
        return channel;
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public <T> T invoke(RpcRequest request, InetSocketAddress addr) throws Throwable {
		Channel channel = doConnect(addr);
		if (channel!=null && channel.isActive()) {
            SynchronousQueue<Result<?>> queue = clientHandler.sendRequest(request,channel);
            Result<?> result = queue.take();
            if(result.hasException()) {
            	throw result.getException();
            }else {
            	return (T)result.getData();
            }
        }else {
        	throw new Exception("netty client is null or not active");
        }
	}
}