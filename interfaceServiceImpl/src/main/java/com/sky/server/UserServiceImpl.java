package com.sky.server;

import org.springframework.stereotype.Service;

import com.sky.exception.OneException;
import com.sky.service.TestService;
import com.sky.service.UserService;

/**
 * Hello world!
 *
 */
@Service
public class UserServiceImpl implements UserService
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

	public String getUserInfo(String name) {
		System.out.println("some one action");
		return "your name:"+name+" pwd：问问";
	}

	public String getUserAddr(String name) throws OneException {
		throw new OneException("exception test");
		//throw new Exception("exception test");
	}

	public String testSevice(TestService test) {
		System.err.println("the testservice is： "+test);
		return test.test(getUserInfo("test"));
	}
}
