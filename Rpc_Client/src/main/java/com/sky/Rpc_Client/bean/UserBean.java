package com.sky.Rpc_Client.bean;

import java.io.Serializable;

public class UserBean implements Serializable{
	private static final long serialVersionUID = 1L;
	public UserBean(String name,String word) {
		this.name=name;
		this.word=word;
	}
	private String name;
	private String word;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public void say() {
		System.err.println(name+" say: "+word);
	}
	
}
