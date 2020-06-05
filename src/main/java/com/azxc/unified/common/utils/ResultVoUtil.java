package com.azxc.unified.common.utils;

import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.data.URL;
import com.azxc.unified.common.exception.ResultEnum;

/**
 * 响应数据(结果)最外层对象工具
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public final class ResultVoUtil {

  public static final ResultVo<Object> SAVE_SUCCESS = success("保存成功");

  /**
   * 操作成功
   *
   * @param msg    提示信息
   * @param object 对象
   */
  public static ResultVo<Object> success(String msg, Object object) {
    ResultVo<Object> resultVo = new ResultVo<>();
    resultVo.setMsg(msg);
    resultVo.setCode(ResultEnum.SUCCESS.getCode());
    resultVo.setData(object);
    return resultVo;
  }

  /**
   * 操作成功，返回url地址
   *
   * @param msg 提示信息
   * @param url URL包装对象
   */
  public static ResultVo<Object> success(String msg, URL url) {
    return success(msg, url.getUrl());
  }

  /**
   * 操作成功，使用默认的提示信息
   *
   * @param object 对象
   */
  public static ResultVo<Object> success(Object object) {
    String message = ResultEnum.SUCCESS.getMessage();
    return success(message, object);
  }

  /**
   * 操作成功，返回提示信息，不返回数据
   */
  public static ResultVo<Object> success(String msg) {
    return success(msg, (Object) null);
  }

  /**
   * 操作成功，不返回数据
   */
  public static ResultVo<Object> success() {
    return success(null);
  }

  /**
   * 操作有误
   *
   * @param code 错误码
   * @param msg  提示信息
   */
  public static ResultVo<Object> error(Integer code, String msg) {
    ResultVo<Object> resultVo = new ResultVo<>();
    resultVo.setMsg(msg);
    resultVo.setCode(code);
    return resultVo;
  }

  /**
   * 操作有误，使用默认400错误码
   *
   * @param msg 提示信息
   */
  public static ResultVo<Object> error(String msg) {
    Integer code = ResultEnum.ERROR.getCode();
    return error(code, msg);
  }

  /**
   * 操作有误，只返回默认错误状态码
   */
  public static ResultVo<Object> error() {
    return error(null);
  }

}
