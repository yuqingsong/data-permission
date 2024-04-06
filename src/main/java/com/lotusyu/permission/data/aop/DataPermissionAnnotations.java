package com.lotusyu.permission.data.aop;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yqs
 * @Date: 2023/6/2
 * @Description:
 */
@Getter
public class DataPermissionAnnotations {
    List<Annotation> annotations = new ArrayList<>(5);

    public static DataPermissionAnnotations valueOf(Annotation... annotationArray){
        DataPermissionAnnotations instance = new DataPermissionAnnotations();
        for (Annotation annotation : annotationArray) {
            instance.annotations.add(annotation);
        }
        return instance;
    }

}
