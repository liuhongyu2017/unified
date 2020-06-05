package com.azxc.unified.common.data.jpaFilter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * jpa 过滤器
 *
 * @author lhy
 * @version 1.0 2020/3/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface JpaFilter {

  /**
   * 过滤器
   */
  Class<? extends com.azxc.unified.common.data.jpaFilter.JpaFilter> value();
}
