package com.lotusyu.permission.data.config;

import java.util.List;

/**
 * 提供数据权限配置
 * Description: TablePermissionProvider
 * Date: 2023/6/14
 * Author: yqs
 */

public interface TablePermissionProvider {
    List<ColumnPermission> getColumnPermissions(String tableName);

    default List<ColumnPermission> getColumnPermissions(String tableName,String tableAliasName){
        return getColumnPermissions(tableName);
    }
}
