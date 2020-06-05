package com.azxc.unified.common.file.config;

import com.azxc.unified.common.file.FileUpload;
import com.azxc.unified.common.properties.ProjectProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置上传的文件静态资源访问路径
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
@Configuration
public class UploadConfig implements WebMvcConfigurer {

  private final ProjectProperties properties;

  public UploadConfig(ProjectProperties properties) {
    this.properties = properties;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(properties.getStaticPath())
        .addResourceLocations("file:" + FileUpload.getUploadPath());
  }
}
