package com.lotusyu.permission.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.cond.impl.UserDefineFunctionDemo;
import com.lotusyu.permission.data.cond.impl.UserDefinePermissionFunctionProviderTest;
import com.lotusyu.permission.data.po.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 在方法上加上数据权限的注解的示例
 * @Auther: yqs
 * @Date: 2023/6/1
 * @Description:
 */
@Service
@DataPermission
public interface IUserService extends IService<User> {

    /**
     * 执行SQL时会加上条件 name in （'lisi','def')
     * @return
     */
    @DataPermission(table = "user", column = "name", operator = "in", value = "('lisi','def')")
    List<User> getAll();

    @DataPermission(table = "user", where = " name in ('where1','where2')")
    List<User> getAllWithWhere();

    /**
     * 执行SQL时会加上条件 name in （CURRENT_USER_ID_FUN函数的执行结果)
     * @return
     */
    @DataPermission(table = "user", column = "name", operator = "in", value = UserDefinePermissionFunctionProviderTest.CURRENT_USER_ID_FUN)
    List<User> getAllWithFunction();

}
