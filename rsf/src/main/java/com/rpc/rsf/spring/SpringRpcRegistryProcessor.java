package com.rpc.rsf.spring;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SpringRpcRegistryProcessor implements BeanDefinitionRegistryPostProcessor,ApplicationContextAware{
	private Log log=LogFactory.getLog(getClass());
	ApplicationContext applicationContext;
	private String basePackage="com.sky";
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
		log.info("rpc auto init config end");
		
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry regist) throws BeansException {
		log.info("rpc init regist");
		SpringRpcConsumoerDefineScanner scan = new SpringRpcConsumoerDefineScanner(regist);
		Set<BeanDefinitionHolder> beans = scan.doScan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
		for(BeanDefinitionHolder bean:beans) {
			regist.registerBeanDefinition(bean.getBeanName(), bean.getBeanDefinition());
		}
		
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

}
