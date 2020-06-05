package com.azxc.unified.service.impl;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.PageSort;
import com.azxc.unified.common.data.jpaFilter.annotation.JpaFilter;
import com.azxc.unified.common.data.jpaFilter.filter.DeptFilter;
import com.azxc.unified.entity.Dept;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import com.azxc.unified.repository.UserRepository;
import com.azxc.unified.service.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/3/16
 */
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Page<User> getPageList(User user) {
    PageRequest pageRequest = PageSort.pageRequest(Direction.ASC, "dept.sort", "sort");
    return userRepository.findAll(
        (Specification<User>) (root, query, cb) -> {
          List<Predicate> preList = new ArrayList<>();
          if (user == null) {
            return query.getRestriction();
          }
          if (user.getId() != null) {
            preList.add(cb.equal(root.get("id").as(Long.class), user.getId()));
          }
          if (StrUtil.isNotEmpty(user.getUsername())) {
            preList.add(
                cb.like(root.get("username").as(String.class), "%" + user.getUsername() + "%"));
          }
          if (StrUtil.isNotEmpty(user.getNickname())) {
            preList.add(
                cb.like(root.get("nickname").as(String.class), "%" + user.getNickname() + "%"));
          }
          if (user.getStatus() != null) {
            preList.add(cb.equal(root.get("status").as(Byte.class), user.getStatus()));
          }
          if (user.getDept() != null) {
            preList.add(cb.equal(root.get("dept").as(Dept.class), user.getDept()));
          }
          if (user.getJob() != null) {
            preList.add(cb.equal(root.get("job").as(Byte.class), user.getJob()));
          }
          Predicate[] pres = new Predicate[preList.size()];
          return query.where(preList.toArray(pres)).getRestriction();
        }, pageRequest);
  }

  @Override
  public Page<User> getPageListByDept(Dept dept) {
    PageRequest pageRequest = PageSort.pageRequest(Direction.ASC, "dept.sort", "sort");
    return userRepository.findAll(
        (Specification<User>) (root, query, cb) -> {
          List<Predicate> preList = new ArrayList<>();
          preList.add(cb.equal(root.get("dept").as(Dept.class), dept));
          Predicate[] pres = new Predicate[preList.size()];
          return query.where(preList.toArray(pres)).getRestriction();
        }, pageRequest);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public List<User> save(List<User> users) {
    return userRepository.saveAll(users);
  }

  @Override
  public boolean repeatByUsername(User user) {
    Long id = user.getId() != null ? user.getId() : Long.MIN_VALUE;
    return userRepository.findByUsernameAndIdNot(user.getUsername(), id) != null;
  }

  @Override
  public User getById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public Long getMaxSort(Dept dept) {
    return userRepository.findSortMax(dept);
  }

  @Transactional
  @Override
  public boolean updateStatus(StatusEnum statusEnum, List<Long> ids) {
    if (statusEnum == StatusEnum.DELETE) {
      return userRepository.deleteByIdIn(ids) > 0;
    }
    return userRepository.updateStatus(statusEnum.getCode(), ids) > 0;
  }

  @Override
  public User getByName(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public List<User> getListByDeptSort(Dept dept) {
    Sort sort = Sort.by(Direction.ASC, "dept.sort", "sort");
    return userRepository.findByDept(sort, dept);
  }

  @Transactional
  @Override
  public void updateSortById(List<User> users) {
    users.forEach(user -> {
      if (user.getId() != null && user.getSort() != null) {
        userRepository.updateUserSort(user.getId(), user.getSort());
      }
    });
  }

  @Override
  public List<User> getListByRole(Role role) {
    return userRepository.findByRoles(role);
  }

  @Override
  public List<User> getList() {
    Sort sort = Sort.by(Direction.ASC, "dept.sort", "sort");
    return userRepository.findAll(sort);
  }

  @Override
  public List<User> getListOk(Long notId) {
    Sort sort = Sort.by(Direction.ASC, "dept.sort", "sort");
    return userRepository.findByStatusAndIdNot(sort, StatusEnum.OK.getCode(), notId);
  }
}
