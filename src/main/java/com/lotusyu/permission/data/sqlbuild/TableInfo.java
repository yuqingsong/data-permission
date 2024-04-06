package com.lotusyu.permission.data.sqlbuild;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表信息
 *
 * @author maple
 * @version 1.0
 * @since 2020-11-27 00:24
 */
@Data
@AllArgsConstructor
public class TableInfo {
    private String tableName;
    private String alias;
}
