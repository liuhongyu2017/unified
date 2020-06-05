package com.azxc.unified.common.data.jpaFilter;

import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.common.utils.SpringContextUtil;
import com.azxc.unified.entity.User;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 * jpa 过滤器切面处理类
 *
 * @author lhy
 * @version 1.0 2020/3/26
 */
@Aspect
@Component
public class JpaFilterAop {

  @PersistenceContext
  private EntityManager entityManager;

  @Around("@annotation(com.azxc.unified.common.data.jpaFilter.annotation.JpaFilter)")
  public Object doProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    // 超级管理员不使用任何过滤器
    User user = ShiroUtil.getSubject();
    if (Objects.equals(AdminConst.ADMIN_ID, user.getId())) {
      return joinPoint.proceed();
    }
    // 读取注解消息
    Method targetMethod = ((MethodSignature) (joinPoint.getSignature())).getMethod();
    com.azxc.unified.common.data.jpaFilter.annotation.JpaFilter anno =
        targetMethod
            .getAnnotation(com.azxc.unified.common.data.jpaFilter.annotation.JpaFilter.class);
    JpaFilter jpaFilter = SpringContextUtil.getBean(anno.value());
    try {
      jpaFilter.action(entityManager);
      return joinPoint.proceed();
    } finally {
      entityManager.unwrap(Session.class).disableFilter(anno.getClass().getSimpleName());
    }
  }
}
