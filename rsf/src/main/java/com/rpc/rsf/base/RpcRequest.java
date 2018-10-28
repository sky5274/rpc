package com.rpc.rsf.base;

import java.io.Serializable;

/**
 * rpc client request body
 *<p>Title: RpcRequest.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月15日
 */

public class RpcRequest implements Serializable{
	
	/***/
	private static final long serialVersionUID = 1L;
	/**类名*/
	private String className;
	/**方法名*/
	private String methodName;
	/**方法参数*/
	private Object[] args;
	/**方法参数类型*/
	private Class<?>[] parameterTypes;
	public RpcRequest() {}
	public RpcRequest(String className, String methodName, Class<?>[] parameterTypes, Object[] args) throws ClassNotFoundException {
		this.className=className;
		this.methodName=methodName;
		this.setParameterTypes(parameterTypes);
		this.args=args;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
}
