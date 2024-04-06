package com.lotusyu.permission.data.cond.impl;


import com.lotusyu.permission.data.cond.TableConditionConverter;
import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.config.ColumnPermission;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将权限配置转换成SQL条件
 * Description: TableConditionConverterImpl
 * Date: 2023/6/14
 * Author: yqs
 */


@AllArgsConstructor
public class TableConditionConverterImpl implements TableConditionConverter {

    @Resource
    private ValueParser valueParser;
    @Override
    public String convertToWhere(List<ColumnPermission> permissions) {
        String collect = permissions.stream().map(dataPermission -> "(" + stringCondition(dataPermission) + ")").collect(Collectors.joining(" and"));
//        System.out.println("where:"+collect);
        return collect;

    }


    private String stringCondition(ColumnPermission dataPermission){
       return dataPermission.toWhere(valueParser);

    }


}
