package com.azxc.unified.common.utils;

import com.azxc.unified.common.constant.StatusConst;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.exception.ResultException;
import java.util.Locale;

/**
 * 数据状态工具
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public final class StatusUtil {

  /**
   * 逻辑删除语句
   */
  public static final String SLICE_DELETE = " set status=" + StatusConst.DELETE + " WHERE id=?";

  /**
   * 不等于逻辑删除条件语句
   */
  public static final String NOT_DELETE = "status != " + StatusConst.DELETE;

  /**
   * 获取状态StatusEnum对象
   *
   * @param param 状态字符参数
   */
  public static StatusEnum getStatusEnum(String param) {
    try {
      return StatusEnum.valueOf(param.toUpperCase(Locale.TRADITIONAL_CHINESE));
    } catch (IllegalArgumentException e) {
      throw new ResultException(ResultEnum.STATUS_ERROR);
    }
  }
}
