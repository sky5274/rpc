package com.rpc.rsf.provide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.alibaba.fastjson.JSON;
import com.rpc.rsf.base.Result;
import com.rpc.rsf.base.RpcRequest;


/**
 * 服务任务(任务的传送对象必须在提供者和消费者都存在)
 *<p>Title: ServiceTask.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月13日
 */
public class ProviderServiceTask implements Runnable ,ApplicationContextAware{
	private Socket client;
	private ApplicationContext applicationContext;
	private static  Log log=LogFactory.getLog(ProviderServiceTask.class);

	public void run() {
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		try {
			// 2.将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
			input = new ObjectInputStream(client.getInputStream());
			RpcRequest request = (RpcRequest) input.readObject();
			log.info(">>get request:"+JSON.toJSONString(request));
			Object bean = applicationContext.getBean(Class.forName(request.getClassName()));
			if (bean == null) {
				throw new ClassNotFoundException(request.getClassName() + " not found");
			}

			// 3.将执行结果反序列化，通过socket发送给客户端
			output = new ObjectOutputStream(client.getOutputStream());
			Class<?> serviceClass=bean.getClass();
			Method method = serviceClass.getMethod(request.getMethodName(), request.getParameterTypes());
			try {
				long starttime = System.currentTimeMillis();
				Object result = method.invoke(serviceClass.newInstance(), request.getArgs());
				long endtime = System.currentTimeMillis();
				log.info(String.format(serviceClass.getName()+"."+method.getName()+" cost time: %ds",(endtime-starttime)/1000));
				output.writeObject(new Result<Object>(result));
			} catch (Exception e) {
				output.writeObject(new Result<Object>(e));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (client != null) {
					try {
						client.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}
	
	public ProviderServiceTask append(Socket client) {
		this.client=client;
		return this;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

}
