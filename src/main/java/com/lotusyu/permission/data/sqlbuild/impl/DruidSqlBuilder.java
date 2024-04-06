package com.lotusyu.permission.data.sqlbuild.impl;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.lotusyu.permission.data.cond.TableConditionProvider;
import com.lotusyu.permission.data.sqlbuild.DataPermissionSqlBuilder;
import com.lotusyu.permission.data.sqlbuild.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 使用DruidSql解析SQL
 * Description: DruidSqlBuilder
 * Date: 2023/6/7
 * Author: yqs
 */

@Slf4j
@RequiredArgsConstructor
public class DruidSqlBuilder  extends SQLASTVisitorAdapter implements DataPermissionSqlBuilder{


    private final TableConditionProvider tableConditionProvider;


    private Set<SQLExpr> dataPermissionSqlExprs = new HashSet<>();

    @Override
    public String build(String sql) {

        if(log.isInfoEnabled()){
            log.info("original sql:{}",sql);
        }



//        System.out.println("original sql:"+sql);
        String finalSql = buildInner(sql);
//        String result = sqlStatement.toString();
        if(log.isInfoEnabled()) {
            log.info("final sql {}", finalSql);
        }
        return finalSql;
    }

    public String buildInner(String sql) {
        SQLStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement sqlStatement = parser.parseStatement();
        sqlStatement.accept(new DruidSqlBuilder(this.tableConditionProvider));
        String finalSql = null;
        if(sqlStatement instanceof SQLSelectStatement){
            finalSql = ((SQLSelectStatement) sqlStatement).toUnformattedString();
        }else{
            finalSql = sqlStatement.toString();
        }
        return finalSql;
    }

    private final String EMPTY_TABLE_KEY = "";

    /**
     * 查询语句
     * @param queryBlock
     * @return
     */
    @Override
    public boolean visit(SQLSelectQueryBlock queryBlock) {


        //给查询语句添加条件

        if( (queryBlock.getFrom() instanceof SQLJoinTableSource)){
            return true;
        }else{
//            List<TableInfo> tableInfoList = getTableInfoList(queryBlock.getFrom(),
//                    new ArrayList<>());
            addWhereCondition(queryBlock,queryBlock.getWhere());
            return true;
        }
//        if(queryBlock.getWhere() == null ){
//            if( (queryBlock.getFrom() instanceof SQLJoinTableSource)){
////                if(((SQLJoinTableSource) queryBlock.getFrom()).getJoinType() == SQLJoinTableSource.JoinType.COMMA) {
////                    // 当没有where 条件时 select * from table1,table2 这种join方式，也需要添加条件
////                    addWhereCondition(queryBlock, null);
////                    return false;
////                }
//            }else{
//                addWhereCondition(queryBlock,null);
//                return false;
//            }
//        }else{
//            List<TableInfo> tableInfoList = getTableInfoList(queryBlock.getFrom(),
//                    new ArrayList<>());
//            addWhereCondition(queryBlock,queryBlock.getWhere());
//        }
////        System.out.println("\nSQLSelectQueryBlock\n"+queryBlock);
//        return super.visit(queryBlock);
    }

    @Override
    public boolean visit(SQLJoinTableSource joinTableSource){
        //给join添加条件
        if(joinTableSource.getJoinType() != SQLJoinTableSource.JoinType.COMMA) {
            addWhereCondition(joinTableSource, joinTableSource.getCondition());
        }else{
            SQLObject parent = joinTableSource.getParent();
            if(parent instanceof SQLSelectQueryBlock){
                SQLExpr where = ((SQLSelectQueryBlock) parent).getWhere();
                addWhereCondition(parent, where);
                return where != null;
            }
        }
        return super.visit(joinTableSource);
    }

    /**
     * in 子查询 条件，和普通的条件查询不一样
     * @param in
     * @return
     */
//    @Override
//    public boolean visit(SQLInSubQueryExpr in){
//        addWhereCondition(in);
//        return super.visit(in);
//    }

    /**
     * in 列表查询 条件，和普通的条件查询不一样
     * @param in
     * @return
     */
//    @Override
//    public boolean visit(SQLInListExpr in){
//        Object parent = in.getParent();
//        List<TableInfo> tableInfoList = null;
//        if (parent instanceof SQLSelectQueryBlock) {
//            tableInfoList = getTableInfoList(((SQLSelectQueryBlock) parent).getFrom(),
//                    new ArrayList<>());
//            ((SQLSelectQueryBlock) parent).setWhere( buildExpr(tableInfoList,in));
//            return false;
//        }
//        return super.visit(in);
//    }

    /**
     * 条件查询
     * @param cond
     * @return
     */
    @Override
    public boolean visit(SQLBinaryOpExpr cond) {
//        System.out.println("\nSQLBinaryOpExpr:\n"+cond);
//        addWhereCondition(cond);
//        System.out.println(tableInfoList);
        return super.visit(cond);
    }

    /**
     * exists 条件，和普通的条件查询不一样
     * @param x
     * @return
     */
//    public boolean visit(SQLExistsExpr x) {
//        if(isPermissionExpr(x)){
//
//        }else{
//            addWhereCondition(x);
//        }
//        return super.visit(x);
//    }

    /**
     * 添加where 条件
     * @param condExp
     * @return
     */
    private boolean addWhereCondition(SQLExpr condExp) {
        SQLObject parent = condExp.getParent();
        return addWhereCondition(parent,condExp);
    }

    private boolean addWhereCondition(SQLObject parent,SQLExpr condExp) {
        if (isPermissionExpr(condExp)) return false;
        List<TableInfo> tableInfoList = null;
        //  select where块
        if (parent instanceof SQLSelectQueryBlock) {
            tableInfoList = getTableInfoList(((SQLSelectQueryBlock) parent).getFrom(),
                    new ArrayList<>());
            ((SQLSelectQueryBlock) parent).setWhere( buildExpr(tableInfoList, condExp));
            return true;
        }

        if (parent instanceof SQLJoinTableSource) {
            // 拼接要加入的条件
            tableInfoList = getTableInfoList(((SQLJoinTableSource) parent),
                    new ArrayList<>());

            ((SQLJoinTableSource) parent).setCondition( buildExpr(tableInfoList, condExp));
            return true;
        }

        // update where块
        if (parent instanceof SQLUpdateStatement) {
            tableInfoList = getTableInfoList(((SQLUpdateStatement) parent).getTableSource(),
                    new ArrayList<>());
            ((SQLUpdateStatement) parent).setWhere( buildExpr(tableInfoList, condExp));
            return true;
        }

        // delete where块
        if (parent instanceof SQLDeleteStatement) {
            tableInfoList = getTableInfoList(((SQLDeleteStatement) parent).getTableSource(),
                    new ArrayList<>());
            ((SQLDeleteStatement) parent).setWhere( buildExpr(tableInfoList, condExp));
            return true;
        }
        return false;
    }

    private boolean isPermissionExpr(SQLExpr condExp) {
        return dataPermissionSqlExprs.contains(condExp);
    }

    @Override
    public boolean visit(SQLDeleteStatement x) {
        return super.visit(x);
    }

    /**
     * 添加条件
     * @param tableInfoList
     * @param x
     * @return
     */
    private SQLExpr buildExpr(List<TableInfo> tableInfoList, SQLExpr x) {
        SQLExpr allOpExpr = x;
        for (TableInfo item : tableInfoList) {
            String aliasName = item.getAlias();
            String conds = tableConditionProvider.getConds(item.getTableName(),aliasName);
//            if(StringUtils.isEmpty(conds)){
//                conds = getConds(item.getTableName());
//            }
            SQLExpr dataPermissionCondExpr = null;
            if(StringUtils.isNotEmpty(conds)){
                dataPermissionCondExpr = createExpr(conds);
//                if(dataPermissionCondExpr instanceof  SQLBinaryOpExpr){
//                    SQLExpr left = ((SQLBinaryOpExpr) dataPermissionCondExpr).getLeft();
//                    if(left)
//                }
//                if(dataPermissionCondExpr.)
            }


            if(dataPermissionCondExpr != null){
                dataPermissionSqlExprs.add(dataPermissionCondExpr);
                if(allOpExpr == null){
                    allOpExpr = dataPermissionCondExpr;
                }else{
                    allOpExpr = SQLBinaryOpExpr.and(allOpExpr, dataPermissionCondExpr);
                }
            }
        }
        return allOpExpr;
    }

    private  SQLExpr createExpr(String conds) {
        SQLExpr dataPermissionCondExpr;
        SQLExprParser exprParser = new SQLExprParser(conds);
        SQLExpr expr = exprParser.expr();
        this.dataPermissionSqlExprs.add(expr);
        return expr;

    }


    private String blankToDefault(String str, String defaultStr) {
        return StringUtils.isEmpty(str) ? defaultStr : str.toString();
    }


    /**
     * 遍历并获得当前层级下的表别名
     *
     * @param tableSource   表信息 由form块获取
     * @param tableInfoList 用于迭代的表信息集合
     * @return 表信息集合
     */
    protected List<TableInfo> getTableInfoList(SQLTableSource tableSource, List<TableInfo> tableInfoList) {
        return getTableInfoList(tableSource, tableInfoList, Boolean.TRUE);
    }

    /**
     * 遍历并获得当前层级下的表别名
     *
     * @param tableSource   表信息 由form块获取
     * @param tableInfoList 用于迭代的表信息集合
     * @param isGetRight    关联查询是是否获取右表信息
     * @return 表信息集合
     */
    private List<TableInfo> getTableInfoList(SQLTableSource tableSource, List<TableInfo> tableInfoList,
                                             Boolean isGetRight) {
        if (tableSource instanceof SQLSubqueryTableSource) {
            tableInfoList.add(new TableInfo(EMPTY_TABLE_KEY, tableSource.getAlias()));
        }

        if (tableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource joinSource = (SQLJoinTableSource) tableSource;
            getTableInfoList(joinSource.getLeft(), tableInfoList, isGetRight);
            // 这里如果是join语句在where条件中是不需要加入右表的 因为关联查询关联表不应该影响数据条数 应该只影响关联结果
            if (isGetRight) {
                getTableInfoList(joinSource.getRight(), tableInfoList, true);
            }
        }

        if (tableSource instanceof SQLExprTableSource) {
            tableInfoList.add(new TableInfo(((SQLExprTableSource) tableSource).getTableName(), tableSource.getAlias()));
        }
        return tableInfoList;
    }


}
