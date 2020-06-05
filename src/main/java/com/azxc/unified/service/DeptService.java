package com.azxc.unified.service;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.ParentService;
import com.azxc.unified.entity.Dept;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Example;

/**
 * 部门
 *
 * @author lhy
 * @version 1.0 2020/3/19
 */
public interface DeptService extends ParentService<Dept> {

  /**
   * 获取部门列表
   *
   * @param example 查询实例
   * @return 查询结果
   */
  List<Dept> getList(Example<Dept> example);

  /**
   * 获取最大排序
   *
   * @param pid 父级ID
   * @return 排序最大值
   */
  Long getSortMax(Long pid);

  /**
   * 根据父级部门ID查询本级全部部门
   *
   * @param pid  父级部门ID
   * @param noId 需要排除的id
   * @return 查询结果
   */
  List<Dept> getListByPid(Long pid, Long noId);

  /**
   * 批量保存部门
   *
   * @param depts 部门实例
   * @return 保存结果
   */
  List<Dept> save(List<Dept> depts);

  /**
   * 保存部门
   *
   * @param dept 部门实例
   * @return 保存结果
   */
  Dept save(Dept dept);

  /**
   * 根据ID查询
   *
   * @param id 主键
   * @return 查询结果
   */
  Dept getById(Long id);

  /**
   * 根据id查询子孙部门
   *
   * @param id 部门ID
   * @return 查询结果
   */
  List<Dept> getListByPidLikeOk(Long id);

  /**
   * 批量修改状态（启动、冻结、删除）
   *
   * @param statusEnum 数据状态
   * @param ids        部门ID数组
   * @return 操作结果
   */
  boolean updateStatus(StatusEnum statusEnum, List<Long> ids);

  /**
   * 查询状态正常的集合
   *
   * @return 查询结果
   */
  List<Dept> getListBySortOk();

  /**
   * 根据角色id查询状态正常的部门
   *
   * @param id 角色id
   * @return 测试结果
   */
  Set<Dept> getOkListByRole(Long id);

  /**
   * 获取机构
   *
   * @return 查询结果
   */
  List<Dept> getList();
}
