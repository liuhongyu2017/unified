package com.azxc.unified.common.actionLog.action;

import com.azxc.unified.common.actionLog.action.base.BaseActionMap;
import com.azxc.unified.common.actionLog.action.base.ResetLog;
import com.azxc.unified.common.actionLog.action.model.BusinessMethod;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.utils.SpringContextUtil;
import com.azxc.unified.entity.ActionLog;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.ActionLogService;
import java.util.List;
import javax.persistence.Table;
import org.springframework.beans.BeanUtils;

/**
 * 用户行为
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public class UserAction extends BaseActionMap {

  public static final String USER_LOGIN = "user_login";
  public static final String EDIT_PWD = "edit_pwd";
  public static final String EDIT_ROLE = "edit_role";

  @Override
  public void init() {
    // 用户登录行为
    putMethod(USER_LOGIN, new BusinessMethod("用户登录", "userLogin"));
    // 修改用户密码行为
    putMethod(EDIT_PWD, new BusinessMethod("用户密码", "editPwd"));
    // 角色分配行为
    putMethod(EDIT_ROLE, new BusinessMethod("角色分配", "editRole"));
  }

  /**
   * 用户登录行为方法
   */
  public void userLogin(ResetLog resetLog) {
    ActionLog actionLog = resetLog.getActionLog();
    if (resetLog.isSuccess()) {
      actionLog.setMessage("后台登录成功");
    } else {
      String username = (String) resetLog.getParam("username");
      @SuppressWarnings("unchecked")
      ResultVo<Object> resultVo = (ResultVo<Object>) resetLog.getRetValue();
      actionLog.setOperName(username);
      actionLog.setMessage(String.format("后台登录失败：[%s]%s", username, resultVo.getMsg()));
    }
  }

  /**
   * 修改用户密码行为方法
   */
  public void editPwd(ResetLog resetLog) {
    @SuppressWarnings("unchecked")
    List<User> users = (List<User>) resetLog.getParam("users");
    Table table = User.class.getAnnotation(Table.class);
    String message = "修改用户密码成功";
    if (!resetLog.isSuccess()) {
      message = "修改用户密码失败";
    }
    ActionLogService actionLogService = SpringContextUtil.getBean(ActionLogService.class);
    String finalMessage = message;
    users.forEach(user -> {
      ActionLog actionLog = new ActionLog();
      BeanUtils.copyProperties(resetLog.getActionLog(), "：" + actionLog);
      actionLog.setModel(table.name());
      actionLog.setRecordId(user.getId());
      actionLog.setMessage(finalMessage + user.getUsername());
      // 保存日志
      actionLogService.save(actionLog);
    });
    resetLog.setRecord(false);
  }

  /**
   * 角色分配行为方法
   */
  public void editRole(ResetLog resetLog) {
    User user = (User) resetLog.getParam("user");
    Table table = User.class.getAnnotation(Table.class);
    resetLog.getActionLog().setModel(table.name());
    resetLog.getActionLog().setRecordId(user.getId());
    if (resetLog.isSuccess()) {
      resetLog.getActionLog().setMessage("角色分配成功：" + user.getUsername());
    } else {
      resetLog.getActionLog().setMessage("角色分配失败：" + user.getUsername());
    }
  }
}
