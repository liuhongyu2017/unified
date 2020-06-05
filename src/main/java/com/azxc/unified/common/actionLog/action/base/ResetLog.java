package com.azxc.unified.common.actionLog.action.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.actionLog.annotation.EntityParam;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.entity.ActionLog;
import com.azxc.unified.entity.User;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 自定义日志数据
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
@NoArgsConstructor
@Data
public class ResetLog {

  /**
   * 注解日志方法的返回值
   */
  private Object retValue;

  /**
   * 获取日志实体对象
   */
  private ActionLog actionLog;

  /**
   * aop 连接点
   */
  private JoinPoint joinPoint;

  /**
   * 是否记录日志（默认记录）
   */
  private Boolean record = true;

  /**
   * request对象
   */
  private HttpServletRequest request;

  /**
   * 日志行为
   */
  private Object actionModel;

  /**
   * 产生日志的用户
   */
  private User user;

  /**
   * 判断返回值是否为 ResultVo 对象
   */
  public boolean isResultVo() {
    return retValue instanceof ResultVo;
  }

  /**
   * 判断ResultVo状态码是否为成功
   */
  public boolean isSuccess() {
    return retValue instanceof ResultVo
        && ((ResultVo<?>) retValue).getCode().equals(ResultEnum.SUCCESS.getCode());
  }

  /**
   * 判断ResultVo状态码是否为成功，且设置是否记录日志
   */
  public boolean isSuccessRecord() {
    return retValue instanceof ResultVo
        && ((ResultVo<?>) retValue).getCode().equals(ResultEnum.SUCCESS.getCode());
  }

  /**
   * 获取切入点方法指定名称的参数值
   */
  public Object getParam(String name) {
    Object[] args = joinPoint.getArgs();
    if (args.length > 0) {
      MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
      String[] parameterNames = methodSignature.getParameterNames();
      for (int i = 0; i < parameterNames.length; i++) {
        if (parameterNames[i].equals(name)) {
          return args[i];
        }
      }
    }
    return null;
  }

  /**
   * 获取切入点参数注解@EntityParam的对象
   */
  public Object getEntityParam() {
    Object[] args = joinPoint.getArgs();
    if (args.length > 0) {
      MethodSignature signature = (MethodSignature) joinPoint.getSignature();
      Method method = signature.getMethod();
      Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      for (int i = 0; i < parameterAnnotations.length; i++) {
        for (int j = 0; j < parameterAnnotations[i].length; j++) {
          if (parameterAnnotations[i][j] instanceof EntityParam) {
            return args[i];
          }
        }
      }
    }
    return null;
  }

  private static final Pattern FILL_PATTERN = Pattern.compile("\\$\\{[a-zA-Z0-9]+}");

  /**
   * 内容填充规则
   */
  public String fillRule(Object beanObject, String content) {
    Matcher matcher = FILL_PATTERN.matcher(content);
    while (matcher.find()) {
      String matchWord = matcher.group(0);
      String property = matchWord.substring(2, matchWord.length() - 1);
      String fill = null;
      try {
        fill = String.valueOf(EntityBeanUtil.getField(beanObject, property));
      } catch (ReflectiveOperationException e) {
        e.printStackTrace();
      } finally {
        content = StrUtil.replace(content, matchWord, fill);
      }
    }
    return content;
  }

  /**
   * 获取用户名
   */
  public String getUsername() {
    return user.getUsername();
  }

  /**
   * 获取用户昵称
   */
  public String getNickname() {
    return user.getNickname();
  }

  /**
   * 获取当前时间
   */
  public String getDatetime() {
    return DateUtil.now();
  }

  /**
   * 获取当前时间（自定义时间格式）
   */
  public String getDatetime(String pattern) {
    return DateUtil.format(new Date(), pattern);
  }
}
