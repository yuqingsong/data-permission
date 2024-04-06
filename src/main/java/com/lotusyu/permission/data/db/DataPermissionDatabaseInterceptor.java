package com.lotusyu.permission.data.db;


import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.lotusyu.permission.data.sqlbuild.DataPermissionSqlBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;

/**
 * mybatis-plus拦截器
 *
 */
@RequiredArgsConstructor
public class DataPermissionDatabaseInterceptor  implements InnerInterceptor {

    private final DataPermissionSqlBuilder sqlBuilder;



    @Override // SELECT 场景
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        // 获得 Mapper 对应的数据权限的规则
//        List<DataPermissionRule> rules = ruleFactory.getDataPermissionRule(ms.getId());
//        if (mappedStatementCache.noRewritable(ms, rules)) { // 如果无需重写，则跳过
//            return;
//        }

        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
            // 处理 SQL
        mpBs.sql(sqlBuilder.build(mpBs.sql()));

    }

    @Override // 只处理 UPDATE / DELETE 场景，不处理 INSERT 场景（因为 INSERT 不需要数据权限)
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            // 获得 Mapper 对应的数据权限的规则
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
                // 处理 SQL
            mpBs.sql(sqlBuilder.build(mpBs.sql()));

        }
    }


}
