package com.azxc.unified.service;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.entity.Dept;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * 用户
 *
 * @author lhy
 * @version 1.0 2020/3/16
 */
public interface UserService {

  /**
   * 获取分页列表数据
   *
   * @param user 查询条件
   * @return 返回分页数据
   */
  Page<User> getPageList(User user);

  /**
   * 根据部门查询用户
   *
   * @param dept 部门实体类
   * @return 分页结果
   */
  Page<User> getPageListByDept(Dept dept);

  /**
   * 保存用户
   *
   * @param user 实体类
   * @return 保存结果
   */
  User save(User user);

  /**
   * 批量保存用户
   *
   * @param users 实体类集合
   * @return 保存结果
   */
  List<User> save(List<User> users);

  /**
   * 判断用户名是否唯一
   *
   * @param user 用户实体类
   * @return true:不唯一
   */
  boolean repeatByUsername(User user);

  /**
   * 根据id查询
   *
   * @param id 主键
   * @return 查询结果
   */
  User getById(Long id);

  /**
   * 获取最大排序
   *
   * @param dept 部门
   * @return 查询结果
   */
  Long getMaxSort(Dept dept);

  /**
   * 批量更新状态
   *
   * @param statusEnum 数据状态
   * @param ids        主键集合
   * @return 操作结果
   */
  boolean updateStatus(StatusEnum statusEnum, List<Long> ids);

  /**
   * 根据用户名查询用户
   *
   * @param username 用户名
   * @return 查询结果
   */
  User getByName(String username);

  /**
   * 根据部门id，查询用户排序
   *
   * @param dept 部门
   * @return 查询结果
   */
  List<User> getListByDeptSort(Dept dept);

  /**
   * 批量修改用户排序
   *
   * @param users 用户实体类
   */
  void updateSortById(List<User> users);

  /**
   * 根据角色查询用户
   *
   * @param role 角色实体
   * @return 查询结果
   */
  List<User> getListByRole(Role role);

  /**
   * 获取用户
   *
   * @return 查询结果
   */
  List<User> getList();

  /**
   * 获取状态正常
   *
   * @param notId 排除的id
   * @return 查询结果
   */
  List<User> getListOk(Long notId);
}
