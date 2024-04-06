package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.annotation.DataPermissionFunction;
import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.cond.ValueParserProvider;
import com.lotusyu.permission.data.util.ReflectionUtils;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 提供用户自定义函数，会扫描指定包下的DataPermissionFunction注解，将注解所在类存入到用户自定义函数中
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public class UserDefinePermissionFunctionProvider implements ValueParserProvider {

    private Map<String,ValueParser> userDefineFunctionMap = new HashMap<String, ValueParser>();

    public UserDefinePermissionFunctionProvider(ApplicationContext context,String packageName) {
        Set<Class<?>> classes = ReflectionUtils.scanTableAnnotations(packageName,DataPermissionFunction.class);
        classes.stream().forEach(aClass -> parseUserDefineFunction(aClass,context));
    }

    private void parseUserDefineFunction(Class<?> aClass,ApplicationContext context) {
        Function<String,Object> bean = (Function<String,Object>) context.getBean(aClass);
        DataPermissionFunction annotation = aClass.getAnnotation(DataPermissionFunction.class);
        userDefineFunctionMap.put(annotation.value(), key->bean.apply(key));
    }


    @Override
    public ValueParser getParser(String  key) {
        return userDefineFunctionMap.get(key);
    }


}
