package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.cond.TableConditionConverter;
import com.lotusyu.permission.data.cond.TableConditionProvider;
import com.lotusyu.permission.data.config.ColumnPermission;
import com.lotusyu.permission.data.config.TablePermissionProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
@Getter
@AllArgsConstructor
public class TableConditionProviderImpl implements TableConditionProvider {



    @Resource
    private TablePermissionProvider tablePermissionProvider;

    @Resource
    private TableConditionConverter tableConditionConverter;

    @Override
    public String getConds(String tableName) {
        List<ColumnPermission> columnPermissions = tablePermissionProvider.getColumnPermissions(tableName);
        if(CollectionUtils.isEmpty(columnPermissions)){
            return null;
        }
        return tableConditionConverter.convertToWhere(columnPermissions);
    }








}
