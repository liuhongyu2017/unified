package com.azxc.unified.common.actionLog.action;

import com.azxc.unified.common.actionLog.action.base.BaseActionMap;
import com.azxc.unified.common.actionLog.action.base.ResetLog;
import com.azxc.unified.common.actionLog.action.model.BusinessMethod;
import com.azxc.unified.common.actionLog.annotation.ActionLogAop;

/**
 * 默认行为 - 只记录 message
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public class MessageAction extends BaseActionMap {

  @Override
  public void init() {
    putMethod(ActionLogAop.DEFAULT_ACTION_NAME, new BusinessMethod("defaultMethod"));
  }

  public void defaultMethod(ResetLog resetLog) {

  }
}
