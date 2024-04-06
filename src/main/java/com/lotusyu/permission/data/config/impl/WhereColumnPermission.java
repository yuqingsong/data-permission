package com.lotusyu.permission.data.config.impl;

import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.config.ColumnPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 单个列权限配置
 * Description: ColumnPermission
 * Date: 2023/6/14
 * Author: yqs
 */


@EqualsAndHashCode(callSuper = false)

public class WhereColumnPermission extends DefaultColumnPermission {


    public WhereColumnPermission(String code,String table, String column, String operator, String value,String where) {
        super(code,table, column, operator, value,where);
    }

    @Override
    public String toWhere(ValueParser valueParser) {
        String where = this.where();
        return valueParser.parse(where).toString();
//        return where;
    }
}
