package com.rpc.rsf.spring;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;


public class SpringRpcConsumoerDefineScanner extends ClassPathBeanDefinitionScanner {


	public SpringRpcConsumoerDefineScanner(BeanDefinitionRegistry registry) {
		super(registry,false);
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
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
		this.addIncludeFilter(new AnnotationTypeFilter(Component.class));
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		logger.info("rpc class scan start");
		for(BeanDefinitionHolder beand:beanDefinitions) {
			logger.info(beand.getBeanName());
			logger.info(beand.getSource().getClass());
			logger.info(beand.getBeanDefinition().getBeanClassName());
		}

	}

}
