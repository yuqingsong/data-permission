package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.cond.ValueParserContext;
import com.lotusyu.permission.data.cond.ValueParserProvider;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
@AllArgsConstructor
public class ValueParserImpl implements com.lotusyu.permission.data.cond.ValueParser {



    private ValueParserProvider systemFunctionParser;


    private ValueParserProvider userFunctionParser;


    private ValueParserProvider varParser;


    @Override
    public String parse(String value) {
        if(value.startsWith("$")){
            value = value.substring(1);
        }
        //首先从变量中取值
        ValueParser parser = varParser.getParser(value);
        if(parser != null){
            return String.valueOf(parser.parse(value));
        }

        //其次从用户函数取值
        ValueParser userParser = userFunctionParser.getParser(value);
        if(userParser != null){
            return String.valueOf(userParser.parse(value));
        }

        //最后从系统函数取值
        ValueParser systemParser = systemFunctionParser.getParser(value);
        if(systemParser != null){
            return String.valueOf(systemParser.parse(value));
        }
        return value;
    }
}
