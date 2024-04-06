package com.lotusyu.permission.data.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@Component
public class TableAnnotationScanner {



    public static Set<Class<?>> scanTableAnnotations(String packageName, Class<? extends Annotation>... annoClazzArray) {
        Set<Class<?>> tableClasses = new HashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        for (Class<? extends Annotation> aClass : annoClazzArray) {
            scanner.addIncludeFilter(new AnnotationTypeFilter(aClass));
        }


        Set<BeanDefinition> candidates = scanner.findCandidateComponents(packageName);
        for (BeanDefinition beanDefinition : candidates) {
            try {
//                ((ScannedGenericBeanDefinition)beanDefinition).getMetadata().getAnnotations()
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                tableClasses.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return tableClasses;
    }
}
