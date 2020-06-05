package com.azxc.unified.common.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lhy
 * @version 1.0 2020/3/31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Excel {

  /**
   * 字段名或文件名
   */
  String value();

  /**
   * 字段字典标识，导出时进行字典转换
   */
  String dict() default "";

  /**
   * 关联操作实体对象字段名称，用于获取关联数据
   */
  String joinField() default "";
}
