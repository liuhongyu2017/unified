package com.azxc.unified.common.actionLog.action.model;

import lombok.Getter;

/**
 * 行为模型
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Getter
public class ActionModel {

  /**
   * 日志名称
   */
  protected String name;

  /**
   * 日志类型
   */
  protected Byte type;
}
