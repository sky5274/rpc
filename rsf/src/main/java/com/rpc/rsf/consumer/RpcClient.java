package com.rpc.rsf.consumer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketAddress;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.rpc.rsf.base.Result;
import com.rpc.rsf.base.RpcRequest;

public class RpcClient <T>{
	private Log logger=LogFactory.getLog(getClass());
	private SocketAddress addr;
	public RpcClient(SocketAddress addr){
		this.addr=addr;
	}
	
	public T request(Object proxy, Method method, Object[] args, Class<T> serviceInterface,String interfaceImpl) throws Exception {
		logger.debug("rpt client proxt mentod: "+serviceInterface.getName()+"."+method.getName());
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        try {
            // 2.创建Socket客户端，根据指定地址连接远程服务提供者
            socket = new Socket();
            
			socket.connect(addr);
			logger.debug("rpt client connect server:"+addr);
            // 3.将远程服务调用所需的接口类、方法名、参数列表等编码后发送给服务提供者
            output = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest request=new RpcRequest(interfaceImpl==null?serviceInterface.getName():interfaceImpl,method.getName(),method.getParameterTypes(),args);
            output.writeObject(request);
            logger.debug("rpt client send msg:"+request);
            // 4.同步阻塞等待服务器返回应答，获取应答后返回
            input = new ObjectInputStream(socket.getInputStream());
             @SuppressWarnings("unchecked")
			Result<T> result=(Result<T>) input.readObject();
             logger.debug("rpt client get reuslt: "+result);
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
