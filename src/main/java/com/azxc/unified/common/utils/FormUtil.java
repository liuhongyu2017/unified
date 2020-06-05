package com.azxc.unified.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.azxc.unified.common.exception.ResultExceptionError;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 表单工具类
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
public class FormUtil {

  private FormUtil() {
  }

  /**
   * 表单参数验证器
   *
   * @param valid 验证器
   * @param bean  被验证的 bean
   * @param <T>   bean
   */
  public static <T> void validateParameter(Class<?> valid, T bean) {
    Object validItem = null;
    try {
      validItem = valid.newInstance();
    } catch (IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
    if (validItem != null) {
      BeanUtil.copyProperties(bean, validItem);
      // 获得验证器
      Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
      // 执行验证
      Set<ConstraintViolation<Object>> set = validator.validate(validItem);
      // 取出第一个
      ConstraintViolation<Object> constraintViolation = set.stream().findFirst().orElse(null);
      // 如果第一个存在抛出异常
      if (constraintViolation != null) {
        throw new ResultExceptionError(constraintViolation.getMessage());
      }
    }
  }
}
