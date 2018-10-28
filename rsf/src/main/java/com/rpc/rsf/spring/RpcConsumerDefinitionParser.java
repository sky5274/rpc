package com.rpc.rsf.spring;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.w3c.dom.Element;

import com.rpc.rsf.base.RpcConfig;
import com.rpc.rsf.base.RpcConfig.nodeData;
import com.rpc.rsf.base.RpcElement;
import com.rpc.rsf.consumer.ProxyFactory;

/**
 * rpc consumer bean praser
 *<p>Title: RpcConsumerDefinitionParser.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月15日
 */
public class RpcConsumerDefinitionParser implements BeanDefinitionParser  {
	private Log logger=LogFactory.getLog(getClass());

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RpcElement rpcEle = getRpcElement(element);
		logger.info("rpt client config regist:" +rpcEle.writeUrl());
		Class<?> clazz;
		try {
			clazz = Class.forName(rpcEle.getClassName());
			List<String> serverlist = RpcConfig.getInitServerUrl(rpcEle.writeUrl());
			if(serverlist.isEmpty()) {
				throw new Exception("no server can consumer:"+rpcEle.getClassName());
			}
			BeanDefinition bean=null;
			for(String server:serverlist) {
				nodeData node = RpcConfig.getRandomServer(server);
				if(node!=null) {
					bean=registBean(node.getClassName().substring(node.getClassName().lastIndexOf("."), node.getClassName().length()),clazz,node.getClassName(),server,parserContext);
				}
			}
			return bean;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 注册bean
	* <p>Title: registBean</p>
	* <p>Description: </p>
	* @param id
	* @param intertface
	* @param parserContext
	* @return
	 */
	private GenericBeanDefinition registBean(String id, Class<?> intertface,String interfaceImpl,String mapperUrl, ParserContext parserContext) {
		logger.info("rpt load interface id :"+id+">> class :"+intertface.getName());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(intertface);
		GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
		definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
		definition.getPropertyValues().add("interfaceImpl", interfaceImpl);
		definition.getPropertyValues().add("mapperUrl", mapperUrl);
		definition.setBeanClass(ProxyFactory.class);
		definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		parserContext.getRegistry().registerBeanDefinition(id,definition);
		return definition;
	}
	
	private RpcElement getRpcElement(Element element) {
		RpcElement rpcEle=new RpcElement();
		rpcEle.setType("provider");
		rpcEle.setId(element.getAttribute("id"));
		rpcEle.setClassName(element.getAttribute("class"));
		rpcEle.setGroup(element.getAttribute("group"));
		rpcEle.setVersion(element.getAttribute("version"));
		return rpcEle;
	}

}