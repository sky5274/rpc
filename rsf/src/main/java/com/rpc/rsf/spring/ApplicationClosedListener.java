package com.rpc.rsf.spring;


import java.net.InetSocketAddress;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import com.rpc.rsf.base.netty.client.RpcNettyClientHandel;
import io.netty.channel.Channel;

/**
 * spring  stop listener
 * @author 王帆
 * @date  2019年6月7日 下午11:47:09
 */
public class ApplicationClosedListener  implements ApplicationListener<ContextClosedEvent>,ApplicationContextAware{
	ApplicationContext applicationContext;
	Log log=LogFactory.getLog(getClass());
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.err.println("clear netty channel");
		Map<Long, Channel> map = RpcNettyClientHandel.getChannelMap();
		for(Channel channel:map.values()) {
			channel.close();
		}
		map.clear();
	}

}
