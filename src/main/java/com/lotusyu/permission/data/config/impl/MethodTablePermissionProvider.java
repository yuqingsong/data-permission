package com.lotusyu.permission.data.config.impl;

import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.annotation.DataPermissions;
import com.lotusyu.permission.data.aop.DataPermissionAnnotations;
import com.lotusyu.permission.data.aop.DataPermissionContextHolder;
import com.lotusyu.permission.data.config.ColumnPermission;
import com.lotusyu.permission.data.config.TablePermissionProvider;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取方法注解的权限配置
 * Description: MethodTablePermissionProvider
 * Date: 2023/6/14
 * Author: yqs
 */

public class MethodTablePermissionProvider implements TablePermissionProvider {
    @Override
    public List<ColumnPermission> getColumnPermissions(String tableName) {
        List<DataPermissionAnnotations> all = DataPermissionContextHolder.getAll();
        List<DataPermission> allMatchedDataPermission = new ArrayList<>();
        all.forEach(dataPermissionAnnotations -> {
            dataPermissionAnnotations.getAnnotations().forEach(dataPermission->{
                filteCondsByTableName(tableName,dataPermission,allMatchedDataPermission);
            });
        });
        return allMatchedDataPermission.stream().map(MethodTablePermissionProvider::toColumnPermission).collect(Collectors.toList());
    }

    public static ColumnPermission toColumnPermission(DataPermission dataPermission) {
        if(StringUtils.isNotEmpty(dataPermission.where())){
            return new WhereColumnPermission(null,dataPermission.table(),dataPermission.column(),dataPermission.operator(), dataPermission.value(), dataPermission.where());
        }else {
            return new DefaultColumnPermission(null,dataPermission.table(), dataPermission.column(), dataPermission.operator(), dataPermission.value(), dataPermission.where());
        }
    }

    private List<DataPermission> filteCondsByTableName(String tableName, Annotation annotation, List<DataPermission> allMatchedDataPermission){
        if(annotation instanceof DataPermission){
            DataPermission dataPermission = (DataPermission) annotation;
            if( isTableNameMatch(tableName,dataPermission.table())){
                allMatchedDataPermission.add(dataPermission);
            }
        }
        if(annotation instanceof DataPermissions){
            DataPermissions dataPermissions = (DataPermissions) annotation;
            List<DataPermission> result = Arrays.stream(dataPermissions.value()).filter(dataPermission -> isTableNameMatch(tableName, dataPermission.table())).collect(Collectors.toList());
            Arrays.stream(dataPermissions.value()).forEach(dataPermission -> {
                if(isTableNameMatch(tableName, dataPermission.name())){
                    allMatchedDataPermission.add(dataPermission);
                }
            });
        }
        return allMatchedDataPermission;
    }

    public boolean isTableNameMatch(String tableName,String tableNamePattern){
        return tableName.equals(tableNamePattern);
    }
}
