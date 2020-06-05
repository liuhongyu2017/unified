package com.azxc.unified.entity;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.data.SuperEntity;
import com.azxc.unified.common.utils.HttpServletUtil;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_file")
public class Upload extends SuperEntity {

  /**
   * 文件名称
   */
  private String name;

  /**
   * 文件路径
   */
  private String path;

  /**
   * 文件类型
   */
  private String mine;

  /**
   * 文件大小
   */
  private Long size;

  /**
   * 文件md5值
   */
  private String md5;

  /**
   * 文件sha1值
   */
  private String sha1;

  /**
   * 获取文件绝对路径
   */
  public String getUrl() {
    HttpServletRequest request = HttpServletUtil.getRequest();
    if (!StrUtil.isNotEmpty(path)) {
      StringBuffer url = request.getRequestURL();
      String baseUrl = url.delete(url.length() - request.getRequestURI().length(), url.length())
          .append(request.getContentLength()).toString();
      return baseUrl + path;
    }
    return path;
  }
}
