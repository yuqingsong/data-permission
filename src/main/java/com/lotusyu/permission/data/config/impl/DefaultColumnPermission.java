package com.lotusyu.permission.data.config.impl;

import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.config.ColumnPermission;
import lombok.AllArgsConstructor;
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
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DefaultColumnPermission implements ColumnPermission {

    private String code;

    final private String table;

    final private String column;

    final private String operator;

    final private String value;

    private String where;




    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String table() {
        return this.table;
    }

    @Override
    public String column() {
        return this.column;
    }

    @Override
    public String operator() {
        return this.operator;
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String where() {
        return this.where;
    }

    @Override
    public String toWhere(ValueParser valueParser) {
        ColumnPermission dataPermission = this;
        String column = dataPermission.table()+"."+dataPermission.column();
        String operator = dataPermission.operator();
        if(StringUtils.isEmpty(operator)){
            operator = "=";
        }
        String value = dataPermission.value();

        Object parse = valueParser.parse(value);

        String sqlValue = null;

        if(parse instanceof Number){
            if( parse instanceof Float || parse instanceof Double){
                sqlValue = String.valueOf(((Number) parse).doubleValue());
            }else{
                sqlValue =String.valueOf(((Number) parse).longValue());
            }
        }else{
            //FIXME 处理数组等其他类型,
            if("in".equals(operator)){
                sqlValue = (String) parse;
            }else{
                sqlValue = "\'"+(String) parse+"\'";
            }
        }
        return column+" "+ operator + " " + sqlValue;

    }
}
