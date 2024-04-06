package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.MyBatisUtils;
import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.annotation.DataPermissions;
import com.lotusyu.permission.data.aop.DataPermissionAnnotations;
import com.lotusyu.permission.data.aop.DataPermissionContextHolder;
import com.lotusyu.permission.data.cond.TableConditionProvider;
import com.lotusyu.permission.data.cond.ValueParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
@Getter
@AllArgsConstructor
@Deprecated
public class TableConditionProviderAnnotation implements TableConditionProvider {


    @Resource
    private ValueParser valueParser;

    @Override
    public String getConds(String tableName) {
        List<DataPermissionAnnotations> all = DataPermissionContextHolder.getAll();
        List<DataPermission> allMatchedDataPermission = new ArrayList<>();
        all.forEach(dataPermissionAnnotations -> {
            dataPermissionAnnotations.getAnnotations().forEach(dataPermission->{
                filteCondsByTableName(tableName,dataPermission,allMatchedDataPermission);
            });
        });
        return convertToSqlConds(allMatchedDataPermission);
    }

    /**
     * 拼装成sql条件
     * @param dataPermissions
     * @return
     */
    private String convertToSqlConds(List<DataPermission> dataPermissions){
        String collect = dataPermissions.stream().map(dataPermission -> "(" + stringCondition(dataPermission) + ")").collect(Collectors.joining(" and"));
        System.out.println("where:"+collect);
        return collect;

    }


    private String stringCondition(DataPermission dataPermission){
        String column = dataPermission.table()+"."+dataPermission.column();
        String operator = dataPermission.operator();
        if(StringUtils.isEmpty(operator)){
            operator = "=";
        }
        String value = dataPermission.value();

        Object parse = valueParser.parse(value);

        String sqlValue = null;

        if(parse instanceof Number){
            if( parse instanceof Float || parse instanceof Double){

                sqlValue = String.valueOf(((Number) parse).doubleValue());
            }else{
                sqlValue =String.valueOf(((Number) parse).longValue());
            }
        }else{
            //FIXME 处理数组等其他类型,
            if("in".equals(operator)){
                sqlValue = (String) parse;
            }else{
                sqlValue = "\'"+(String) parse+"\'";
            }
        }
        return column+" "+ operator + " " + sqlValue;

    }
    /**
     * 转换成jsql 表达式
     * @param dataPermission
     * @return
     */
    private Expression jsqlCondition(DataPermission dataPermission){
        Column column = MyBatisUtils.buildColumn(dataPermission.table(), null, dataPermission.name());

        String value = dataPermission.value();

        Object parse = valueParser.parse(value);
        Expression jsqlValue = null;
        if(parse instanceof Number){
            if( parse instanceof Float || parse instanceof Double){
                DoubleValue doubleValue = new DoubleValue( );
                doubleValue.setValue(((Number) parse).doubleValue());
                jsqlValue = doubleValue;
            }else{
                jsqlValue = new LongValue(((Number) parse).longValue());
            }
        }else{
            jsqlValue = new StringValue((String) parse);
        }

        return new EqualsTo(column, jsqlValue);

    }

    private List<DataPermission> filteCondsByTableName(String tableName,Annotation annotation,List<DataPermission> allMatchedDataPermission){
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
