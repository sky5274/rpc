package com.rpc.rsf.spring;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import com.rpc.rsf.base.RpcConfig;
import com.rpc.rsf.base.RpcProvider;
import com.rpc.rsf.provide.ProviderServer;
import com.rpc.rsf.provide.ProviderServiceFactory;
import com.rpc.rsf.provide.ProviderServiceTask;

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
		registerDefaultFilters();
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		if (beanDefinitions.isEmpty()) {
			logger.warn("No rpc interface was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
		} else {
			processBeanDefinitions(beanDefinitions);
		}
		return beanDefinitions;
	}

	@Override
	public void registerDefaultFilters() {
		// 添加需扫描的RpcConsumer Class
		this.addIncludeFilter(new AnnotationTypeFilter(RpcProvider.class));
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		if(beanDefinitions !=null &&  !beanDefinitions.isEmpty()) {
			for(BeanDefinitionHolder beand:beanDefinitions) {
				logger.info("getBean: { id: "+beand.getBeanName()+", class ："+beand.getBeanDefinition().getBeanClassName()+"}");
				registRpcProvider(beand);
			}
			try {
				if(this.registry.getBeanDefinition(ProviderServer.class.getSimpleName())==null) {
					registBean(ProviderServiceTask.class.getSimpleName(), ProviderServiceTask.class);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
	}
	
	/**
	 * 
	 * 注册rpc 服务提供者
	* <p>Title: registRpcProvider</p>
	* <p>Description: </p>
	* @param bean
	 */
	private void registRpcProvider(BeanDefinitionHolder bean) {
		try {
			String className = bean.getBeanDefinition().getBeanClassName();
			Class<?> clazz = Class.forName(className);
			RpcProvider provider = clazz.getDeclaredAnnotation(RpcProvider.class);
			String url = getNodeUrl(provider);
			RpcConfig.regist(url, className, ProviderServiceFactory.getPort());
		} catch (ClassNotFoundException e) {
			logger.warn(e.getMessage(),e);
		} catch (Exception e) {
			logger.warn(e.getMessage(),e);
		}
	}
	private String getNodeUrl(RpcProvider provider) {
		StringBuilder str=new StringBuilder();
		str.append("/").append(provider.group()).append("/provider").append("/").append(provider.version());
		return str.toString();
	}
	
	private void registBean(String id, Class<?> intertface) {
		logger.info("rpt provider load bean name :"+id+">> class :"+intertface.getName());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(intertface);
		GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
		definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		this.registry.registerBeanDefinition(id,definition);
	}
	
	public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
	      return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata().hasAnnotation(RpcProvider.class.getName());
	}

}
