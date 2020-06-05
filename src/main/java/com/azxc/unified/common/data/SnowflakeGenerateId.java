package com.azxc.unified.common.data;

import cn.hutool.core.util.IdUtil;
import com.azxc.unified.common.properties.ProjectProperties;
import com.azxc.unified.common.utils.SpringContextUtil;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Snowflake id生成算法
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public class SnowflakeGenerateId implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor,
      Object o) throws HibernateException {
    ProjectProperties projectProperties = SpringContextUtil.getBean(ProjectProperties.class);
    return IdUtil.getSnowflake(projectProperties.getWorkId(), projectProperties.getDataCenterId())
        .nextId();
  }
}
