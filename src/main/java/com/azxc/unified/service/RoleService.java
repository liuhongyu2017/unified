package com.azxc.unified.service;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.entity.Role;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

/**
 * 角色
 *
 * @author lhy
 * @version 1.0 2020/3/20
 */
public interface RoleService {

  /**
   * 获取用户角色列表
   *
   * @param id 用户ID
   * @return 查询结果
   */
  Set<Role> getUserOkRoleList(Long id);

  /**
   * 判断用户是否存在角色
   *
   * @param id 用户ID
   * @return true:存在角色
   */
  boolean existsUserOk(Long id);

  /**
   * 根据ID查询角色
   *
   * @param id 角色ID
   * @return 查询结果
   */
  Role getById(Long id);

  /**
   * 分页查询角色
   *
   * @param example 查询实例
   * @return 分页查询结果
   */
  Page<Role> getPageList(Example<Role> example);

  /**
   * 获取角色列表数据
   *
   * @return 查询结果
   */
  List<Role> getListBySortOk();

  /**
   * 判断角色标识是否重复
   *
   * @param role 角色实体类
   * @return true:标识重复
   */
  boolean repeatByName(Role role);

  /**
   * 保存角色
   *
   * @param role 角色实体类
   * @return 保存结果
   */
  Role save(Role role);

  /**
   * 批量修改状态（启用、冻结、删除）
   *
   * @param statusEnum 数据状态
   * @param ids        数据ID列表
   * @return 操作结果
   */
  boolean updateStatus(StatusEnum statusEnum, List<Long> ids);
}
