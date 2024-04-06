package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.cond.ValueParserProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供系统内置函数
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public class SystemPermissionFunctionProvider implements ValueParserProvider {

    private Map<String,ValueParser> userDefineFunctionMap = new HashMap<String, ValueParser>();


    @Override
    public ValueParser getParser(String  key) {
        return userDefineFunctionMap.get(key);
    }

    public void put(String key,ValueParser value){
        this.userDefineFunctionMap.put(key,value);
    }


}
