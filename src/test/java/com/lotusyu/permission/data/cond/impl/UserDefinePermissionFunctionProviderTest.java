package com.lotusyu.permission.data.cond.impl;

import com.lotusyu.permission.data.TestBootApp;
import com.lotusyu.permission.data.cond.ValueParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestBootApp.class)
public class UserDefinePermissionFunctionProviderTest {

    public static final String CURRENT_USER_ID_FUN = "CurrentUserId";
    public static final Long CURRENT_USER_ID = 123L;

    @Resource
    UserDefinePermissionFunctionProvider userDefinePermissionFunctionProvider;
    @Test
    public void testGetParser(){
        ValueParser parser = userDefinePermissionFunctionProvider.getParser(CURRENT_USER_ID_FUN);
        assertNotNull(parser);
        Object parse = parser.parse(null);
        assertEquals(CURRENT_USER_ID, parse);
    }

}