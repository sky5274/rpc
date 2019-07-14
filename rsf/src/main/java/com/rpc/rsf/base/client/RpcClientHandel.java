package com.rpc.rsf.base.client;

import java.net.InetSocketAddress;
import com.rpc.rsf.base.RpcRequest;

public interface RpcClientHandel {
	public <T> T invoke(RpcRequest request, InetSocketAddress addr, int timeout) throws Throwable;
	
}
