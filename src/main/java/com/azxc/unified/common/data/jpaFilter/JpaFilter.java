package com.azxc.unified.common.data.jpaFilter;

import javax.persistence.EntityManager;

/**
 * hibernate 过滤器继承
 *
 * @author lhy
 * @version 1.0 2020/3/26
 */
public interface JpaFilter {

  /**
   * 过滤器具体执行方法
   *
   * @param entityManager hibernate 管理器
   */
  void action(EntityManager entityManager);
}
