package com.azxc.unified.service.impl;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.jpaFilter.annotation.JpaFilter;
import com.azxc.unified.common.data.jpaFilter.filter.DeptFilter;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.exception.ResultException;
import com.azxc.unified.entity.Dept;
import com.azxc.unified.entity.User;
import com.azxc.unified.repository.DeptRepository;
import com.azxc.unified.repository.UserRepository;
import com.azxc.unified.service.DeptService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/3/19
 */
@Service
public class DeptServiceImpl implements DeptService {

  private final DeptRepository deptRepository;
  private final UserRepository userRepository;

  public DeptServiceImpl(DeptRepository deptRepository, UserRepository userRepository) {
    this.deptRepository = deptRepository;
    this.userRepository = userRepository;
  }

  @JpaFilter(DeptFilter.class)
  @Override
  public List<Dept> getList(Example<Dept> example) {
    Sort sort = Sort.by(Direction.ASC, "sort");
    return deptRepository.findAll(example, sort);
  }

  @Override
  public Long getSortMax(Long pid) {
    return deptRepository.findSortMax(pid);
  }

  @JpaFilter(DeptFilter.class)
  @Override
  public List<Dept> getListByPid(Long pid, Long noId) {
    Sort sort = Sort.by(Direction.ASC, "sort");
    return deptRepository.findByPidAndIdNot(sort, pid, noId);
  }

  @Transactional
  @Override
  public List<Dept> save(List<Dept> depts) {
    return deptRepository.saveAll(depts);
  }

  @Transactional
  @Override
  public Dept save(Dept dept) {
    return deptRepository.save(dept);
  }

  @JpaFilter(DeptFilter.class)
  @Override
  public Dept getById(Long id) {
    return deptRepository.findById(id).orElse(null);
  }

  @JpaFilter(DeptFilter.class)
  @Override
  public List<Dept> getListByPidLikeOk(Long id) {
    return deptRepository.findByPidsLikeAndStatus("%[" + id + "]%", StatusEnum.OK.getCode());
  }

  @Transactional
  @Override
  public boolean updateStatus(StatusEnum statusEnum, List<Long> ids) {
    // 获取所有与之有关联的部门
    Set<Dept> treeDepts = new HashSet<>();
    List<Dept> depts = deptRepository.findByIdIn(ids);
    depts.forEach(dept -> {
      treeDepts.add(dept);
      treeDepts.addAll(
          deptRepository.findByPidsLikeAndStatus("%[" + dept.getId() + "]%", dept.getStatus())
      );
    });

    Sort sort = Sort.by(Direction.ASC, "dept.sort", "sort");
    treeDepts.forEach(dept -> {
      if (statusEnum == StatusEnum.DELETE) {
        List<User> users = userRepository.findByDept(sort, dept);
        if (users.size() > 0) {
          throw new ResultException(ResultEnum.DEPT_EXIST_USER);
        }
      }
      // 更新关联的所有部门状态
      dept.setStatus(statusEnum.getCode());
    });
    return treeDepts.size() > 0;
  }

  @JpaFilter(DeptFilter.class)
  @Override
  public List<Dept> getListBySortOk() {
    Sort sort = Sort.by(Direction.ASC, "sort");
    Byte status = StatusEnum.OK.getCode();
    return deptRepository.findAllByStatus(sort, status);
  }

  @Override
  public Set<Dept> getOkListByRole(Long id) {
    Byte status = StatusEnum.OK.getCode();
    return deptRepository.findByRoles_IdAndStatus(id, status);
  }

  @Override
  public List<Dept> getList() {
    Sort sort = Sort.by(Direction.ASC, "sort");
    return deptRepository.findAll(sort);
  }
}
