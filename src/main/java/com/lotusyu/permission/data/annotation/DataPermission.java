package com.lotusyu.permission.data.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DataPermissions.class)
public @interface DataPermission {
    /**
     * 数据权限名称
     * @return
     */
    String name() default "";
    /**
     * 实体（表名）
     * @return
     */
    String table() default "";

    /**
     * 维度（列名）
     * @return
     */
    String column() default "";


    /**
     * 操作符
     * @return
     */
    String operator() default "";

    /**
     * 值
     * @return
     */
    String value() default "";

    /**
     * where条件，当有where有值时，会忽略 value operator的值。where的优先级>value和operator
     * @return
     */
    String where() default "";
}
