package com.azxc.unified.common.data;

import com.azxc.unified.common.constant.StatusConst;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author lhy
 * @version 1.0 2020/3/12
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

  /**
   * 根据id和状态条件查询数据
   *
   * @param id     主键ID
   * @param status 状态
   * @return 实体对象
   */
  T findByIdAndStatus(ID id, Byte status);

  /**
   * 根据id批量更新#{#entityName}表数据状态
   *
   * @param status 状态
   * @param id     ID列表
   * @return 更新数量
   */
  @Modifying
  @Transactional
  @Query(
      "update #{#entityName} set status = ?1  where id in ?2 and status <> " + StatusConst.DELETE)
  Integer updateStatus(Byte status, List<ID> id);
}
