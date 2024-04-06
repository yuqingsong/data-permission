package com.lotusyu.permission.data.annotation;

/**
 * 黑白名单数据权限注解
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public @interface AllowBlockList {
    /**
     * 权限白名单
     * @return
     */
    String[] allowPermission() default {};
    /**
     * 权限黑名单
     * @return
     */
    String[] blockPermission() default {};

//    /**
//     * 允许的表名
//     * @return
//     */
//    String[] allowEntity() default {};
//
//    /**
//     * 禁用的实体
//     * @return
//     */
//    String[] blockEntity() default {};


}
