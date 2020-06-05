package com.azxc.unified.entity;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.convert.JsonDictConvert;
import com.azxc.unified.common.data.ParentEntity;
import com.azxc.unified.common.data.SortEntity;
import com.azxc.unified.common.data.SuperEntity;
import com.azxc.unified.common.utils.StatusUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

/**
 * 菜单
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
@ToString(exclude = {"roles"})
@EqualsAndHashCode(callSuper = true, exclude = {"roles"})
@Where(clause = StatusUtil.NOT_DELETE)
@Data
@Entity
@Table(name = "sys_menu")
public class Menu extends SuperEntity implements ParentEntity {

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
   * 功能名称
   */
  private String title;

  /**
   * 功能地址
   */
  private String url;

  /**
   * 权限标识符
   */
  private String perms;

  /**
   * 功能图标
   */
  private String icon;

  /**
   * 功能类型
   */
  @JsonDictConvert("FUN_TYPE")
  private Byte type;

  /**
   * 排序
   */
  private Long sort;

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
   * 功能关联的角色
   */
  @JsonIgnore
  @ManyToMany(mappedBy = "menus")
  private Set<Role> roles = new HashSet<>(0);

  /**
   * 子菜单
   */
  @JsonIgnore
  @Transient
  private Map<String, Menu> children = new HashMap<>();

  public void setPids(String pids) {
    if (StrUtil.startWith(pids, ",")) {
      pids = pids.substring(1);
    }
    this.pids = pids;
  }
}
