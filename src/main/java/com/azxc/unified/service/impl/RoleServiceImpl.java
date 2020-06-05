package com.azxc.unified.service.impl;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.PageSort;
import com.azxc.unified.entity.Role;
import com.azxc.unified.repository.RoleRepository;
import com.azxc.unified.service.RoleService;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/3/20
 */
@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Transactional
  @Override
  public Set<Role> getUserOkRoleList(Long id) {
    Byte status = StatusEnum.OK.getCode();
    return roleRepository.findByUsers_IdAndStatus(id, status);
  }

  @Override
  public boolean existsUserOk(Long id) {
    Byte status = StatusEnum.OK.getCode();
    return roleRepository.existsByUsers_IdAndStatus(id, status);
  }

  @Transactional
  @Override
  public Role getById(Long id) {
    return roleRepository.findById(id).orElse(null);
  }

  @Override
  public Page<Role> getPageList(Example<Role> example) {
    PageRequest page = PageSort.pageRequest(Direction.ASC);
    return roleRepository.findAll(example, page);
  }

  @Override
  public List<Role> getListBySortOk() {
    Sort sort = Sort.by(Direction.ASC, "createdDate");
    return roleRepository.findAllByStatus(sort, StatusEnum.OK.getCode());
  }

  @Override
  public boolean repeatByName(Role role) {
    Long id = (role.getId() != null) ? role.getId() : Long.MIN_VALUE;
    return roleRepository.findByNameAndIdNot(role.getName(), id) != null;
  }

  @Override
  public Role save(Role role) {
    return roleRepository.save(role);
  }

  @Transactional
  @Override
  public boolean updateStatus(StatusEnum statusEnum, List<Long> ids) {
    // 删除角色操作之前取消与角色关联的用户和功能
    if (statusEnum == StatusEnum.DELETE) {
      roleRepository.cancelFunJoin(ids);
      roleRepository.cancelUserJoin(ids);
    }
    return roleRepository.updateStatus(statusEnum.getCode(), ids) > 0;
  }
}
