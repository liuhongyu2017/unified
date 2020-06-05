package com.azxc.unified.common.actionLog.action.base;

import com.azxc.unified.common.actionLog.annotation.ActionLogAop;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.utils.StatusUtil;
import java.util.List;

/**
 * 通用：记录数据状态
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public class StatusAction extends BaseActionMap {

  @Override
  public void init() {
    // 记录数据状态改变日志
    putMethod(ActionLogAop.DEFAULT_ACTION_NAME, "defaultMethod");
  }

  /**
   * 重新包装保存的数据行为方法
   *
   * @param resetLog ResetLog对象数据
   */
  @SuppressWarnings("unchecked")
  public static void defaultMethod(ResetLog resetLog) {
    if (resetLog.isSuccessRecord()) {
      String param = (String) resetLog.getParam("param");
      StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
      List<Long> ids = (List<Long>) resetLog.getParam("ids");
      resetLog.getActionLog().setMessage(statusEnum.getMessage() + "ID：" + ids.toString());
    }
  }
}
