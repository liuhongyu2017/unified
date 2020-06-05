package com.azxc.unified.validator;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 角色校验
 *
 * @author lhy
 * @version 1.0 2020/3/20
 */
@Data
public class RoleValid implements Serializable {

  @Pattern(regexp = "^[a-zA-Z_]*$", message = "角色标识不合法，只能由英文字母和 _ 组成")
  @NotEmpty(message = "角色标识不能为空")
  private String name;

  @NotEmpty(message = "角色名称不能为空")
  private String title;
}
