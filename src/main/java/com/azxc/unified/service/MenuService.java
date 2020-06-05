package com.azxc.unified.service;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.ParentService;
import com.azxc.unified.entity.Menu;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Example;

/**
 * 菜单
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
public interface MenuService extends ParentService<Menu> {

  /**
   * 查询集合，根据sort正序排序
   *
   * @return 查询结果
   */
  List<Menu> getListBySort();

  /**
   * 后台主页菜单
   *
   * @return 查询结果
   */
  Map<String, Menu> getTreeMenu();

  /**
   * 根据条件查询，并根据 sort 正序排序
   *
   * @param example 查询条件
   * @return 查询结果
   */
  List<Menu> getListByExampleSort(Example<Menu> example);

  /**
   * 根据主键查询菜单
   *
   * @param id 主键
   * @return 查询结果
   */
  Menu getById(Long id);

  /**
   * 根据父级菜单ID获取本级全部菜单
   *
   * @param pid   父级ID
   * @param notId 需要排除的菜单ID
   * @return 查询结果
   */
  List<Menu> getListByPid(Long pid, Long notId);

  /**
   * 根据父级ID查询最大排序
   *
   * @param pid 父级ID
   * @return 查询结果
   */
  Long getSortMaxByPid(Long pid);

  /**
   * 保存菜单
   *
   * @param menu 实体类
   * @return 保存结果
   */
  Menu save(Menu menu);

  /**
   * 批量保存
   *
   * @param menus 实体类集合
   * @return 保存结果
   */
  List<Menu> save(List<Menu> menus);

  /**
   * 批量更新状态
   *
   * @param statusEnum 数据状态
   * @param ids        主键集合
   * @return true:更新成功
   */
  boolean updateStatus(StatusEnum statusEnum, List<Long> ids);

  /**
   * 查询状态正常的集合
   *
   * @return 查询结果
   */
  List<Menu> getListBySortOk();

  /**
   * 根据id查询子孙
   *
   * @param id ID
   * @return 查询结果
   */
  List<Menu> getListByPidLikeOk(Long id);
}
