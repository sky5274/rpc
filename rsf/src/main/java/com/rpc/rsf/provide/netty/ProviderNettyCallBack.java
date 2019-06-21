package com.rpc.rsf.provide.netty;

import java.util.concurrent.SynchronousQueue;

import com.rpc.rsf.base.Result;
import com.rpc.rsf.base.RpcRequest;
import com.rpc.rsf.base.call.RpcConnectCall;

import io.netty.channel.ChannelHandlerContext;

/**
 * netty  call back
 *<p>Title: ProviderNettyCallBack.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2019年6月21日
 */
public class ProviderNettyCallBack implements RpcConnectCall{
	ChannelHandlerContext ctx;
	String requestId;
	
	public ProviderNettyCallBack(ChannelHandlerContext ctx, String requestId) {
		this.ctx=ctx;
		this.requestId=requestId;
	}

	@Override
	public Object call(RpcRequest req) throws Throwable {
		String id = requestId+"_"+req.getClassName()+"."+req.getMethodName();
		req.setRequestId(id);
		SynchronousQueue<Result<?>> quene=new SynchronousQueue<Result<?>>();
		//代理调用参数的方法
		ctx.writeAndFlush(req);
		ProviderObjectNettyHandel.argSyncObjectMap.put(id, quene);
		Result<?> res = quene.take();
		if(res.hasException()) {
			throw res.getException();
		}
		return res.getData();
	}

}
