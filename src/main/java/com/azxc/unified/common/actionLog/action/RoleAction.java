package com.azxc.unified.common.actionLog.action;

import com.azxc.unified.common.actionLog.action.base.BaseActionMap;
import com.azxc.unified.common.actionLog.action.base.ResetLog;
import com.azxc.unified.common.actionLog.action.model.BusinessMethod;
import com.azxc.unified.entity.Role;
import javax.persistence.Table;

/**
 * 角色行为
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public class RoleAction extends BaseActionMap {

  public static final String ROLE_AUTH = "role_auth";

  @Override
  public void init() {
    // 角色授权行为
    putMethod(ROLE_AUTH, new BusinessMethod("角色授权", "roleAuth"));
  }

  /**
   * 角色授权行为方法
   */
  public void roleAuth(ResetLog resetLog) {
    Role role = (Role) resetLog.getParam("role");
    Table table = Role.class.getAnnotation(Table.class);
    resetLog.getActionLog().setModel(table.name());
    resetLog.getActionLog().setRecordId(role.getId());
    if (resetLog.isSuccess()) {
      resetLog.getActionLog().setMessage("角色授权成功：" + role.getTitle());
    } else {
      resetLog.getActionLog().setMessage("角色授权失败：" + role.getTitle());
    }
  }
}
