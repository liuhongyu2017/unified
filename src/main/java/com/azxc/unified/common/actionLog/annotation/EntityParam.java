package com.azxc.unified.common.actionLog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制实体参数注解
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface EntityParam {

}
