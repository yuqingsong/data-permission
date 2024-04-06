//package com.lotusyu.permission.data.cond.impl;
//
//import com.lotusyu.permission.data.cond.ValueParserContext;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Auther: yqs
// * @Date: 2023/5/30
// * @Description:
// */
//public class ValueParserContextImpl implements ValueParserContext {
//
//    private Map<String,Object> varMap = new HashMap<>();
//    @Override
//    public <T> T get(String var) {
//        return (T) varMap.get(var);
//    }
//
//    public void put(String key,Object value){
//        this.varMap.put(key,value);
//    }
//}
