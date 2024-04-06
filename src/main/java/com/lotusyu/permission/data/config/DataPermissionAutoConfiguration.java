package com.lotusyu.permission.data.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lotusyu.permission.data.MyBatisUtils;
import com.lotusyu.permission.data.aop.DataPermissionAnnotationAdvisor;
import com.lotusyu.permission.data.cond.TableConditionConverter;
import com.lotusyu.permission.data.cond.TableConditionProvider;
import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.cond.impl.*;
import com.lotusyu.permission.data.config.impl.AllowBlockListTablePermissionProvider;
import com.lotusyu.permission.data.config.impl.GlobalAnnotationTablePermissionProvider;
import com.lotusyu.permission.data.config.impl.MethodTablePermissionProvider;
import com.lotusyu.permission.data.db.DataPermissionDatabaseInterceptor;
import com.lotusyu.permission.data.sqlbuild.DataPermissionSqlBuilder;
import com.lotusyu.permission.data.sqlbuild.impl.DruidSqlBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限的自动配置类
 *
 * @author yqs
 */
@AutoConfiguration
public class DataPermissionAutoConfiguration{


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor()); // 分页插件
        return mybatisPlusInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean(TableConditionProvider.class)
    public TableConditionProvider tableConditionProvider(ValueParser valueParser,TablePermissionProvider tablePermissionProvider,TableConditionConverter converter) {
        return new TableConditionProviderImpl(tablePermissionProvider,converter);
//        return new TableConditionProviderAnnotation(valueParser);
//        return new TableConditionProviderImpl();
    }
    @Bean
    @ConditionalOnMissingBean(TablePermissionProvider.class)
    public TablePermissionProvider tablePermissionProvider(ValueParser valueParser) {
        return new AllowBlockListTablePermissionProvider(new MethodTablePermissionProvider(),new GlobalAnnotationTablePermissionProvider("com."));
//        return new MethodTablePermissionProvider();
//        return new TableConditionProviderImpl();
    }

    @Bean
    @ConditionalOnMissingBean(TableConditionConverter.class)
    public TableConditionConverter tableConditionConverter(ValueParser valueParser) {
        return new TableConditionConverterImpl(valueParser);

    }





    @Bean
    @ConditionalOnMissingBean(ValueParser.class)
    public ValueParser valueParser(SystemPermissionFunctionProvider systemPermissionFunctionProvider,UserDefinePermissionFunctionProvider userDefinePermissionFunctionProvider){
        return new ValueParserImpl(systemPermissionFunctionProvider,userDefinePermissionFunctionProvider,new VarValueParser());
    }

    @Bean
    @ConditionalOnMissingBean(SystemPermissionFunctionProvider.class)
    public SystemPermissionFunctionProvider systemPermissionFunctionProvider(){
        return new SystemPermissionFunctionProvider();
    }
    @Bean
    @ConditionalOnMissingBean(UserDefinePermissionFunctionProvider.class)
    public UserDefinePermissionFunctionProvider userDefinePermissionFunctionProvider(ApplicationContext context){
        return new UserDefinePermissionFunctionProvider(context,"com.");
    }


    @Bean
    @ConditionalOnMissingBean(DataPermissionSqlBuilder.class)
    public DataPermissionSqlBuilder dataPermissionSqlBuilder(TableConditionProvider tableConditionProvider) {
        return new DruidSqlBuilder(tableConditionProvider);
    }

    @Bean
    @ConditionalOnMissingBean(DataPermissionDatabaseInterceptor.class)

    public DataPermissionDatabaseInterceptor dataPermissionDatabaseInterceptor(MybatisPlusInterceptor interceptor,
                                                                               DataPermissionSqlBuilder sqlBuilder) {
        // 创建 DataPermissionDatabaseInterceptor 拦截器
        DataPermissionDatabaseInterceptor inner = new DataPermissionDatabaseInterceptor(sqlBuilder);
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return inner;
    }

    @Bean
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
        return new DataPermissionAnnotationAdvisor();
    }







}
