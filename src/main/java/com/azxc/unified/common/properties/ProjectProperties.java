package com.azxc.unified.common.properties;

import com.azxc.unified.common.utils.ToolUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件中的项目配置
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@Data
@ConfigurationProperties(prefix = "project")
@Component
public class ProjectProperties {

  /**
   * 工作组id
   */
  private Integer workId = 1;

  /**
   * 数据中心id
   */
  private Integer dataCenterId = 1;

  /**
   * cookie记住我登录信息时间，默认7天
   */
  private Integer rememberMeTimeout = 7;

  /**
   * Session会话超时时间，默认30分钟
   */
  private Integer globalSessionTimeout = 1800;

  /**
   * Session会话检测间隔时间，默认15分钟
   */
  private Integer sessionValidationInterval = 900;

  /**
   * 忽略个路径规则，多个规则使用“,”分割
   */
  private String excludes = "";

  /**
   * 文件上传路径
   */
  private String filePath;

  /**
   * 上传文件静态资源访问路径
   */
  private String staticPath = "/file/**";

  /**
   * 获取文件路径
   */
  public String getFilePath() {
    if (filePath == null) {
      return ToolUtil.getProjectPath() + "/upload/";
    }
    return filePath;
  }
}
