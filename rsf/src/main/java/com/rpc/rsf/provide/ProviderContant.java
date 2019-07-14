package com.rpc.rsf.provide;

public class ProviderContant {
	public volatile static boolean hasProvider=false;
	
	public synchronized static boolean isHasProvider() {
		return hasProvider;
	}
	public synchronized static void setHasProvider(boolean hasProvider) {
		ProviderContant.hasProvider=hasProvider;
	}
}
