package com.azxc.unified.common.utils;

import cn.hutool.core.util.StrUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取HttpServlet子对象
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public final class HttpServletUtil {

  /**
   * 获取ServletRequestAttributes对象
   */
  public static ServletRequestAttributes getServletRequest() {
    return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
  }

  /**
   * 获取HttpServletRequest对象
   */
  public static HttpServletRequest getRequest() {
    return getServletRequest().getRequest();
  }

  /**
   * 获取HttpServletResponse对象
   */
  public static HttpServletResponse getResponse() {
    return getServletRequest().getResponse();
  }

  /**
   * 获取请求参数
   */
  public static String getParameter(String param) {
    return getRequest().getParameter(param);
  }

  /**
   * 获取请求参数，带默认值
   */
  public static String getParameter(String param, String defaultValue) {
    String parameter = getRequest().getParameter(param);
    if (StrUtil.isEmpty(parameter)) {
      return defaultValue;
    } else {
      return parameter;
    }
  }

  /**
   * 获取请求参数转换为int类型
   */
  public static Integer getParameterInt(String param) {
    return Integer.valueOf(getRequest().getParameter(param));
  }

  /**
   * 获取请求参数转换为int类型，带默认值
   */
  public static Integer getParameterInt(String param, Integer defaultValue) {
    return Integer.valueOf(getParameter(param, String.valueOf(defaultValue)));
  }

}
