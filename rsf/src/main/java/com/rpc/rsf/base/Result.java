package com.rpc.rsf.base;

import java.io.Serializable;

public class Result <T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean hasExp=false;
	private T data;
	private Exception exp;
	
	public Result(T res){
		this.data=res;
	}
	
	public Result(Exception e) {
		hasExp=true;
		this.exp=e;
	}
	
	public boolean hasException() {
		return hasExp;
	}

	public T getData() {
		return data;
	}
	public Exception getException() {
		return exp;
	}
}
