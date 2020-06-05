package com.azxc.unified.entity;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.SuperEntity;
import com.azxc.unified.common.utils.StatusUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

/**
 * 数据字典
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@Where(clause = StatusUtil.NOT_DELETE)
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_dict")
public class Dict extends SuperEntity {

  /**
   * 字典标识
   */
  private String name;

  /**
   * 字典名称
   */
  private String title;

  /**
   * 字典类型
   */
  private Byte type;

  /**
   * 字典键值
   */
  @Lob
  @Column(columnDefinition = "TEXT")
  private String value;

  /**
   * 备注
   */
  @Lob
  @Column(columnDefinition = "TEXT")
  private String remark;

  /**
   * 数据状态
   */
  private Byte status = StatusEnum.OK.getCode();
}
