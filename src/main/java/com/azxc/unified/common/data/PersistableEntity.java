package com.azxc.unified.common.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 * 主键生成策略
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Data
@MappedSuperclass
public class PersistableEntity implements Serializable {

  /**
   * 主键生成策略：snowflake 算法。
   */
  @JsonSerialize(using = ToStringSerializer.class)
  @GenericGenerator(name = "idGenerator", strategy = "com.azxc.unified.common.data.SnowflakeGenerateId")
  @GeneratedValue(generator = "idGenerator")
  @Id
  private Long id;
}
