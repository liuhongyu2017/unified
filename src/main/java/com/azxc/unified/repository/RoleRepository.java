package com.azxc.unified.repository;

import com.azxc.unified.common.data.BaseRepository;
import com.azxc.unified.entity.Role;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 角色
 *
 * @author lhy
 * @version 1.0 2020/3/20
 */
public interface RoleRepository extends BaseRepository<Role, Long> {

  /**
   * 查询指定用户的角色列表
   *
   * @param id     用户ID
   * @param status 角色状态
   * @return 查询结果
   */
  Set<Role> findByUsers_IdAndStatus(Long id, Byte status);

  /**
   * 判断指定的用户是否存在角色
   *
   * @param id     用户ID
   * @param status 角色状态
   * @return true:存在
   */
  Boolean existsByUsers_IdAndStatus(Long id, Byte status);

  /**
   * 根据状态查询角色
   *
   * @param sort   排序实例
   * @param status 状态
   * @return 查询结果
   */
  List<Role> findAllByStatus(Sort sort, Byte status);

  /**
   * 根据标识查询角色数据，并且排除指定ID的角色
   *
   * @param name  角色标识
   * @param notId 排除ID
   * @return 查询结果
   */
  Role findByNameAndIdNot(String name, Long notId);

  /**
   * 取消角色和用户之间的关联
   *
   * @param ids 角色ID
   * @return 影响行数
   */
  @Transactional
  @Modifying
  @Query(value = "delete from sys_user_role where role_id in ?1", nativeQuery = true)
  Integer cancelUserJoin(List<Long> ids);

  /**
   * 取消角色和功能之间的关系
   *
   * @param ids 角色ID
   * @return 影响行数
   */
  @Transactional
  @Modifying
  @Query(value = "delete from sys_role_menu where role_id in ?1", nativeQuery = true)
  Integer cancelFunJoin(List<Long> ids);
}
