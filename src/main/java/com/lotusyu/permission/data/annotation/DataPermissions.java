package com.lotusyu.permission.data.annotation;

import java.lang.annotation.*;

/**
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermissions {
    String name() default "";
    DataPermission[] value() default {};
}
