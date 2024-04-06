package com.lotusyu.permission.data.config.impl;

import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.annotation.DataPermissions;
import com.lotusyu.permission.data.config.ColumnPermission;
import com.lotusyu.permission.data.config.TablePermissionProvider;
import com.lotusyu.permission.data.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.*;

import static com.lotusyu.permission.data.config.impl.MethodTablePermissionProvider.toColumnPermission;

/**
 * 全局的表配置
 * Description: AnnotationHolder
 * Date: 2023/6/15
 * Author: yqs
 */
@Slf4j
public class GlobalAnnotationTablePermissionProvider implements TablePermissionProvider {

    /**
     * 保存class有哪些注解，key 为class
     */
    private Map<Class<?>, Annotation[]> classAnnoMap = null;

    /**
     * 保存表有哪些权限配置，key为表名
     */
    private Map<String,List<ColumnPermission>> tablePermissionMap = new HashMap<>();


    public GlobalAnnotationTablePermissionProvider(String packageName) {
        scan(packageName);
    }

    /**
     * 全局扫描注解，并存储扫描内容，扫描的范围限定包名
     * @param packageName
     */
    private void scan(String packageName) {
        synchronized (GlobalAnnotationTablePermissionProvider.class) {
            Set<Class<?>> types = null;
            if(classAnnoMap == null){
                types = ReflectionUtils.scanTableAnnotations(packageName, DataPermission.class, DataPermissions.class);
                classAnnoMap = new HashMap<>();
                for (Class<?> aClass : types) {
                    Annotation[] annotations = aClass.getAnnotations();
                    this.putAnnotation(aClass, annotations);
                }
            }else{
                //classAnnoMap 不为空 ，说明已经扫描过了，不需要重复扫描
                return;
            }

        }
    }

    /**
     * 存储注解
     * @param annotationClass
     * @param annotations
     */
    private void putAnnotation(Class<?> annotationClass, Annotation[] annotations) {
        classAnnoMap.put(annotationClass,annotations);
        synchronized (tablePermissionMap){
            for (Annotation annotation : annotations) {
                if(annotation instanceof DataPermission){
                    putDataPermissionToMap(annotation);
                }else if(annotation instanceof DataPermissions){
                    DataPermission[] value = ((DataPermissions) annotation).value();
                    Arrays.stream(value).forEach(this::putDataPermissionToMap);

                }
            }
        }
    }
//    public void putAnnotation(Class<?> annotationClass, Annotation[] annotations) {
//        classAnnoMap.put(annotationClass,annotations);
//        synchronized (tableAnnoMap){
//            for (Annotation annotation : annotations) {
//                if(annotation instanceof DataPermission){
//                    putDataPermissionToMap(annotation);
//                }else if(annotation instanceof DataPermissions){
//                    DataPermission[] value = ((DataPermissions) annotation).value();
//                    Arrays.stream(value).forEach(this::putDataPermissionToMap);
//
//                }
//            }
//        }
//    }

    /**
     * 将注解转换成 ColumnPermission 并存储在tablePermissionMap中
     * @param annotation
     */
    private void putDataPermissionToMap(Annotation annotation) {
        String table = ((DataPermission) annotation).table();
//        System.out.println("table:"+table);
        if(StringUtils.isNotBlank(table)) {
            List<ColumnPermission> annotationList = tablePermissionMap.get(table);
            if (annotationList == null) {
                annotationList = new ArrayList<>();
                tablePermissionMap.put(table, annotationList);
            }

            visitDataPermission(annotation,annotationList);

        }
    }

    public Map<String, List<ColumnPermission>> getTablePermissionMap() {
        return Collections.unmodifiableMap(tablePermissionMap);
    }

    /**
     * 获取表的权限配置
     * @param tableName
     * @return
     */
    public List<ColumnPermission> getAnnotations(String tableName) {
        return tablePermissionMap.get(tableName);
    }


    @Override
    public List<ColumnPermission> getColumnPermissions(String tableName) {
        List<ColumnPermission> annotationList = tablePermissionMap.get(tableName);
        return annotationList;
    }

//    private List<ColumnPermission> toColumnPermissions( List<Annotation> annotationList) {
//        List<ColumnPermission> columnPermissions = new ArrayList<>();
//        annotationList.forEach(annotation -> this.visitDataPermission(annotation, columnPermissions));
//        return columnPermissions;
//    }

    /**
     * 遍历数据权限配置，转换成列权限并添加到result中
     * @param annotation
     * @param result
     */
    public  void  visitDataPermission(Annotation annotation,List result) {
        if(annotation instanceof DataPermission){
            DataPermission dataPermission = (DataPermission) annotation;
            addToList(result, dataPermission);
        }else if(annotation instanceof DataPermissions){
            DataPermission[] dataPermissions = ((DataPermissions) annotation).value();
            for (DataPermission dataPermission : dataPermissions) {
                addToList(result, dataPermission);
            }
        }
    }

    /**
     * 转换成columnPermission，当不为空的时候添加到list
     * @param result
     * @param dataPermission
     */
    private static void addToList(List result, DataPermission dataPermission) {
        ColumnPermission columnPermission = toColumnPermission(dataPermission);
        if(columnPermission != null){
            result.add(columnPermission);
        }else{
            log.warn("dataPermission {} 转换失败",dataPermission);
        }
    }

//    private static DefaultColumnPermission newColumnPermission(DataPermission dataPermission) {
//        return new DefaultColumnPermission(dataPermission.table(), dataPermission.column(), dataPermission.operator(), dataPermission.value(),data);
//    }
}
