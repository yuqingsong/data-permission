//package com.lotusyu.permission.data.cond.impl;
//
///**
// * @Auther: yqs
// * @Date: 2023/5/30
// * @Description:
// */
//public class FunctionTableCondition extends VarOrConstTableCondition{
//    String table;
//
//    String column;
//
//    String function;
//
//    ValueParserImpl valueParser;
//
//
//    @Override
//    public String table() {
//        return this.table;
//    }
//
//    @Override
//    public String condition() {
//        return column +" in "+valueParser.parse(this.function);
//    }
//}
