package com.azxc.unified.common.data;

import java.util.List;

/**
 * 需要排序的 service 需要实现的接口
 *
 * @author lhy
 * @version 1.0 2020/3/31
 */
public interface ParentService<T extends SortEntity> {

  /**
   * 根据ID查询
   *
   * @param id 主键
   * @return 查询结果
   */
  T getById(Long id);

  /**
   * 根据父级 ID 查询本级全部
   *
   * @param pid  父级ID
   * @param noId 需要排除的id
   * @return 查询结果
   */
  List<T> getListByPid(Long pid, Long noId);
}
