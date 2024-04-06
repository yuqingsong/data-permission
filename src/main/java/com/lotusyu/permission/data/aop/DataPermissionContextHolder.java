package com.lotusyu.permission.data.aop;

import com.alibaba.ttl.TransmittableThreadLocal;


import java.util.LinkedList;
import java.util.List;

/**
 * {@link DataPermissionAnnotations} 注解的 Context 上下文
 *
 *
 */
public class DataPermissionContextHolder {

    /**
     * 使用 List 的原因，可能存在方法的嵌套调用
     */
    private static final ThreadLocal<LinkedList<DataPermissionAnnotations>> DATA_PERMISSIONS =
            TransmittableThreadLocal.withInitial(LinkedList::new);

    /**
     * 获得当前的 DataPermission 注解
     *
     * @return DataPermission 注解
     */
    public static DataPermissionAnnotations get() {
        return DATA_PERMISSIONS.get().peekLast();
    }

    /**
     * 入栈 DataPermission 注解
     *
     * @param dataPermission DataPermission 注解
     */
    public static void add(DataPermissionAnnotations dataPermission) {
        DATA_PERMISSIONS.get().addLast(dataPermission);
    }

    /**
     * 出栈 DataPermission 注解
     *
     * @return DataPermission 注解
     */
    public static DataPermissionAnnotations remove() {
        DataPermissionAnnotations dataPermission = DATA_PERMISSIONS.get().removeLast();
        // 无元素时，清空 ThreadLocal
        if (DATA_PERMISSIONS.get().isEmpty()) {
            DATA_PERMISSIONS.remove();
        }
        return dataPermission;
    }

    /**
     * 获得所有 DataPermission
     *
     * @return DataPermission 队列
     */
    public static List<DataPermissionAnnotations> getAll() {
        return DATA_PERMISSIONS.get();
    }

    /**
     * 清空上下文
     *
     * 目前仅仅用于单测
     */
    public static void clear() {
        DATA_PERMISSIONS.remove();
    }

}
