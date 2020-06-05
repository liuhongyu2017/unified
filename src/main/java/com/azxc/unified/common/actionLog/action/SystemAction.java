package com.azxc.unified.common.actionLog.action;

import com.azxc.unified.common.actionLog.action.base.BaseActionMap;
import com.azxc.unified.common.actionLog.action.base.ResetLog;
import com.azxc.unified.common.actionLog.action.model.BusinessMethod;
import com.azxc.unified.entity.ActionLog;

/**
 * 系统行为
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public class SystemAction extends BaseActionMap {

  public static final String RUNTIME_EXCEPTION = "runtime_exception";

  @Override
  public void init() {
    // 系统异常行为
    putMethod(RUNTIME_EXCEPTION, new BusinessMethod("系统异常", "runtimeException"));
  }

  /**
   * 系统异常行为方法
   */
  public void runtimeException(ResetLog resetLog) {
    RuntimeException runtime = (RuntimeException) resetLog.getParam("e");
    StringBuilder message = new StringBuilder();
    message.append(runtime.toString());
    StackTraceElement[] stackTrace = runtime.getStackTrace();
    for (StackTraceElement stack : stackTrace) {
      message.append("\n\t").append(stack.toString());
    }
    ActionLog actionLog = resetLog.getActionLog();
    actionLog.setOperName("系统");
    actionLog.setMessage(String.valueOf(message));
  }
}
