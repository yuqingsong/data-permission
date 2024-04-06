package com.lotusyu.permission.data.service;

import com.lotusyu.permission.data.TestBootApp;
import com.lotusyu.permission.data.mapper.UserMapper;
import com.lotusyu.permission.data.service.impl.IUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@SpringBootTest(classes = TestBootApp.class)
class IUserServiceTest {

	@Resource
	IUserService userService;

	@Resource
	UserMapper userMapper;


	@Resource
	SimpleService simpleService;

	@Test
	void contextLoads() {
		System.out.println("abcd");
//		System.out.println(userService.list());
//		System.out.println(userMapper.getUserById(1L));
		System.out.println(userService.getAll());
//		simpleService.hello();
	}


	@Test
	void ana(){
		Method[] methods = IUserServiceImpl.class.getMethods();
		System.out.println(methods);
	}
}
