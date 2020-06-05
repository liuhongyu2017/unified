package com.azxc.unified.repository;

import com.azxc.unified.common.constant.StatusConst;
import com.azxc.unified.common.data.BaseRepository;
import com.azxc.unified.entity.Menu;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

/**
 * 菜单
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
public interface MenuRepository extends BaseRepository<Menu, Long> {

  /**
   * 根据状态查询菜单
   *
   * @param sort   排序
   * @param status 状态
   * @return 查询结果
   */
  List<Menu> findAllByStatus(Sort sort, Byte status);

  /**
   * 根据父级菜单ID获取本级全部菜单
   *
   * @param sort  排序对象
   * @param pid   父级菜单ID
   * @param notId 需要排除的菜单ID
   * @return 查询结果
   */
  List<Menu> findByPidAndIdNot(Sort sort, Long pid, Long notId);

  /**
   * 获取最大排序值
   *
   * @param pid 父级ID
   * @return 最大排序值
   */
  @Query("select max(m.sort) from Menu m where m.pid = ?1 and m.status <> " + StatusConst.DELETE)
  Long findSortMax(Long pid);

  /**
   * 根据主键批量查询菜单
   *
   * @param ids 主键
   * @return 查询结果
   */
  List<Menu> findByIdIn(List<Long> ids);

  /**
   * 根据父ID查找子菜单
   *
   * @param pids   pid列表
   * @param status 数据状态
   * @return 查询结果
   */
  List<Menu> findByPidsLikeAndStatus(String pids, Byte status);

  /**
   * 根据状态查询菜单
   *
   * @param sort   排序实例
   * @param status 状态
   * @return 查询结果
   */
  List<Menu> findByStatus(Sort sort, Byte status);
}
