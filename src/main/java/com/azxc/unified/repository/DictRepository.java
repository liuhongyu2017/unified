package com.azxc.unified.repository;

import com.azxc.unified.common.data.BaseRepository;
import com.azxc.unified.entity.Dict;
import java.util.List;

/**
 * 字典
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public interface DictRepository extends BaseRepository<Dict, Long> {

  /**
   * 根据字典标识符查询
   *
   * @param name   字典标识符
   * @param status 状态
   * @return 查询结果
   */
  Dict findByNameAndStatus(String name, Byte status);

  /**
   * 根据字典标识查询指定的字典项，排序指定id
   *
   * @param name 字典标识
   * @param id   排序id
   * @return 查询结果
   */
  Dict findByNameAndIdNot(String name, Long id);

  /**
   * 根据字典类型和字典id查询
   *
   * @param type 字典类型
   * @param ids  id集合
   * @return 查询结果
   */
  List<Dict> findByTypeAndIdIn(Byte type, List<Long> ids);
}
