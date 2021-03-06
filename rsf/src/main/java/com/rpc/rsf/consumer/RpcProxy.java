package com.rpc.rsf.consumer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.FactoryBean;

import com.rpc.rsf.base.RpcConfig;
import com.rpc.rsf.base.RpcElement;
import com.rpc.rsf.base.RpcConfig.nodeData;
import com.rpc.rsf.base.client.RpcClient;

/**
 * rpc client proxy
 *<p>Title: RpcProxy.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月19日
 */
public class RpcProxy{
	@SuppressWarnings("unchecked")
	public static <T> T getRemoteProxyObj(final Class<T> serviceInterface, final InetSocketAddress addr) {
		// 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
		return getRemoteProxyObj(serviceInterface,addr,null);
	}
	@SuppressWarnings("unchecked")
	public static <T> T getRemoteProxyObj(final Class<T> serviceInterface, final InetSocketAddress addr,final String interfaceImpl) {
		// 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
		return (T) Proxy.newProxyInstance(FactoryBean.class.getClassLoader(), new Class<?>[]{serviceInterface},
				new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				try {
					return new RpcClient<T>(addr).request(serviceInterface,method,args,interfaceImpl);
				} catch (InvocationTargetException e){
					//抛出造成的异常
					throw e.getCause();
				}
			}
			
		});
	}
	@SuppressWarnings("unchecked")
	public static <T> T getNullProxyObj(final Class<?>... serviceInterfaces) {
		// 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
		return (T) Proxy.newProxyInstance(FactoryBean.class.getClassLoader(), serviceInterfaces,
				new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return null;
			}
			
		});
	}
	@SuppressWarnings("unchecked")
	public static <T> T getRemoteProxyObj(final Class<T> serviceInterface, final RpcElement node) {
		// 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
				return (T) Proxy.newProxyInstance(FactoryBean.class.getClassLoader(), new Class<?>[]{serviceInterface},new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						nodeData ip = getRandomIp(node);
						if(ip!=null) {
							try {
								return new RpcClient<T>(new InetSocketAddress(ip.getIp(), ip.getPort()),node.getTimeout()).request(serviceInterface,method,args,ip.getClassName());
							} catch (InvocationTargetException e){
								//抛出造成的异常
								throw e.getCause();
							}
						}else {
							throw new Exception("rpc client get no server provider for class:"+serviceInterface.getName());
						}
					}
					
				});
	}
	
	public static nodeData getRandomIp(RpcElement node) throws KeeperException, InterruptedException, IOException {
		return RpcConfig.getRandomServer(node.writeUrl()+"/"+node.getClassName(),node.getTarget());
	}
}
