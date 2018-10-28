package com.sky.Rpc_Server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rpc.rsf.provide.ProvideServer;

/**
 * demo to rpc provide
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext application =new ClassPathXmlApplicationContext("classpath:application.xml");
        ProvideServer provideServer = application.getBean(ProvideServer.class);
        provideServer.start();
    }
}
