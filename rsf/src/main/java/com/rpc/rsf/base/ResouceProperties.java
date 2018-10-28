package com.rpc.rsf.base;

import java.io.IOException;
import java.util.Properties;

/**
 * 资源文件读写信息
 *<p>Title: ResouceProperties.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月28日
 */
public class ResouceProperties {
	static Properties properties;
	
	public static Properties getDefProperties() {
		if(properties==null) {
			try {
				properties=new Properties();
				properties.load(ResouceProperties.class.getResourceAsStream("rpc.properties"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	public static String getProperty(String key) {
		return getDefProperties().getProperty(key);
	}
}
