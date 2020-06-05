package com.azxc.unified.common.actionLog.exception;

import com.azxc.unified.common.actionLog.action.SystemAction;
import com.azxc.unified.common.actionLog.annotation.ActionLog;
import com.azxc.unified.common.exception.advice.ExceptionAdvice;

/**
 * 运行时抛出的异常进行日志记录
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public class ActionLogProceedAdvice implements ExceptionAdvice {

  @ActionLog(key = SystemAction.RUNTIME_EXCEPTION, action = SystemAction.class)
  @Override
  public void run(RuntimeException e) {

  }
}
