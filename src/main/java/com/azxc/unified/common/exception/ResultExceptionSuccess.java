package com.azxc.unified.common.exception;

import lombok.Getter;

/**
 * 自定义异常对象{统一异常处理：成功}
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@Getter
public class ResultExceptionSuccess extends ResultException {

  /**
   * 统一异常处理：抛出默认成功信息
   */
  public ResultExceptionSuccess() {
    super(ResultEnum.SUCCESS);
  }

  /**
   * 统一异常处理：抛出成功提示信息
   *
   * @param message 提示信息
   */
  public ResultExceptionSuccess(String message) {
    super(ResultEnum.SUCCESS.getCode(), message);
  }
}
