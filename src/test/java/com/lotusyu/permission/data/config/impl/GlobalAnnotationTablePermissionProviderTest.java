package com.lotusyu.permission.data.config.impl;

import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.annotation.DataPermissionFunction;
import com.lotusyu.permission.data.annotation.DataPermissions;
import com.lotusyu.permission.data.config.ColumnPermission;
import com.lotusyu.permission.data.config.TablePermissionProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataPermissions({
        @DataPermission(table = "order",column = "dept_id",operator = ">" ,value = "1"),
        @DataPermission(table = "order",column = "area_id",operator = "<", value = "1000")
})
class GlobalAnnotationTablePermissionProviderTest {

    @Test
    void getColumnPermissions() {
        String pkgName = "com.lotusyu.permission.data.config.";
//        pkgName = "com.lotusyu.permission.data.util";

        TablePermissionProvider provider = new GlobalAnnotationTablePermissionProvider(pkgName);


        List<ColumnPermission> orderPermissions = provider.getColumnPermissions("order");
        List<ColumnPermission> userPermissions = provider.getColumnPermissions("resource");
        assertEquals(2,orderPermissions.size());
        assertTrue(orderPermissions.contains(new DefaultColumnPermission("order","dept_id",">","1")));
        assertTrue(orderPermissions.contains(new DefaultColumnPermission("order","area_id","<","1000")));
        assertEquals(2,userPermissions.size());
        assertTrue(userPermissions.contains(new DefaultColumnPermission("resource","org_id","","")));
        assertTrue(userPermissions.contains(new DefaultColumnPermission("resource","dept_id","","")));
        System.out.println(orderPermissions);
        System.out.println(userPermissions);

        List<ColumnPermission> appPermissions = provider.getColumnPermissions("app");
        System.out.println(appPermissions);


    }
}

@DataPermission(table = "resource",column = "dept_id")
@DataPermission(table = "resource",column = "org_id")
@DataPermission(table = "app",column = "org_id", value = "$CurrentOrg")
class EmptyClass{

}



