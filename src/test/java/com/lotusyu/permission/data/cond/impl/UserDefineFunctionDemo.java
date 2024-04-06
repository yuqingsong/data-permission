package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.annotation.DataPermissionFunction;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Description: UserDefineFunctionDemo
 * Date: 2023/6/19
 * Author: yqs
 */

@Component
@DataPermissionFunction(value = UserDefinePermissionFunctionProviderTest.CURRENT_USER_ID_FUN)
public class UserDefineFunctionDemo implements Function<String,Object> {

    @Override
    public Object apply(String valueExpress) {
        //当前用户ID是123
        return UserDefinePermissionFunctionProviderTest.CURRENT_USER_ID;
    }

}
