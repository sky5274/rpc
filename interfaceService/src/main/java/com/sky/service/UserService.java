package com.sky.service;

import org.springframework.stereotype.Component;

import com.sky.exception.OneException;

/**
 * 测试接口
 *<p>Title: UserService.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: </p>
 * @author sky
 * @date 2018年10月13日
 */
@Component
public interface UserService {
	
	public String getUserInfo(String name);
	public String getUserAddr(String name) throws OneException;
	
	public String testSevice(TestService test);
}