package com.azxc.unified.repository;

import com.azxc.unified.common.constant.StatusConst;
import com.azxc.unified.common.data.BaseRepository;
import com.azxc.unified.entity.Dept;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

/**
 * 部门
 *
 * @author lhy
 * @version 1.0 2020/3/19
 */
public interface DeptRepository extends BaseRepository<Dept, Long> {

  /**
   * 获取最大排序值
   *
   * @param pid 父级ID
   * @return 最大排序值
   */
  @Query("select max(d.sort) from Dept d where d.pid = ?1 and d.status <>" + StatusConst.DELETE)
  Long findSortMax(Long pid);

  /**
   * 根据父ID查找子孙部门
   *
   * @param pids   pid列表
   * @param status 数据状态
   * @return 查询结果
   */
  List<Dept> findByPidsLikeAndStatus(String pids, Byte status);

  /**
   * 根据父级部门ID获取本级全部部门
   *
   * @param sort  排序对象
   * @param pid   pid 父部门ID
   * @param notId 需要排除的ID
   * @return 查询结果
   */
  List<Dept> findByPidAndIdNot(Sort sort, Long pid, Long notId);

  /**
   * 根据ID查询
   *
   * @param ids ID集合
   * @return 查询结果
   */
  List<Dept> findByIdIn(List<Long> ids);

  /**
   * 根据状态查询部门
   *
   * @param sort   排序
   * @param status 状态
   * @return 查询结果
   */
  List<Dept> findAllByStatus(Sort sort, Byte status);

  /**
   * 根据角色id和状态查询
   *
   * @param id     角色id
   * @param status 状态
   * @return 查询结果
   */
  Set<Dept> findByRoles_IdAndStatus(Long id, Byte status);
}
