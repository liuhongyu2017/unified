package com.azxc.unified.common.data.jpaFilter.filter;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.jpaFilter.JpaFilter;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.entity.Role;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 * 根据授权的部门提供过滤
 *
 * @author lhy
 * @version 1.0 2020/3/26
 */
@Component
public class DeptFilter implements JpaFilter {

  @Override
  public void action(EntityManager entityManager) {
    Set<Role> roles = ShiroUtil.getSubjectRoles();
    Set<Long> deptIds = new HashSet<>();
    deptIds.add(Long.MIN_VALUE);
    roles.forEach(role -> {
      role.setDepts(ShiroUtil.getSubjectDepts(role));
      role.getDepts().forEach(dept -> {
        if (StatusEnum.OK.getCode().equals(dept.getStatus())) {
          deptIds.add(dept.getId());
        }
      });
    });
    try {
      Filter filter =
          entityManager.unwrap(Session.class).enableFilter(DeptFilter.class.getSimpleName());
      filter.setParameterList("deptId", deptIds);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
