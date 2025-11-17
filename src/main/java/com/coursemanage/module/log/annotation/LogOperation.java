package com.coursemanage.module.log.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    String operator() default "";
    String target() default "";
    String detailOnSuccess() default "";
    String detailOnFailure() default "";
    String module() default "";
    String action() default "";
}
