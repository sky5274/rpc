package com.rpc.rsf.base.call;

import com.rpc.rsf.base.RpcRequest;

/**
 * rpc connect call back interface
 *<p>Title: RpcConnectCall.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2019年6月21日
 */
public interface RpcConnectCall {
	public Object call(RpcRequest req) throws Throwable;
}
