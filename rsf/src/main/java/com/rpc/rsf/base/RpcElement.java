package com.rpc.rsf.base;

import org.springframework.util.StringUtils;

public class RpcElement {
	private String id;
	private String type;
	private String className;
	private String group;
	private String version;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className){
		this.className = className;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String writeUrl() {
		StringBuffer sbf=new StringBuffer();
		StringBuffer err=new StringBuffer();
		if(StringUtils.isEmpty(group)) {
			err.append("/group");
		}else {
			sbf.append("/"+group);
		}
		if(StringUtils.isEmpty(type)) {
			err.append("/type");
		}else {
			sbf.append("/"+type);
		}
		if(StringUtils.isEmpty(version)) {
			err.append("/version");
		}else {
			sbf.append("/"+version);
		}
		if(err.toString().length()>0) {
			throw new IllegalArgumentException(err.toString()+"must not empty");
		}
		return sbf.toString();
	}
}
