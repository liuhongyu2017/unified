package com.azxc.unified.common.actionLog.action.model;

import lombok.Getter;

/**
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Getter
public class BusinessMethod extends ActionModel {

  /**
   * 行为方法名
   */
  protected String method;

  /**
   * 只构建行为方法名，日志名称由日志注解 name 定义
   *
   * @param method 行为方法名
   */
  public BusinessMethod(String method) {
    this.method = method;
  }

  /**
   * 构建日志名称和行为方法名
   *
   * @param name   日志名称
   * @param method 行为方法名
   */
  public BusinessMethod(String name, String method) {
    this.method = method;
    this.name = name;
  }
}
