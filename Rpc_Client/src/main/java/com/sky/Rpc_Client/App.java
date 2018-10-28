package com.sky.Rpc_Client;


import java.net.InetSocketAddress;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rpc.rsf.consumer.RpcProxy;
import com.sky.exception.OneException;
import com.sky.service.UserService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	ApplicationContext application =new ClassPathXmlApplicationContext("classpath:application.xml");
        System.out.println( "Hello World!" );
        //java proxy get interface
        UserService userserver = RpcProxy.getRemoteProxyObj(UserService.class, new InetSocketAddress("127.0.0.1", 9000));
        try {
        	System.out.println(userserver.getUserInfo("tome"));
			System.out.println(userserver.getUserAddr("name"));
		} catch (OneException e) {
				e.printStackTrace();
		}
       
        //spring get interface
        try {
        	 UserService server=(UserService) application.getBean(UserService.class);
			System.out.println(server.getUserInfo("tome"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
