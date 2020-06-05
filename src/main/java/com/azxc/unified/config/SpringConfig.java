package com.azxc.unified.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring mvc 配置
 *
 * @author lhy
 * @version 1.0 2020/3/22
 */
@Configuration
public class SpringConfig implements WebMvcConfigurer {

  /**
   * 静态页面
   */
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/noAuth").setViewName("/system/main/noAuth");
  }
}
