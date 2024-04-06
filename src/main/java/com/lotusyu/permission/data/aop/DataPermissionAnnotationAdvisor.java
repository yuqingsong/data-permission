package com.lotusyu.permission.data.aop;

import com.lotusyu.permission.data.annotation.DataPermission;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * {@link DataPermission 注解的 Advisor 实现类
 *
 *
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DataPermissionAnnotationAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;

    private final Pointcut pointcut;

    public DataPermissionAnnotationAdvisor() {
        this.advice = new DataPermissionAnnotationInterceptor();
        this.pointcut = this.buildPointcut();
    }

    protected Pointcut buildPointcut() {
        Class<DataPermission> classAnnotationType = DataPermission.class;
        return buildPointcut(classAnnotationType);
    }

    private static ComposablePointcut buildPointcut(Class<DataPermission>... classAnnotationType) {
        ComposablePointcut composablePointcut = null;
        for (Class<DataPermission> dataPermissionClass : classAnnotationType) {
            Pointcut classPointcut = new AnnotationMatchingPointcut(dataPermissionClass, true);
            if(composablePointcut == null){
                composablePointcut = new ComposablePointcut(classPointcut);
            }
            Pointcut methodPointcut = new AnnotationMatchingPointcut(null, dataPermissionClass, true);

            composablePointcut.union(methodPointcut);
        }

        return composablePointcut;
    }



    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
