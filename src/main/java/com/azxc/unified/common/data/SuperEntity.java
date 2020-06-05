package com.azxc.unified.common.data;

import com.azxc.unified.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 设置 id 生成方式，添加审计功能
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@ToString(exclude = {"createdBy", "lastModifiedBy"})
@EqualsAndHashCode(callSuper = false, exclude = {"createdBy", "lastModifiedBy"})
@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class SuperEntity extends PersistableEntity implements Serializable {

  /**
   * 创建人
   */
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  @CreatedBy
  @JoinColumn(name = "created_by")
  @ManyToOne(fetch = FetchType.LAZY)
  private User createdBy;

  /**
   * 创建时间
   */
  @CreatedDate
  @JoinColumn(name = "created_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  /**
   * 更新人
   */
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  @LastModifiedBy
  @JoinColumn(name = "last_modified_by")
  @ManyToOne(fetch = FetchType.LAZY)
  private User lastModifiedBy;

  /**
   * 更新时间
   */
  @LastModifiedDate
  @JoinColumn(name = "last_modified_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastModifiedDate;
}
