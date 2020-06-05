package com.azxc.unified.config;

import com.azxc.unified.common.utils.DictUtil;
import com.azxc.unified.common.utils.PageUtil;
import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateModelException;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * FreeMarker 配置类
 *
 * @author lhy
 * @version 1.0 2020/3/13
 */
@Configuration
public class FreeMarkerConfig {

  private final org.springframework.web.servlet.view.freemarker.FreeMarkerConfig freeMarkerConfig;

  public FreeMarkerConfig(
      org.springframework.web.servlet.view.freemarker.FreeMarkerConfig freeMarkerConfig) {
    this.freeMarkerConfig = freeMarkerConfig;
  }

  /**
   * 添加自定义变量
   */
  @PostConstruct
  public void setSharedVariable() throws TemplateModelException {
    freeMarkerConfig.getConfiguration().setSharedVariable("dictUtil", new DictUtil());
    freeMarkerConfig.getConfiguration().setSharedVariable("pageUtil", new PageUtil());
    freeMarkerConfig.getConfiguration().setSharedVariable("shiro", new ShiroTags());
  }


}
