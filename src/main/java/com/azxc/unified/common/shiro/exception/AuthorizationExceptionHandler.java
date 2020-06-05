package com.azxc.unified.common.shiro.exception;

import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.SpringContextUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 拦截访问权限异常处理
 *
 * @author lhy
 * @version 1.0 2020/3/20
 */
@ControllerAdvice
@Order(-1)
public class AuthorizationExceptionHandler {

  /**
   * 拦截访问权限异常
   */
  @ResponseBody
  @ExceptionHandler(AuthorizationException.class)
  public ResultVo<Object> authorizationException(AuthorizationException e,
      HttpServletRequest request, HttpServletResponse response) {
    Integer code = ResultEnum.NO_PERMISSIONS.getCode();
    String msg = ResultEnum.NO_PERMISSIONS.getMessage();
    // 获取异常信息
    Throwable cause = e.getCause();
    String message = cause.getMessage();
    @SuppressWarnings("rawtypes")
    Class<ResultVo> resultVoClass = ResultVo.class;
    // 判断无权限访问的方法返回对象是否为ResultVo
    if (!message.contains(resultVoClass.getName())) {
      try {
        // 重定向到无权限页面
        String contextPath = request.getContextPath();
        ShiroFilterFactoryBean shiroFilter =
            SpringContextUtil.getBean(ShiroFilterFactoryBean.class);
        response.sendRedirect(contextPath + shiroFilter.getUnauthorizedUrl());
      } catch (IOException e1) {
        return ResultVoUtil.error(code, msg);
      }
    }
    return ResultVoUtil.error(code, msg);
  }
}
