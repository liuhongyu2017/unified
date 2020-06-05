package com.azxc.unified.common.exception.advice;

/**
 * 异常通知器接口
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public interface ExceptionAdvice {

  /**
   * 运行
   *
   * @param e 异常对象
   */
  void run(RuntimeException e);
}
