package com.edgar.core.validator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 14-9-11.
 */
@Service
@Aspect
public class ValidatedAspect {
    /**
     * 建立日志的切面方法.
     */
    @Pointcut("execution(* com.edgar..*Service+.*(@com.edgar.core.validator.Validated (*))) && args(obj)")
    public void validatePointCut(Object obj) {
    }

    @Before(value = "validatePointCut(obj)", argNames = "jp, obj")
    public void afterUpdate(JoinPoint jp, Object obj) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        Validate.isAssignableFrom(ValidatorStrategy.class, validated.value());
//        ValidatorStrategy validator = validated.value().newInstance();
        System.out.println("validated");
        System.out.println(obj.getClass());
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Annotation[][] annotations = jp.getTarget().getClass().getMethod(methodName,parameterTypes).getParameterAnnotations();
//        validator.validator(target);
        int i = 0;
        for (Object arg : jp.getArgs()) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType() == Validated.class){
                    ValidatorStrategy validatorStrategy = (ValidatorStrategy) annotation.annotationType().getMethod("value").invoke(annotation).getClass().newInstance();
                    validatorStrategy.validator(arg);
                }
            }
            i++;
        }
    }
}
