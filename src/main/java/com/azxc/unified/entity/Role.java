package com.azxc.unified.entity;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.convert.JsonDictConvert;
import com.azxc.unified.common.data.SuperEntity;
import com.azxc.unified.common.utils.StatusUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

/**
 * 角色
 *
 * @author lhy
 * @version 1.0 2020/3/19
 */
@ToString(exclude = {"users", "menus", "depts"})
@EqualsAndHashCode(callSuper = true, exclude = {"users", "menus", "depts"})
@Where(clause = StatusUtil.NOT_DELETE)
@Data
@Entity
@Table(name = "sys_role")
public class Role extends SuperEntity {

  /**
   * 角色标识
   */
  private String name;

  /**
   * 角色名称
   */
  private String title;

  /**
   * 备注
   */
  @Lob
  @Column(columnDefinition = "TEXT")
  private String remark;

  /**
   * 数据状态
   */
  @JsonDictConvert("DATA_STATUS")
  private Byte status = StatusEnum.OK.getCode();

  /**
   * 角色关联的用户
   */
  @OrderBy("sort")
  @JsonIgnore
  @ManyToMany(
      mappedBy = "roles",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE}
  )
  private Set<User> users = new HashSet<>(0);

  /**
   * 角色关联的功能
   */
  @JsonIgnore
  @JoinTable(
      name = "sys_role_menu",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "menu_id")
  )
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Menu> menus = new HashSet<>(0);

  /**
   * 角色关联部门
   */
  @OrderBy("sort")
  @JsonIgnore
  @JoinTable(
      name = "sys_role_dept",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "dept_id")
  )
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Dept> depts = new HashSet<>(0);
}
