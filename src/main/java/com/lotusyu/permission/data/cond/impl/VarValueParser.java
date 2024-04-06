package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.cond.ValueParser;
import com.lotusyu.permission.data.cond.ValueParserProvider;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public class VarValueParser  implements ValueParserProvider ,ValueParser{
    private Map<String,Object> vars = new HashMap<>();
    @Override
    public ValueParser getParser(String name) {
        if(vars.get(name)!=null){
            return this;
        }
        return null;
    }

    @Override
    public Object parse(String valueExpress) {
        return vars.get(valueExpress);
    }
}
