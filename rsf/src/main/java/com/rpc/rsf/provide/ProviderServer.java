package com.rpc.rsf.provide;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import com.rpc.rsf.base.ResouceProperties;

/**
 * 服务端
 *<p>Title: ServerManager.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月13日
 */
public class ProvideServer implements ApplicationContextAware{
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private Log log=LogFactory.getLog(getClass());
	private int port=9000;
	private boolean isOpen=true;
	private ServerSocket server;
	private ApplicationContext applicationContext;
	private String portKey="rpc.provider.server.port";
	
	public ProvideServer() {
		String sys_port = System.getProperty(portKey);
		if(StringUtils.isEmpty(sys_port)) {
			String port_str = ResouceProperties.getProperty(portKey);
			if(port_str!=null) {
				sys_port=port_str;
			}
		}
		if(!StringUtils.isEmpty(sys_port)) {
			try {
				port=Integer.valueOf(sys_port);
				System.setProperty(portKey, sys_port);
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}
	};
	public ProvideServer(int port) {
		this.port=port;
	};
	
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public ServerSocket getServer() {
		return server;
	}
	public void setServer(ServerSocket server) {
		this.server = server;
	}
	public void start() {
		try {
			isOpen=true;
			server=new ServerSocket(port);
			log.info("rpc provider start , the port:"+port);
			while(isOpen) {
				ProvideServiceTask provideServiceTask =applicationContext.getBean(ProvideServiceTask.class);
				 executor.execute(provideServiceTask.append(server.accept()));
			}
		} catch (IOException e) {
			log.error("rpc provirder runing error",e);
		}
	}
	
	public void stop() {
		try {
			server.close();
		} catch (IOException e) {
			log.error("rpc privoder close error");
		}finally {
			server=null;
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	
}
