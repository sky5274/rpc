package com.rpc.rsf.provide.socket;

import java.io.IOException;
import java.net.ServerSocket;

import com.rpc.rsf.provide.ProviderServer;

public class ProviderSocketServer extends ProviderServer{

	private ServerSocket server;
	public ServerSocket getServer() {
		return server;
	}
	public void setServer(ServerSocket server) {
		this.server = server;
	}
	
	public void run() {
		try {
			isOpen=true;
			server=new ServerSocket(port);
			log.info("rpc socket provider start in port: "+getPort());
			while(isOpen) {
				ProviderSocketServerTask provideServiceTask =applicationContext.getBean(ProviderSocketServerTask.class);
				 executor.execute(provideServiceTask.append(server.accept()));
			}
		} catch (IOException e) {
			log.error("rpc provirder runing error",e);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void close() {
		try {
			server.close();
		} catch (IOException e) {
			log.error("rpc privoder close error");
		}finally {
			server=null;
		}
		stop();
	}
}
