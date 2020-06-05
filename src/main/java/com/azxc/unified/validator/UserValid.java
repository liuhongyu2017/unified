package com.azxc.unified.validator;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 用户数据校验层
 *
 * @author lhy
 * @version 1.0 2020/3/16
 */
@Data
public class UserValid implements Serializable {

  @NotEmpty(message = "用户名不能为空")
  private String username;

  @NotEmpty(message = "用户昵称不能为空")
  @Size(min = 2, message = "用户昵称最少两个字符")
  private String nickname;

  private String confirm;

  @Pattern(regexp = "^$|^1[3456789]\\d{9}$", message = "移动电话码号不合法")
  private String mobilePhone;

  @Pattern(regexp = "^$|^[0-9]*&", message = "办公电话不合法")
  private String officePhone;

  @Pattern(regexp = "^$|^[0-9]*&", message = "家庭电话不合法")
  private String homePhone;

  @Pattern(regexp = "^$|^[0-9]*&", message = "虚拟号码不合法")
  private String virtualNum;

  @Pattern(regexp = "^$|^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "电子邮箱不合法")
  private String email;
}
