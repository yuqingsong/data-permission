package com.lotusyu.permission.data;

import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.mapper.UserMapper;
import com.lotusyu.permission.data.service.IUserService;
import com.lotusyu.permission.data.sqlbuild.DataPermissionSqlBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@SpringBootTest(classes = TestBootApp.class)
@ComponentScan("com.lotusyu.permission.data.*")
class DataPermissionApplicationTests {

	@Resource
	IUserService userService;

	@Resource
	DataPermissionSqlBuilder sqlBuilder;

	@Resource
	UserMapper userMapper;

	@Resource
	ValueParser valueParser;

	@Test
	void contextLoads() {
		System.out.println(userService.getAll());
		System.out.println(userService.getAllWithWhere());
		System.out.println(userService.getAllWithFunction());
//		System.out.println(userService.list());
//		System.out.println(userMapper.getUserById(1L));
	}

}
