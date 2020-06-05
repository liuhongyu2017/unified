package com.azxc.unified.validator;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author lhy
 * @version 1.0 2020/3/19
 */
@Data
public class DeptValid implements Serializable {

  @NotEmpty(message = "部门名称不能为空")
  private String title;

  @NotNull(message = "父级部门不能为空")
  private Long pid;
}
