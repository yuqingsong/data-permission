package com.lotusyu.permission.data.service.impl;

import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.annotation.DataPermissions;
import com.lotusyu.permission.data.service.SimpleService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @Auther: yqs
 * @Date: 2023/6/1
 * @Description:
 */
@Component
public class SimpleServiceImpl implements SimpleService {

    @DataPermissions
    public void hello() {
        System.out.println("hello");
    }
}
