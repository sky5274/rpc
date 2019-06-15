package com.rpc.rsf.spring;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.w3c.dom.Element;
import com.alibaba.fastjson.JSON;
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
		registBean(parserContext,ApplicationClosedListener.class);
		RpcElement rpcEle = getRpcElement(element);
		logger.info("rpt consumer config regist bean:" +JSON.toJSONString(rpcEle));
		Class<?> clazz;
		try {
			clazz = Class.forName(rpcEle.getClassName());
//			List<String> serverlist = RpcConfig.getInitServerUrl(rpcEle.writeUrl());
//			if(serverlist.isEmpty()) {
//				throw new Exception("no server can consumer:"+rpcEle.getClassName());
//			}
//			BeanDefinition bean=null;
//			for(String server:serverlist) {
//				nodeData node = RpcConfig.getRandomServer(server);
//				if(node!=null) {
//					bean=registBean(node.getClassName().substring(node.getClassName().lastIndexOf("."), node.getClassName().length()),clazz,node.getClassName(),server,parserContext);
//				}
//			}
			return registBean(rpcEle.getClassName().substring(rpcEle.getClassName().lastIndexOf(".")+1, rpcEle.getClassName().length()),clazz,rpcEle,parserContext);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void registBean(ParserContext parserContext,Class<?> clazz) {
		try {
			if(parserContext.getRegistry().getBeanDefinition(clazz.getSimpleName())==null) {
				registBean(clazz.getSimpleName(), clazz, parserContext);
			}
		} catch (Throwable e) {
			registBean(clazz.getSimpleName(), clazz, parserContext);
		}
	}
	
	private GenericBeanDefinition registBean(String id, Class<?> intertface, ParserContext parserContext) {
		logger.info("rpt consumer load bean name :"+id+">> class :"+intertface.getName());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(intertface);
		GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
		definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		parserContext.getRegistry().registerBeanDefinition(id,definition);
		return definition;
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
	private GenericBeanDefinition registBean(String id, Class<?> intertface,RpcElement node, ParserContext parserContext) {
		logger.info("rpt load interface id :"+id+">> class :"+intertface.getName());
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(intertface);
		GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
		definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
		definition.getPropertyValues().add("node", node);
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
		rpcEle.setTarget(element.getAttribute("target"));
		rpcEle.setGroup(element.getAttribute("group"));
		rpcEle.setVersion(element.getAttribute("version"));
		return rpcEle;
	}

}