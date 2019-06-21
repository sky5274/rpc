package com.rpc.rsf.base.call;

import java.util.HashMap;
import java.util.Map;

public class RpcConnectCallFactory {
	public static Map<String, RpcConnectCall> callMap=new HashMap<>();
	
	private static String getNowThreadName() {
		return Thread.currentThread().getName();
	}
	
	public static void addConnectCall(RpcConnectCall call) {
		callMap.put(getNowThreadName(), call);
	}
	public static RpcConnectCall getNowConnectCall() {
		return callMap.get(getNowThreadName());
	}
	
	public static void clearConnectCall() {
		callMap.clear();
	}
	public static void removeNowConnectCall() {
		callMap.remove(getNowThreadName());
	}
}
