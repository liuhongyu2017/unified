package com.azxc.unified.common.data;

import com.azxc.unified.common.utils.HttpServletUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装URL地址，自动添加应用上下文
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@Data
@NoArgsConstructor
public class URL {

  private String url;

  public URL(String url) {
    this.url = HttpServletUtil.getRequest().getContextPath() + url;
  }
}
