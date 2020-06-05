package com.azxc.unified.common.exception.interfaces;

/**
 * 结果枚举接口
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public interface ResultInterface {

  /**
   * 获取状态编码
   *
   * @return 编码
   */
  Integer getCode();

  /**
   * 获取提示信息
   *
   * @return 提示信息
   */
  String getMessage();

}
