package com.rpc.rsf.base.call;

import java.util.HashMap;
import java.util.Map;
import com.rpc.rsf.base.RpcRequest;

/**
 * rpc connect call back factory
 *<p>Title: RpcConnectCallFactory.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2019年6月21日
 */
public class RpcConnectCallFactory implements RpcConnectCall{
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

	@Override
	public Object call(RpcRequest req) throws Throwable {
		RpcConnectCall call = getNowConnectCall();
		if(call==null) {
			throw new Exception("未发现线程rpc回调链接");
		}
		return call.call(req);
	}
}
