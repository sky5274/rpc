package com.sky.Rpc_Server;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;
import com.rpc.rsf.provide.ProviderServer;

/**
 * demo to rpc provide
 *
 */
public class App 
{
    public static void main( String[] args )
    {	
    	Map<String, String> argMap=new HashMap<String, String>();
    	for(String str:args) {
    		if(str.startsWith("-")) {
    			String[] kv = str.replaceAll("-", "").split("=");
    			argMap.put(kv[0], kv[1]);
    		}
    	}
    	String def_port="9008";
    	if(!StringUtils.isEmpty(argMap.get("port"))) {
    		def_port=argMap.get("port");
    	}
    	System.setProperty("rpc.provider.server.port", def_port);
    	String port = System.getProperty("rpc.provider.server.port");
    	System.err.println("rpc port def:"+port);
    	ApplicationContext application =new ClassPathXmlApplicationContext("classpath:application.xml");
        ProviderServer provideServer = application.getBean(ProviderServer.class);
        provideServer.start();
    }
}
