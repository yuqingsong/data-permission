//package com.lotusyu.permission.data.cond.impl;
//
//import com.lotusyu.permission.data.cond.TableCondition;
//import com.lotusyu.permission.data.cond.ValueParser;
//
///**
// * @Auther: yqs
// * @Date: 2023/5/30
// * @Description:
// */
//public class VarOrConstTableCondition implements TableCondition {
//    String table;
//
//    String column;
//
//    String operator;
//
//    String value;
//
//    ValueParser valueParser;
//
//
//    @Override
//    public String table() {
//        return this.table;
//    }
//
//    @Override
//    public String condition() {
//        return column +" "+this.operator+" "+valueParser.parse(this.value);
//    }
//}
