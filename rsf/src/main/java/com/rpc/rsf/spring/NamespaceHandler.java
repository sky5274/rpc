package com.rpc.rsf.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 自定义spring标签名响应
 *<p>Title: NamespaceHandler.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月15日
 */
public class NamespaceHandler extends NamespaceHandlerSupport {
	public void init() {
		//注册用于解析<rpc:scan>的解析器
		registerBeanDefinitionParser("scan", new SpringRpcScannerParse());
		//注册用于解析<rpc:consumer>的解析器
		registerBeanDefinitionParser("consumer", new RpcConsumerDefinitionParser());
		//注册用于解析<rpc:provider>的解析器
		registerBeanDefinitionParser("provider", new RpcProviderDefinitinParser());
		
	}
}
