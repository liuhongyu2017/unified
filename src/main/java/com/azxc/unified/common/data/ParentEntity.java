package com.azxc.unified.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 上下级
 *
 * @author lhy
 * @version 1.0 2020/3/31
 */
public interface ParentEntity extends SortEntity {

  /**
   * 主键
   */
  Long getId();

  void setId(Long id);

  /**
   * 上级id
   */
  Long getPid();

  void setPid(Long pid);

  /**
   * 全部上级ids
   */
  String getPids();

  void setPids(String pids);
}
