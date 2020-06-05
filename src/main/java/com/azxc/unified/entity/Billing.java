package com.azxc.unified.entity;

import com.azxc.unified.common.convert.JsonDictConvert;
import com.azxc.unified.common.data.SuperEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 账单
 *
 * @author lhy
 * @version 1.0 2020/4/1
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "module_billing")
public class Billing extends SuperEntity {

  /**
   * 金额
   */
  private Long amount;

  /**
   * 日期
   */
  private Date date;

  /**
   * 类型
   */
  @JsonDictConvert("BILLING_TYPE")
  private Byte type;

  /**
   * 备注
   */
  @Lob
  @Column(columnDefinition = "TEXT")
  private String remark;
}
