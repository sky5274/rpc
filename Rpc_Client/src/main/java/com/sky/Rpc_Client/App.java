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
    	final ClassPathXmlApplicationContext application =new ClassPathXmlApplicationContext("classpath:application.xml");
        System.out.println( "Hello World!" );
        //java proxy get interface
//        UserService userserver = RpcProxy.getRemoteProxyObj(UserService.class, new InetSocketAddress("127.0.0.1", 9000));
//        try {
//        	System.out.println(userserver.getUserInfo("tome"));
//			System.out.println(userserver.getUserAddr("name"));
//		} catch (OneException e) {
//				e.printStackTrace();
//		}
        active(application,"tome");
        Thread.sleep(500);
        for(int i=0;i<50;i++) {
        	final int index=i;
        	new Thread(new Runnable() {
				public void run() {
					active(application, "demo_"+index);
					
				}
			}).start();
        	Thread.sleep(500);
        }
        Thread.sleep(60*1000);
        application.close();
    }
    
    private static void active(ApplicationContext application,String param) {
    	   //spring get interface
        try {
        	UserService server=(UserService) application.getBean(UserService.class);
			System.out.println(param+" >>> server.getUserInfo >>>"+server.getUserInfo(param));
			System.err.println(param+" >>> server.testSevice >>>"+server.testSevice(new testService(param)));
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
	private String param;
	public testService(String param) {
		this.param=param;
	}

	public String test(String some) {
		System.out.println("test service");
		UserBean user=new UserBean("tome_"+param, some);
		user.say();
		return user.getWord()+param;
	}
}