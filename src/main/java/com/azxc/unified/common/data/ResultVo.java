package com.azxc.unified.common.data;

import lombok.Data;

/**
 * 响应数据(结果)最外层对象
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@Data
public class ResultVo<T> {

  /**
   * 状态码
   */
  private Integer code;

  /**
   * 提示信息
   */
  private String msg;

  /**
   * 响应数据
   */
  private T data;

  public Integer getCode() {
    return this.code;
  }

  public String getMsg() {
    return this.msg;
  }

  public T getData() {
    return this.data;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public void setData(T data) {
    this.data = data;
  }

}
