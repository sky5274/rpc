package com.rpc.rsf.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import com.rpc.rsf.base.RpcConfig;
import com.rpc.rsf.base.RpcElement;
import com.rpc.rsf.provide.ProviderContant;
import com.rpc.rsf.provide.ProviderServer;
import com.rpc.rsf.provide.netty.ProviderNettyServer;
import com.rpc.rsf.provide.socket.ProviderSocketServer;
import com.rpc.rsf.provide.socket.ProviderSocketServerTask;

/**
 * rpc socket interface service provide parser
 *<p>Title: RpcProviderDefinitinParser.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月15日
 */
public class RpcProviderDefinitinParser implements BeanDefinitionParser{
	private Log logger=LogFactory.getLog(getClass());
	
	@SuppressWarnings("unused")
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		registProvideServer(parserContext);
		try {
			RpcElement rpcEle = getRpcElement(element);
			logger.info("rpt provider load bean name :"+rpcEle.getId()+">> class :"+rpcEle.getClassName());
			String url = rpcEle.writeUrl();
			//标注存在服务提供者
			ProviderContant.setHasProvider(true);
			
			Class<?> clazz = Class.forName(rpcEle.getClassName());
			//ProvideServer provideServer=applicationContext.getBean(ProvideServer.class);
			RpcConfig.regist(url,rpcEle.getInterfaceName(),rpcEle.getClassName(),ProviderServer.getPort());
			//return registBean(rpcEle.getId(),clazz,parserContext);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private GenericBeanDefinition registBean(String id, Class<?> intertface, ParserContext parserContext) {
		logger.info("rpt provider load bean name :"+id+">> class :"+intertface.getName());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(intertface);
		GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
		definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		parserContext.getRegistry().registerBeanDefinition(id,definition);
		return definition;
	}
	
	private void registProvideServer(ParserContext parserContext) {
		if(RpcConfig.isSocketServer()) {
			registSocketProvideService(parserContext);
		}else {
			registNettyProvideService(parserContext);
		}
		registBean(parserContext,ApplicationReadListener.class);
	}
	
	private void registBean(ParserContext parserContext,Class<?> clazz) {
		registBean(parserContext, clazz,clazz.getSimpleName());
	}
	private void registBean(ParserContext parserContext,Class<?> clazz,String clazzName) {
		try {
			if(parserContext.getRegistry().getBeanDefinition(clazzName)==null) {
				registBean(clazzName, clazz, parserContext);
			}
		} catch (Throwable e) {
			registBean(clazzName, clazz, parserContext);
		}
	}
	
	private void registNettyProvideService(ParserContext parserContext) {
		registBean(parserContext,ProviderNettyServer.class,ProviderServer.class.getSimpleName());
		
	}

	private void registSocketProvideService(ParserContext parserContext) {
		registBean(parserContext,ProviderSocketServer.class,ProviderServer.class.getSimpleName());
		registBean(parserContext,ProviderSocketServerTask.class);
	}
	
	private RpcElement getRpcElement(Element element) throws ClassNotFoundException {
		RpcElement rpcEle=new RpcElement();
		rpcEle.setType("provider");
		rpcEle.setId(element.getAttribute("id"));
		rpcEle.setClassName(element.getAttribute("class"));
		rpcEle.setInterfaceName(element.getAttribute("interface"));
		rpcEle.setGroup(element.getAttribute("group"));
		rpcEle.setVersion(element.getAttribute("version"));
		return rpcEle;
	}
}
