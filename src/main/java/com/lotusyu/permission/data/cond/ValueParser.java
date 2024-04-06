package com.lotusyu.permission.data.cond;

/**
 * 用于解析数据权限配置的value
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public interface ValueParser {

    Object parse(String valueExpress);


    default Object parse(String valueExpress,ValueParserContext context){
        return this.parse(valueExpress);
    }
}
