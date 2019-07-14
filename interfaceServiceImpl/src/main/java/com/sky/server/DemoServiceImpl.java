package com.sky.server;


import com.rpc.rsf.annotation.RpcProvider;
import com.sky.service.TestService;

@RpcProvider(clazz=TestService.class,group="test",version="1-2")
public class DemoServiceImpl implements TestService{

	public String test(String some) {
		
		return some+" test";
	}

}
