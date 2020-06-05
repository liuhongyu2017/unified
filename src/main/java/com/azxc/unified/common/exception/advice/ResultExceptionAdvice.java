package com.azxc.unified.common.exception.advice;

import com.azxc.unified.common.utils.SpringContextUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 异常通知器
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@ControllerAdvice
public class ResultExceptionAdvice {

  /**
   * 运行切入程序集合
   */
  private List<ExceptionAdvice> proceed = new ArrayList<>();

  /**
   * 添加切入程序
   */
  public void putProceed(ExceptionAdvice advice) {
    proceed.add(advice);
  }

  /**
   * 执行异常通知
   */
  public void runtimeException(RuntimeException e) {
    for (ExceptionAdvice ea : proceed) {
      ExceptionAdvice advice = SpringContextUtil.getBean(ea.getClass());
      advice.run(e);
    }
  }
}
