package com.rpc.rsf.spring;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import com.rpc.rsf.provide.ProviderServer;

/**
 * spring  start listener
 * @author 王帆
 * @date  2019年6月7日 下午11:47:09
 */
public class ApplicationReadListener  implements ApplicationListener<ContextRefreshedEvent>,ApplicationContextAware{
	ApplicationContext applicationContext;
	Log log=LogFactory.getLog(getClass());
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ProviderServer server = applicationContext.getBean(ProviderServer.class);
		if(server!=null) {
			log.info("rpc server start in port:"+ProviderServer.getPort());
			server.start();
		}
	}

}
