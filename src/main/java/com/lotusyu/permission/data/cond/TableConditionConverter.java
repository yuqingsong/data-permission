package com.lotusyu.permission.data.cond;

import com.lotusyu.permission.data.config.ColumnPermission;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 将多个条件转换成SQL
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public interface TableConditionConverter {

    /**
     * 将条件转换成SQL
     * @param permissions
     * @return
     */
    String convertToWhere(List<ColumnPermission> permissions);





}
