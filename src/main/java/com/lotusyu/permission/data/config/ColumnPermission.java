package com.lotusyu.permission.data.config;

import com.lotusyu.permission.data.cond.ValueParser;

/**
 * 列权限配置
 * Description: ColumnPermission
 * Date: 2023/6/14
 * Author: yqs
 */

public interface ColumnPermission {

    /**
     * 权限编码，可以为空
     * @return
     */
    String code();

    /**
     * 表名
     * @return
     */
    String table();

    /**
     * 列名
     * @return
     */
    String column();

    /**
     * 关系操作符（in and or > <等）
     * @return
     */
    String operator();

    /**
     * 列对应的条件值，值可以是一个表达式
     * @return
     */
    String value();

    String where();

    String toWhere(ValueParser parser);
}
