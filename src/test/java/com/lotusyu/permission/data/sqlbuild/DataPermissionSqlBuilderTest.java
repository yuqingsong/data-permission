package com.lotusyu.permission.data.sqlbuild;

import com.lotusyu.permission.data.cond.TableConditionProvider;
import com.lotusyu.permission.data.sqlbuild.impl.JsqlSqlBuilder;
import com.lotusyu.permission.data.sqlbuild.impl.DruidSqlBuilder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.ibatis.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
@Slf4j
public class DataPermissionSqlBuilderTest {



    private static Stream<String[]> sqls() {
        return Stream.of(
                new String[]{"select * from user", "select * from user where "+tableCondition("user")}
                ,new String[]{"select * from user where status = 1", "select * from user where status = 1"+" and "+tableCondition("user")}
                ,new String[]{"select * from user a join role b on a.role_id = b.id", "select * from user a join role b on a.role_id = b.id and "+tableCondition("user")+" and "+tableCondition("role")}
                ,new String[]{"select * from user a inner join role b on a.role_id = b.id", "select * from user a inner join role b on a.role_id = b.id and "+tableCondition("user")+" and "+tableCondition("role")}
                ,new String[]{"select * from user a where org_id in (111,222,333) ", "SELECT * FROM user a WHERE org_id IN (111,222,333) and "+tableCondition("user")}
                ,new String[]{"select * from user a where org_id in (select id from org where status = 1) ", "SELECT * FROM user a WHERE org_id IN ( SELECT id FROM org WHERE status = 1 ) and "+tableCondition("user")}
                ,new String[]{"select * from flower  ", "select * from flower where  "+tableCondition("flower")}
                ,new String[]{"select * from test a join flower b on a.flowerid = b.id ", "select * from test a join flower b on a.flowerid = b.id and  "+tableCondition("flower")}
                ,new String[]{"select * from test a , flower b where  a.flowerid = b.id ", "select * from test a , flower b where  a.flowerid = b.id and  "+tableCondition("flower")}
                ,new String[]{"select * from test a , flower b ", "select * from test a , flower b  where  "+tableCondition("flower")}
                ,new String[]{"select * from test a join flower b ", "select * from test a join flower b  on  "+tableCondition("flower")}

        );
    }


    private static String tableCondition(String table){
        switch (table){
            case "user":return table+".org_id = 1";
            case "role":return table+".status = 0";
            case "flower":return table+".name in ('def','abc')";
//            case "flower":return table+".name = 'abc'";
        }
        return null;
    }

    public String formatSql(String sql){
        try {
            return CCJSqlParserUtil.parse(sql).toString().toLowerCase();
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return sql;
    }

    public void assertSql(String expected,String actual){
        assertEquals(formatSql(expected),formatSql(actual));
    }


//    @ParameterizedTest
    @MethodSource("sqls")
    public void testJsql(String originalSql,String finalSql){
        DataPermissionSqlBuilder builder = new JsqlSqlBuilder(buildTableCondProvider4test());
        String sql = builder.build(originalSql);
        assertSql(finalSql,sql);
    }
    @ParameterizedTest
    @MethodSource("sqls")
    public void testDruid(String originalSql,String finalSql){
        DataPermissionSqlBuilder builder = new DruidSqlBuilder(buildTableCondProvider4test());
        String sql = builder.build(originalSql);
        assertSql(finalSql,sql);
    }

    @Test
    public void test() throws JSQLParserException {
        DataPermissionSqlBuilder builder = new JsqlSqlBuilder(buildTableCondProvider4test());


        String sql = "select * from test a join user b on a.user_id = b.id where org_id = 123 and created_by=1";
//        sql = "select * from user where org_id = 123 or created_by=1";
//        sql = "select * from user";
        String finalSql = builder.build(sql);
//        Expression expression = CCJSqlParserUtil.parseCondExpression("org_id = 123 and user_id = 456");
//        System.out.println("where:"+expression);
        System.out.println("final sql:"+finalSql);
    }

    private TableConditionProvider buildTableCondProvider4test() {
        return new TableConditionProvider() {
            @Override
            public String getConds(String tableName) {
                return tableCondition(tableName);
//                return null;
            }
        };
    }


    @ParameterizedTest
    @MethodSource("sqls")
    public void testPerfromance(String originalSql,String finalSql){
//        LogFactory.useLog4J2Logging();

        LogFactory.useNoLogging();
//        Config.setLevel("org.apache.ibatis", org.apache.logging.log4j.Level.INFO);
        DruidSqlBuilder builder = new DruidSqlBuilder(buildTableCondProvider4test());
//        builder = new DataPermissionSqlBuilderImpl2(buildTableCondProvider4test());
        long start = System.currentTimeMillis();
        int count = 1000 * 100;
        for (int i = 0; i < count; i++) {
            String sql = builder.buildInner(originalSql);
        }
        long end = System.currentTimeMillis();
        long qps = count / (end - start) * 1000;
        System.out.println("druid qps:"+ qps);
        assertTrue(qps>100000);

    }


}
