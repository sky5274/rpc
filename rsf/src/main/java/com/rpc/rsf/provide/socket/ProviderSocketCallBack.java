package com.rpc.rsf.provide.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.rpc.rsf.base.Result;
import com.rpc.rsf.base.RpcRequest;
import com.rpc.rsf.base.call.RpcConnectCall;

/**
 * socket rpc call back
 *<p>Title: ProviderSocketCallBack.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2019年6月21日
 */
public class ProviderSocketCallBack implements RpcConnectCall{
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String requestId;
	public ProviderSocketCallBack(ObjectInputStream input, ObjectOutputStream output, String requestId) {
		this.input=input;
		this.output=output;
	}

	@Override
	public Object call(RpcRequest req) throws Throwable {
		String id = requestId+"_"+req.getClassName()+"_"+req.getMethodName();
		req.setRequestId(id);
		//代理调用参数的方法
		output.writeObject(req);
		Result<?> res = (Result<?>) input.readObject();
		if(res.hasException()) {
			throw res.getException();
		}
		return res.getData();
	}

}
