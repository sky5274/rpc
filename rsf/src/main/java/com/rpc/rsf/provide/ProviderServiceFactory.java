package com.rpc.rsf.provide;

public class ProviderServiceFactory {
	private static Integer port;
	
	public static Integer getPort() {
		if(port==null) {
			port=new ProviderServer().getPort();
		}
		return port;
	}
}
