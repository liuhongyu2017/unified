package com.azxc.unified.common.convert;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注解到将字典值转换为字典描述
 *
 * @author lhy
 * @version 1.0 2020/3/14
 */
@JsonSerialize(using = JsonDictConvertSerializer.class)
@JacksonAnnotationsInside
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonDictConvert {

  /**
   * 字典类型
   */
  String value() default "";
}
