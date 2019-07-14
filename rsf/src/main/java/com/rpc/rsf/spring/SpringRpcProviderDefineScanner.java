package com.rpc.rsf.spring;

import java.util.Arrays;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.stereotype.Component;
import com.rpc.rsf.base.RpcConfig;
import com.rpc.rsf.provide.ProviderServer;
import com.rpc.rsf.provide.netty.ProviderNettyServer;
import com.rpc.rsf.provide.socket.ProviderSocketServer;
import com.rpc.rsf.provide.socket.ProviderSocketServerTask;

@Component
public class SpringRpcProviderDefineScanner extends ClassPathBeanDefinitionScanner {
	private BeanDefinitionRegistry registry;

	public SpringRpcProviderDefineScanner(BeanDefinitionRegistry registry) {
		super(registry,false);
		this.registry=registry;
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		logger.debug("rpc scanner: "+Arrays.toString(basePackages));
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		//注册服务
		registServer();
		
		//rpc consumer/provider bean configration
		RpcReadyRegistInfoConfigration config=new RpcReadyRegistInfoConfigration();
		config.setRegistry(registry);
		config.init();
		
		return beanDefinitions;
	}


	
	private void registServer() {
		if(RpcConfig.isSocketServer()) {
			registSocketProvideService();
		}else {
			registNettyProvideService();
		}
		regisDefindetBean(ApplicationReadListener.class);
		regisDefindetBean(ApplicationClosedListener.class);
	}
	
	private void regisDefindetBean(Class<?> clazz) {
		regisDefindetBean( clazz,clazz.getSimpleName());
	}
	private void regisDefindetBean(Class<?> clazz,String clazzName) {
		try {
			if(registry.getBeanDefinition(clazzName)==null) {
				registBean(clazzName, clazz);
			}
		} catch (Throwable e) {
			registBean(clazzName, clazz);
		}
	}
	
	private void registNettyProvideService() {
		regisDefindetBean(ProviderNettyServer.class,ProviderServer.class.getSimpleName());
		
	}

	private void registSocketProvideService() {
		regisDefindetBean(ProviderSocketServer.class,ProviderServer.class.getSimpleName());
		regisDefindetBean(ProviderSocketServerTask.class);
	}
	
	/**
	 * 
	 * 注册rpc 服务提供者
	* <p>Title: registRpcProvider</p>
	* <p>Description: </p>
	* @param bean
	 */
	
	private void registBean(String id, Class<?> intertface) {
		logger.info("rpt provider load bean name :"+id+">> class :"+intertface.getName());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(intertface);
		GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
		definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		this.registry.registerBeanDefinition(id,definition);
	}
	

}
