package com.rpc.rsf.consumer;

import org.springframework.beans.factory.FactoryBean;
import com.rpc.rsf.base.RpcElement;

public class ProxyFactory<T> implements FactoryBean<T> {
	private Class<T> interfaceClass;
	private RpcElement node;
	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	public T getObject() throws Exception {
		return (T) RpcProxy.getRemoteProxyObj(interfaceClass,node);
	}

	public Class<?> getObjectType() {
		return interfaceClass;
	}

	public boolean isSingleton() {
		// 单例模式
		return true;
	}
	public RpcElement getNode() {
		return node;
	}
	public void setNode(RpcElement node) {
		this.node = node;
	}
}