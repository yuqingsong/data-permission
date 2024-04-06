package com.lotusyu.permission.data.util;


import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.Scanners;
import org.reflections.util.QueryFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;

public class ReflectionUtils {

    public static Set<Class<?>> scanTableAnnotations(String packageName, Class<? extends Annotation>... annoClazzArray){
        Reflections ref = new Reflections(packageName);

        QueryFunction<Store, String> with = null;
        AnnotatedElement[] annotatedElements = new AnnotatedElement[annoClazzArray.length];
        for (int i = 0; i < annoClazzArray.length; i++) {
            annotatedElements[i]=annoClazzArray[i];
        }
        with = Scanners.TypesAnnotated.with(annotatedElements);
        Set<Class<?>> classes = ref.get(Scanners.SubTypes.of(with).asClass(ref.getConfiguration().getClassLoaders()));
        return classes;
//        Set<Class<?>> typesAnnotatedWith = ref.getTypesAnnotatedWith(annoClazzArray[0]);
//        return typesAnnotatedWith;
    }

}