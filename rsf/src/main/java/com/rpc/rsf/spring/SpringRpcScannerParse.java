package com.rpc.rsf.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class SpringRpcScannerParse implements BeanDefinitionParser{
	private String ATTR_PACKAGE="base-package";
	
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		SpringRpcProviderDefineScanner register = new SpringRpcProviderDefineScanner(parserContext.getRegistry());
		register.doScan(element.getAttribute(ATTR_PACKAGE));
		return null;
	}

}
