package com.lotusyu.permission.data.annotation;

import java.lang.annotation.*;

/**
 * 数据权限函数注解
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface DataPermissionFunction {
    /**
     * 方法名称
     * @return
     */
    String value() default "";
}
