package com.lotusyu.permission.data.config.impl;

import com.lotusyu.permission.data.annotation.AllowBlockList;
import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.aop.DataPermissionAnnotations;
import com.lotusyu.permission.data.aop.DataPermissionContextHolder;
import com.lotusyu.permission.data.config.ColumnPermission;
import com.lotusyu.permission.data.config.TablePermissionProvider;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 获取方法注解的权限黑白名单配置
 * Description: AllowBlockListTablePermissionProvider
 * Date: 2023/6/14
 * Author: yqs
 */

@AllArgsConstructor
public class AllowBlockListTablePermissionProvider implements TablePermissionProvider {

    private MethodTablePermissionProvider methodTablePermissionProvider;

    private GlobalAnnotationTablePermissionProvider globalAnnotationTablePermissionProvider;

    @Override
    public List<ColumnPermission> getColumnPermissions(String tableName) {
        AllowBlockListInfo allAllowBlockList = getAllowBlockList();
        //黑名单中包含当前表，直接返回
        if(allAllowBlockList.blockContains(tableName)){
            return Collections.emptyList();
        }

        List<ColumnPermission> columnPermissions = methodTablePermissionProvider.getColumnPermissions(tableName);
        //方法中包含权限配置，直接返回
        if(CollectionUtils.isNotEmpty(columnPermissions)){
            return columnPermissions;
        }
        //返回全局的权限配置
        return globalAnnotationTablePermissionProvider.getColumnPermissions(tableName);

    }

    private AllowBlockListInfo getAllowBlockList() {
        List<DataPermissionAnnotations> all = DataPermissionContextHolder.getAll();
        AllowBlockListInfo allAllowBlockList = new AllowBlockListInfo();
        all.forEach(dataPermissionAnnotations -> {
            dataPermissionAnnotations.getAnnotations().forEach(dataPermission->{
                filteCondsByTableName(dataPermission,allAllowBlockList);
            });
        });
        return allAllowBlockList;
    }


    private void filteCondsByTableName(Annotation annotation, AllowBlockListInfo allAllowBlockList){

        if(annotation instanceof AllowBlockList){
            AllowBlockList allowBlockListAnnotation = (AllowBlockList)annotation;
            //白名单现在没有用上，忽略
//            String[] tables = allowBlockListAnnotation.allowPermission();
            String[] tables = allowBlockListAnnotation.blockPermission();
            for (String table : tables) {
                allAllowBlockList.addBlock(table);
            }
        }

    }



    private static class AllowBlockListInfo{
        private Set<String> allowSet = new HashSet<>();

        private Set<String> blockSet = new HashSet<>();

        void addAllow(String table){
            this.allowSet.add(table);
        }

        void addBlock(String table){
            this.blockSet.add(table);
        }

        boolean blockContains(String table) {
            return this.blockSet.contains(table);
        }
    }
}
