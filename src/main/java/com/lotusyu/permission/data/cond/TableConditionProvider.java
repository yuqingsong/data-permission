package com.lotusyu.permission.data.cond;

import org.apache.commons.lang3.StringUtils;

/**
 * 根据表名获取SQL条件
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public interface TableConditionProvider {

    String getConds(String tableName);

    default String getConds(String tableName,String tableAliasName){
        String conds = null;
        if(tableAliasName != null){
            conds = getConds(tableAliasName);
        }
        if(StringUtils.isEmpty(conds)){
                conds = getConds(tableName);
            }
        return conds;
    }





}
