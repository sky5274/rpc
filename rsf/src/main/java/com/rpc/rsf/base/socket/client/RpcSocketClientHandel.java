package com.rpc.rsf.base.socket.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.rpc.rsf.base.Result;
import com.rpc.rsf.base.RpcCallBack;
import com.rpc.rsf.base.RpcRequest;
import com.rpc.rsf.base.client.RpcClientHandel;
import com.rpc.rsf.provide.ProviderMethodInvoker;

public class RpcSocketClientHandel implements RpcClientHandel{
	private Log logger=LogFactory.getLog(getClass());
	@Override
	public <T> T invoke(RpcRequest request, InetSocketAddress addr,int timeout) throws Throwable {
		logger.debug("rpt client proxt mentod: "+request.getClassName()+"."+request.getMethodName());
		Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        try {
        	Map<String, Object> argMape =new HashMap<>();
            // 2.创建Socket客户端，根据指定地址连接远程服务提供者
            socket = new Socket();
			socket.connect(addr);
			socket.setSoTimeout(timeout);
			logger.debug("rpt client connect server:"+addr);
            // 3.将远程服务调用所需的接口类、方法名、参数列表等编码后发送给服务提供者
            output = new ObjectOutputStream(socket.getOutputStream());
            Object[] args = request.getArgs();
        	for(int i=0;i<args.length;i++) {
        		Object arg = args[i];
        		if(arg!=null && arg instanceof RpcCallBack) {
					argMape.put(request.getRequestId()+"_"+request.getParameterTypes()[i].getName()+"_"+i, arg);
        			args[i] = RpcCallBack.call;
        		} 
        	}
        	request.setArgs(args);
            output.writeObject(request);
            logger.debug("rpt client send msg:"+JSON.toJSONString(request));
            // 4.同步阻塞等待服务器返回应答，获取应答后返回
            input = new ObjectInputStream(socket.getInputStream());
            
            Object obj = input.readObject();
            while(obj instanceof RpcRequest) {
            	RpcRequest req=(RpcRequest) obj;
            	Object arg = argMape.get(req.getRequestId());
            	try {
					output.writeObject(new Result<>(req.getRequestId(), ProviderMethodInvoker.invoke(addr, arg, req)));
				} catch (Throwable e) {
					output.writeObject(new Result<>(req.getRequestId(), e));
				}
            	obj=input.readObject();
            }
            @SuppressWarnings("unchecked")
			Result<T> result=(Result<T>) obj;
             logger.debug("rpt client get reuslt: "+JSON.toJSONString(result));
             if(result.hasException()) {
            	 throw result.getException();
             }
            return result.getData();
        } finally {
            if (socket != null) socket.close();
            if (output != null) output.close();
            if (input != null) input.close();
        }
	}
	
}
