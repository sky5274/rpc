package com.rpc.rsf.consumer;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.FactoryBean;

import com.alibaba.fastjson.JSON;
import com.rpc.rsf.base.RpcConfig;
import com.rpc.rsf.base.RpcConfig.nodeData;

public class ProxyFactory<T> implements FactoryBean<T> {
	private static  Log log=LogFactory.getLog(ProxyFactory.class);
	private Class<T> interfaceClass;
	private String interfaceImpl;
	private String mapperUrl;
	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	public T getObject() throws Exception {
		nodeData ip = getRandomIp();
		log.debug("rpc proxy get random server:"+JSON.toJSONString(ip));
		if(ip!=null) {
			return (T) RpcProxy.getRemoteProxyObj(interfaceClass,new InetSocketAddress(ip.getIp(),ip.getPort()),interfaceImpl);
		}else {
			return null;
		}
	}

	public Class<?> getObjectType() {
		return interfaceClass;
	}

	public boolean isSingleton() {
		// 单例模式
		return true;
	}
	
	public nodeData getRandomIp() throws KeeperException, InterruptedException, IOException {
		return RpcConfig.getRandomServer(mapperUrl);
	}
	public String getMapperUrl() {
		return mapperUrl;
	}
	public void setMapperUrl(String mapperUrl) {
		this.mapperUrl = mapperUrl;
	}
	public String getInterfaceImpl() {
		return interfaceImpl;
	}
	public void setInterfaceImpl(String interfaceImpl) {
		this.interfaceImpl = interfaceImpl;
	}
}