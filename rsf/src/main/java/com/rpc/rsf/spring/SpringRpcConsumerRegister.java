package com.rpc.rsf.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
public class SpringRpcConsumerRegister implements ImportBeanDefinitionRegistrar {
	private Log log=LogFactory.getLog(SpringRpcConsumerRegister.class);
	
	public void registerBeanDefinitions(AnnotationMetadata arg0, BeanDefinitionRegistry arg1) {
		log.info("rpc regist start");
		
	}

}
