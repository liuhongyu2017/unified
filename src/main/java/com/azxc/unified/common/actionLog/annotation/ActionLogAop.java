package com.azxc.unified.common.actionLog.annotation;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.actionLog.action.base.BaseActionMap;
import com.azxc.unified.common.actionLog.action.base.ResetLog;
import com.azxc.unified.common.actionLog.action.model.ActionModel;
import com.azxc.unified.common.actionLog.action.model.BusinessMethod;
import com.azxc.unified.common.actionLog.action.model.BusinessType;
import com.azxc.unified.common.utils.HttpServletUtil;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.common.utils.SpringContextUtil;
import com.azxc.unified.entity.ActionLog;
import com.azxc.unified.service.ActionLogService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 行为日志 aop
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Slf4j
@Aspect
@Component
public class ActionLogAop {

  /**
   * 默认行为方法
   */
  public static final String DEFAULT_ACTION_NAME = "default";

  /**
   * 切面处理类
   *
   * @param point 切入点
   */
  @Around("@annotation(com.azxc.unified.common.actionLog.annotation.ActionLog)")
  public Object recordLog(ProceedingJoinPoint point) throws Throwable {
    // 先执行切入点，获取返回值
    Object proceed = point.proceed();
    // 封装日志实例对象
    ActionLog actionLog = new ActionLog();
    actionLog.setIpAddr(ShiroUtil.getIp());
    actionLog.setClazz(point.getTarget().getClass().getName());
    actionLog.setOperBy(ShiroUtil.getSubject());
    if (ShiroUtil.getSubject() != null) {
      actionLog.setOperName(ShiroUtil.getSubject().getNickname());
    }
    // 封装行为
    ResetLog resetLog = new ResetLog();
    resetLog.setActionLog(actionLog);
    resetLog.setRetValue(proceed);
    resetLog.setJoinPoint(point);
    resetLog.setUser(ShiroUtil.getSubject());
    resetLog.setRequest(HttpServletUtil.getRequest());
    // 异步处理日志
    ActionLogAop actionLogAop = SpringContextUtil.getBean(ActionLogAop.class);
    actionLogAop.action(resetLog);
    return proceed;
  }

  /**
   * 日志处理
   */
  @Async("actionLogExecutor")
  public void action(ResetLog resetLog) throws IllegalAccessException, InstantiationException {
    // 读取ActionLog注解消息
    Method targetMethod = ((MethodSignature) (resetLog.getJoinPoint().getSignature())).getMethod();
    com.azxc.unified.common.actionLog.annotation.ActionLog anno =
        targetMethod.getAnnotation(com.azxc.unified.common.actionLog.annotation.ActionLog.class);
    // 获取name值
    String name = anno.name();
    // 获取message值
    String message = anno.message();
    // 获取key值
    String key = anno.key();
    // 获取行为模型
    Class<? extends BaseActionMap> action = anno.action();
    BaseActionMap instance = action.newInstance();
    Object actionModel = instance.get(StrUtil.isNotEmpty(key) ? key : DEFAULT_ACTION_NAME);
    Assert.notNull(actionModel, "无法获取日志的行为方法，请检查：" + resetLog.getJoinPoint().getSignature());
    // 封装日志实例对象
    ActionLog actionLog = resetLog.getActionLog();
    actionLog.setMethod(targetMethod.getName());
    actionLog.setType(((ActionModel) actionModel).getType());
    actionLog.setName(!name.isEmpty() ? name : ((ActionModel) actionModel).getName());
    actionLog.setMessage(message);
    actionLog.setCreatedDate(new Date());
    // 判断是否为普通实例对象
    if (actionModel instanceof BusinessType) {
      actionLog.setMessage(((BusinessType) actionModel).getMessage());
    } else {
      try {
        Method method =
            action.getDeclaredMethod(((BusinessMethod) actionModel).getMethod(), ResetLog.class);
        method.invoke(instance, resetLog);
      } catch (NoSuchMethodException | InvocationTargetException e) {
        log.error("获取行为对象方法错误！请检查方法名称是否正确！", e);
        e.printStackTrace();
      }
    }
    if (Boolean.TRUE.equals(resetLog.getRecord())) {
      // 保存日志
      ActionLogService actionLogService = SpringContextUtil.getBean(ActionLogService.class);
      actionLogService.save(actionLog);
    }
  }
}
