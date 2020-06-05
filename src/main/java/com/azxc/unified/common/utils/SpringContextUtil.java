package com.azxc.unified.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring的ApplicationContext对象工具
 *
 * @author lhy
 * @version 1.0 2020/3/13
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtil.applicationContext = applicationContext;
  }

  /**
   * 获取ApplicationContext对象
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 通过name获取Bean
   */
  public static Object getBean(String name) {
    return applicationContext.getBean(name);
  }

  /**
   * 通过class获取Bean
   */
  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }
}
