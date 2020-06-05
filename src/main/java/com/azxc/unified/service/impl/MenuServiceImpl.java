package com.azxc.unified.service.impl;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.constant.FunTypeEnum;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.utils.EhCacheUtil;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.entity.Menu;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import com.azxc.unified.repository.MenuRepository;
import com.azxc.unified.service.MenuService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/3/17
 */
@CacheConfig(cacheNames = "menu")
@Service
public class MenuServiceImpl implements MenuService {

  private final MenuRepository menuRepository;

  public MenuServiceImpl(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  @Override
  public List<Menu> getListBySort() {
    Sort sort = Sort.by(Direction.ASC, "type", "sort");
    return menuRepository.findAllByStatus(sort, StatusEnum.OK.getCode());
  }

  @Cacheable
  @Override
  public Map<String, Menu> getTreeMenu() {
    // 获取当前登入的用户
    User user = ShiroUtil.getSubject();
    // 菜单键值对（ID -> 菜单）
    Map<String, Menu> keyMenu = new HashMap<>(16);
    // 封装菜单树形数据
    Map<String, Menu> treeMenu = new HashMap<>();
    // 超级管理员获取全部菜单
    if (Objects.equals(user.getId(), AdminConst.ADMIN_ID)) {
      List<Menu> menus = this.getListBySort();
      menus.forEach(menu -> keyMenu.put(StrUtil.toString(menu.getId()), menu));
    } else {
      // 其他用户需要从相应的角色中获取菜单资源
      Set<Role> roles = ShiroUtil.getSubjectRoles();
      roles.forEach(role -> role.getMenus().forEach(menu -> {
        if (Objects.equals(menu.getStatus(), StatusEnum.OK.getCode())) {
          keyMenu.put(StrUtil.toString(menu.getId()), menu);
        }
      }));
    }
    // 封装树形数据
    keyMenu.forEach((id, menu) -> {
      if (FunTypeEnum.DIRECTORY.getCode().equals(menu.getType())
          || FunTypeEnum.MENU.getCode().equals(menu.getType())) {
        if (keyMenu.get(StrUtil.toString(menu.getPid())) != null) {
          keyMenu.get(StrUtil.toString(menu.getPid())).getChildren()
              .put(StrUtil.toString(menu.getSort()), menu);
        } else {
          treeMenu.put(StrUtil.toString(menu.getSort()), menu);
        }
      }
    });
    return treeMenu;
  }

  @Override
  public List<Menu> getListByExampleSort(Example<Menu> example) {
    Sort sort = Sort.by(Direction.ASC, "sort");
    return menuRepository.findAll(example, sort);
  }

  @Override
  public Menu getById(Long id) {
    return menuRepository.findById(id).orElse(null);
  }

  @Override
  public List<Menu> getListByPid(Long pid, Long notId) {
    Sort sort = Sort.by(Direction.ASC, "sort");
    return menuRepository.findByPidAndIdNot(sort, pid, notId);
  }

  @Override
  public Long getSortMaxByPid(Long pid) {
    return menuRepository.findSortMax(pid);
  }

  @CacheEvict(allEntries = true)
  @Transactional
  @Override
  public Menu save(Menu menu) {
    return menuRepository.save(menu);
  }

  @CacheEvict(allEntries = true)
  @Transactional
  @Override
  public List<Menu> save(List<Menu> menus) {
    return menuRepository.saveAll(menus);
  }

  @CacheEvict(allEntries = true)
  @Transactional
  @Override
  public boolean updateStatus(StatusEnum statusEnum, List<Long> ids) {
    // 获取与之关联的所有菜单
    Set<Menu> treeMenus = new HashSet<>();
    List<Menu> menus = menuRepository.findByIdIn(ids);
    menus.forEach(menu -> {
      treeMenus.add(menu);
      treeMenus.addAll(
          menuRepository.findByPidsLikeAndStatus("%[" + menu.getId() + "]%", menu.getStatus())
      );
    });

    treeMenus.forEach(menu -> {
      // 更新关联的所有菜单状态
      menu.setStatus(statusEnum.getCode());
    });

    return treeMenus.size() > 0;
  }

  @Override
  public List<Menu> getListBySortOk() {
    Sort sort = Sort.by(Direction.ASC, "sort");
    Byte status = StatusEnum.OK.getCode();
    return menuRepository.findAllByStatus(sort, status);
  }

  @Override
  public List<Menu> getListByPidLikeOk(Long id) {
    return menuRepository.findByPidsLikeAndStatus("%[" + id + "]%", StatusEnum.OK.getCode());
  }
}
