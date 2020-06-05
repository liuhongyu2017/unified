package com.azxc.unified.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 需要排序的表继承这个接口
 *
 * @author lhy
 * @version 1.0 2020/3/31
 */
public interface SortEntity {

  /**
   * 主键
   */
  Long getId();

  void setId(Long id);

  /**
   * 排序
   */
  Long getSort();

  void setSort(Long sort);
}
