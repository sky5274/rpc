package com.rpc.rsf.provide;

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
public abstract class ProviderServer extends Thread implements ApplicationContextAware{
	protected static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	protected Log log=LogFactory.getLog(getClass());
	protected static Integer port;
	protected boolean isOpen=true;
	protected ApplicationContext applicationContext;
	private static String portKey="rpc.provider.server.port";
	
	public ProviderServer() {
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
	public ProviderServer(int port) {
		setPort(port+"");
	};
	
	
	
	public static int getPort() {
		if(ProviderServer.port==null) {
			String sys_port = System.getProperty(portKey);
			if(StringUtils.isEmpty(sys_port)) {
				String port_str = ResouceProperties.getProperty(portKey);
				if(port_str!=null) {
					sys_port=port_str;
				}
			}
			setPort(sys_port);
		}
		return ProviderServer.port;
	}
	
	public  static void setPort(String sys_port) {
		if(StringUtils.isEmpty(sys_port)) {
			sys_port="9000";
		}
		try {
			ProviderServer.port=Integer.valueOf(sys_port);
			System.setProperty(portKey, sys_port);
		} catch (Exception e) {
		}
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public abstract void close();
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	
	
}
