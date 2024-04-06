package com.lotusyu.permission.data.service;

import com.lotusyu.permission.data.annotation.DataPermission;

/**
 * @Auther: yqs
 * @Date: 2023/6/1
 * @Description:
 */
public interface SimpleService {

    @DataPermission
    void hello();
}
