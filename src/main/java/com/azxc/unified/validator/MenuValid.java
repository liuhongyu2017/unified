package com.azxc.unified.validator;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 菜单校验
 *
 * @author lhy
 * @version 1.0 2020/3/18
 */
@Data
public class MenuValid implements Serializable {

  @NotEmpty(message = "标题不能为空")
  private String title;

  @NotNull(message = "父级菜单不能为空")
  private Long pid;

  @NotEmpty(message = "url地址不能为空，可以输入#代替")
  private String url;

  @NotNull(message = "菜单类型不能为空")
  private Byte type;
}
