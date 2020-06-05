package com.azxc.unified.entity;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.convert.JsonDictConvert;
import com.azxc.unified.common.data.ParentEntity;
import com.azxc.unified.common.data.SuperEntity;
import com.azxc.unified.common.excel.annotation.Excel;
import com.azxc.unified.common.utils.StatusUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

/**
 * 部门
 *
 * @author lhy
 * @version 1.0 2020/3/19
 */
@FilterDef(name = "DeptFilter", parameters = {
    @ParamDef(name = "deptId", type = "long")
})
@Filters({
    @Filter(name = "DeptFilter", condition = "id in (:deptId)")
})
@Excel("部门")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@ToString(exclude = {"users", "roles"})
@EqualsAndHashCode(callSuper = true, exclude = {"users", "roles"})
@Where(clause = StatusUtil.NOT_DELETE)
@Data
@Entity
@Table(name = "sys_dept")
public class Dept extends SuperEntity implements ParentEntity {

  /**
   * 部门名称
   */
  @Excel("部门名称")
  private String title;

  /**
   * 父级编号
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long pid;

  /**
   * 所有父级编号
   */
  private String pids;

  /**
   * 排序
   */
  private Long sort;

  /**
   * 备注
   */
  @Excel("备注")
  @Lob
  @Column(columnDefinition = "TEXT")
  private String remark;

  /**
   * 部门下的用户
   */
  @OrderBy("sort")
  @JsonIgnore
  @OneToMany(mappedBy = "dept")
  private List<User> users = new ArrayList<>(0);

  /**
   * 部门绑定的角色
   */
  @JsonIgnore
  @ManyToMany(mappedBy = "depts")
  private Set<Role> roles = new HashSet<>(0);

  /**
   * 数据状态
   */
  @Excel(value = "部门状态", dict = "DATA_STATUS")
  @JsonDictConvert("DATA_STATUS")
  private Byte status = StatusEnum.OK.getCode();

  /**
   * 子部门
   */
  @JsonIgnore
  @Transient
  private List<Dept> children = new ArrayList<>();

  public void setPids(String pids) {
    if (StrUtil.startWith(pids, ",")) {
      pids = pids.substring(1);
    }
    this.pids = pids;
  }
}
