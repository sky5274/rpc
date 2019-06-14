package com.rpc.rsf.base.client;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import com.rpc.rsf.base.RpcConfig;
import com.rpc.rsf.base.RpcRequest;
import com.rpc.rsf.base.netty.client.RpcNettyClientHandel;
import com.rpc.rsf.base.socket.client.RpcSocketClientHandel;

public class RpcClient <T>{
	
	private InetSocketAddress addr;
	public RpcClient(InetSocketAddress addr){
		this.addr=addr;
	}
	
	public T request(Class<T> serviceInterface, Method method, Object[] args,String interfaceImpl) throws Throwable {
		 RpcRequest request=new RpcRequest(interfaceImpl==null?serviceInterface.getClass().getName():interfaceImpl,method.getName(),method.getParameterTypes(),args);
		 RpcClientHandel rpcClientHandel;
		 if(RpcConfig.isSocketServer()) {
			 rpcClientHandel=new RpcSocketClientHandel();
		 }else {
			 rpcClientHandel=new RpcNettyClientHandel();
		 }
		 try {
			 return  rpcClientHandel.invoke(request,addr);
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
         
        
	}
}
