package com.azxc.unified.common.exception;

/**
 * 自定义异常对象{统一异常处理：失败}
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public class ResultExceptionError extends ResultException {

  /**
   * 统一异常处理：抛出默认失败信息
   */
  public ResultExceptionError() {
    super(ResultEnum.ERROR);
  }

  /**
   * 统一异常处理：抛出失败提示信息
   *
   * @param message 提示信息
   */
  public ResultExceptionError(String message) {
    super(ResultEnum.ERROR.getCode(), message);
  }
}
