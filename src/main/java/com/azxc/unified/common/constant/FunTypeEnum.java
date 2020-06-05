package com.azxc.unified.common.constant;

import lombok.Getter;

/**
 * 功能枚举常量
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
@Getter
public enum FunTypeEnum {

  /**
   * 目录类型
   */
  DIRECTORY((byte) 1, "后台目录"),

  /**
   * 菜单类型
   */
  MENU((byte) 2, "后台菜单"),

  /**
   * 按钮类型
   */
  BUTTON((byte) 3, "后台按钮");

  private Byte code;

  private String message;

  FunTypeEnum(Byte code, String message) {
    this.code = code;
    this.message = message;
  }
}
