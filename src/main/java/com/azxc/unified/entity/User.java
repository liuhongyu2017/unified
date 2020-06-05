package com.azxc.unified.entity;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.SuperEntity;
import com.azxc.unified.common.excel.annotation.Excel;
import com.azxc.unified.common.utils.StatusUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 用户
 *
 * @author lhy
 * @version 1.0 2020/3/14
 */
@FilterDef(name = "DeptFilter", parameters = {
    @ParamDef(name = "deptId", type = "long")
})
@Filters({
    @Filter(name = "DeptFilter", condition = "dept_id in (:deptId)")
})
@Excel("用户数据")
@ToString(exclude = {"dept", "roles"})
@EqualsAndHashCode(callSuper = true, exclude = {"dept", "roles"})
@SQLDelete(sql = "update sys_user" + StatusUtil.SLICE_DELETE)
@Where(clause = StatusUtil.NOT_DELETE)
@EntityListeners(AuditingEntityListener.class)
@Data
@Entity
@Table(name = "sys_user")
public class User extends SuperEntity {

  /**
   * 用户名
   */
  @Excel("用户名")
  private String username;

  /**
   * 密码
   */
  @JsonIgnore
  private String password;

  /**
   * 加密盐
   */
  @JsonIgnore
  private String salt;

  /**
   * 职务 - 数据字典
   */
  @Excel(value = "职务", dict = "USER_JOB")
  private Byte job;

  /**
   * 别名
   */
  @Excel("用户昵称")
  private String nickname;

  /**
   * 头像
   */
  private String picture;

  /**
   * 性别 - 数据字典
   */
  @Excel(value = "性别", dict = "USER_SEX")
  private Byte sex;

  /**
   * 移动手机号码
   */
  @Excel("移动手机号码")
  private String mobilePhone;

  /**
   * 办公室电话
   */
  @Excel("办公室电话")
  private String officePhone;

  /**
   * 家庭电话
   */
  @Excel("家庭电话")
  private String homePhone;

  /**
   * 虚拟号码
   */
  @Excel("虚拟号码")
  private String virtualNum;

  /**
   * 电子邮箱
   */
  @Excel("电子邮箱")
  private String email;

  /**
   * 排序 - 正序
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
   * 用户关联的部门
   */
  @Excel(value = "备注", joinField = "title")
  @JsonIgnore
  @JoinColumn(
      name = "dept_id"
  )
  @ManyToOne(fetch = FetchType.LAZY)
  private Dept dept;

  @JoinTable(
      name = "sys_user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Role> roles = new HashSet<>(0);

  /**
   * 数据状态 - 数据字典
   */
  @Excel(value = "状态", dict = "DATA_STATUS")
  private Byte status = StatusEnum.OK.getCode();
}
