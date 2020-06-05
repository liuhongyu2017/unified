package com.azxc.unified.repository;

import com.azxc.unified.common.constant.StatusConst;
import com.azxc.unified.common.data.BaseRepository;
import com.azxc.unified.entity.Dept;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户
 *
 * @author lhy
 * @version 1.0 2020/3/16
 */
public interface UserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {

  /**
   * 根据用户名字查询用户
   *
   * @param username 用户名字
   * @return 查询结果
   */
  User findByUsername(String username);

  /**
   * 根据用户名查询用户，排除指定id
   *
   * @param username 用户名
   * @param id       排除的id
   * @return 查询结果
   */
  User findByUsernameAndIdNot(String username, Long id);

  /**
   * 删除多条数据
   *
   * @param ids id集合
   * @return 影响行数
   */
  Integer deleteByIdIn(List<Long> ids);

  /**
   * 获取排序最大的一条
   *
   * @param dept 部门
   * @return 查询结果
   */
  @Query("select max(user.sort) from User user where user.dept = ?1 and user.status <>"
      + StatusConst.DELETE)
  Long findSortMax(Dept dept);

  /**
   * 查询部门下的全部用户
   *
   * @param sort 排序实体
   * @param dept 部门
   * @return 查询结果
   */
  List<User> findByDept(Sort sort, Dept dept);

  /**
   * 修改用户排序字段
   *
   * @param id   用户id
   * @param sort 排序
   */
  @Modifying
  @Query("update User set sort = ?2 where  id = ?1")
  void updateUserSort(Long id, Long sort);

  /**
   * 根据角色查询用户
   *
   * @param role 角色实体
   * @return 查询结果
   */
  @Query("from User user join user.roles role where role=?1 order by user.dept.sort, user.sort")
  List<User> findByRoles(Role role);

  /**
   * 根据状态获取用户
   *
   * @param sort   排序实体
   * @param status 状态
   * @param notId  排除的id
   * @return 查询结果
   */
  List<User> findByStatusAndIdNot(Sort sort, Byte status, Long notId);
}
