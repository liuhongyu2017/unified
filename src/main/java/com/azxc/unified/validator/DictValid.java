package com.azxc.unified.validator;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 字典校验类
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
@Data
public class DictValid implements Serializable {

  @Pattern(regexp = "^[a-zA-Z_]*$", message = "字典标识不合法，只能由英文字母和 _ 组成")
  @NotEmpty(message = "字典标识不能为空")
  private String name;

  @NotEmpty(message = "字典标题不能为空")
  private String title;

  @Pattern(regexp = "^([0-9]?:[a-zA-Z\u4e00-\u9fa5]*,?)*$", message = "字典值不合法")
  @NotEmpty(message = "字典值不能为空")
  private String value;
}
