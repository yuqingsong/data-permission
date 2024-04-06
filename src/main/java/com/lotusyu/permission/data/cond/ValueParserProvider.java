package com.lotusyu.permission.data.cond;

/**
 * 用于提供值解析器
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public interface ValueParserProvider   {

    /**
     * 根据变量名称或者函数名称，返回变量或者函数解析器
     * @param varOrFunction 变量名称或者函数名称
     * @return
     */
    ValueParser getParser(String varOrFunction);


}
