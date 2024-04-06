package com.lotusyu.permission.data.cond;

/**
 * @Auther: yqs
 * @Date: 2023/5/30
 * @Description:
 */
public interface ValueParserContext {

    <T> T get(String var);
}
