package com.sky.Rpc_Client;


import java.io.Serializable;
import java.net.InetSocketAddress;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rpc.rsf.base.RpcCallBack;
import com.rpc.rsf.consumer.RpcProxy;
import com.sky.Rpc_Client.bean.UserBean;
import com.sky.exception.OneException;
import com.sky.service.TestService;
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
//        UserService userserver = RpcProxy.getRemoteProxyObj(UserService.class, new InetSocketAddress("127.0.0.1", 9000));
//        try {
//        	System.out.println(userserver.getUserInfo("tome"));
//			System.out.println(userserver.getUserAddr("name"));
//		} catch (OneException e) {
//				e.printStackTrace();
//		}
       
        //spring get interface
        try {
        	 UserService server=(UserService) application.getBean(UserService.class);
			System.out.println(server.getUserInfo("tome"));
			System.err.println(server.testSevice(new testService()));
//			server.getUserAddr("sdfsd");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

class testService implements  RpcCallBack,TestService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String test(String some) {
		System.out.println("test service");
		UserBean user=new UserBean("tome", some);
		user.say();
		return user.getWord();
	}
}