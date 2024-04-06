package com.lotusyu.permission.data.util;

import com.lotusyu.permission.data.config.impl.GlobalAnnotationTablePermissionProvider;
import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.annotation.DataPermissions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.lotusyu.permission.data.util.ReflectionUtils.scanTableAnnotations;

//@DataPermission(table="abc",column = "name")
//@DataPermission(table="abc",column = "orgId")
@DataPermissions({
        @DataPermission(table = "order",column = "dept_id"),
        @DataPermission(table = "order",column = "area_id")
})
class ReadAnnotationUtilsTest {

    @Test
    void getClazzFromAnnotation() {
//        Set<Class> clazz = ReadAnnotationUtils.getClazzFromAnnotation("*", TableName.class);
        Set<Class<?>> clazz = TableAnnotationScanner.scanTableAnnotations("com.*", DataPermission.class, DataPermissions.class);

        System.out.println(clazz);
    }
    @Test
    void getClazzFromAnnotation2() {
//        Set<Class> clazz = ReadAnnotationUtils.getClazzFromAnnotation("*", TableName.class);
        String pkgName = "com.";
//        pkgName = "com.lotusyu.permission.data.util";
        Set<Class<?>> clazz = scanTableAnnotations(pkgName, DataPermission.class,DataPermissions.class);
        GlobalAnnotationTablePermissionProvider holder = new GlobalAnnotationTablePermissionProvider(pkgName);
        System.out.println(clazz);
        System.out.println(holder.getTablePermissionMap());

    }
}
//@DataPermission(table = "user",column = "dept_id")
//@DataPermission(table = "user",column = "org_id")
//class EmptyClass{
//
//}