package com.azxc.unified.entity;

import com.azxc.unified.common.convert.JsonDictConvert;
import com.azxc.unified.common.data.PersistableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

/**
 * 日志
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_action_log")
public class ActionLog extends PersistableEntity {

  /**
   * 日志名称
   */
  private String name;

  /**
   * 日志类型
   */
  @JsonDictConvert("LOG_TYPE")
  private Byte type;

  /**
   * 操作地点ip
   */
  private String ipAddr;

  /**
   * 产生日志的类
   */
  private String clazz;

  /**
   * 产生日志的方法
   */
  private String method;

  /**
   * 产生日志的表
   */
  private String model;

  /**
   * 产生日志的数据
   */
  private Long recordId;

  /**
   * 日志消息
   */
  @Lob
  @Column(columnDefinition = "TEXT")
  private String message;

  /**
   * 记录时间
   */
  @CreatedDate
  private Date createdDate;

  @JsonIgnore
  @JoinColumn(name = "oper_by")
  @ManyToOne(fetch = FetchType.LAZY)
  private User operBy;

  /**
   * 产生日志的用户昵称
   */
  private String operName;

  /**
   * 封装日志对象
   *
   * @param name    日志名称
   * @param message 日志消息
   */
  public ActionLog(String name, String message) {
    this.name = name;
    this.message = message;
  }
}
