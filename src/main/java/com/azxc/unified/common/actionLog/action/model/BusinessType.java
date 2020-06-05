package com.azxc.unified.common.actionLog.action.model;

import lombok.Getter;

/**
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Getter
public class BusinessType extends ActionModel {

  /**
   * 日志消息
   */
  protected String message;

  /**
   * 只构建日志消息，日志名称由日志注解 name 定义
   *
   * @param message 日志消息
   */
  public BusinessType(String message) {
    this.message = message;
  }

  /**
   * 构建日志名称和日志消息
   *
   * @param name    日志名称
   * @param message 日志消息
   */
  public BusinessType(String name, String message) {
    this.message = message;
    this.name = name;
  }
}
