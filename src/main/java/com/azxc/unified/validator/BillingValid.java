package com.azxc.unified.validator;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * 账单验证类
 *
 * @author lhy
 * @version 1.0 2020/4/1
 */
@Data
public class BillingValid implements Serializable {

  @Pattern(regexp = "^[0-9]+.[0-9]{2}$", message = "金额输入不合法")
  @NotEmpty(message = "金额不能为空")
  private String vAmount;

  @Pattern(regexp = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))$", message = "日期不合法")
  @NotEmpty(message = "日期不能为空")
  private String vDate;
}
