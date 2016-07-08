package com.gdut.dongjun.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdut.dongjun.annotation.ContextDevTest;
import com.gdut.dongjun.service.UserService;
import com.gdut.dongjun.service.impl.enums.LoginResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextDevTest
public class LoginTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void login() {
		LoginResult loginResult = userService.login("007", "123");
		System.out.println(loginResult);
	}
}
