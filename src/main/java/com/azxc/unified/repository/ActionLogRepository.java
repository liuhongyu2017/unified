package com.azxc.unified.repository;

import com.azxc.unified.entity.ActionLog;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 日志
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {

  /**
   * 清空日志表
   */
  @Transactional
  @Modifying
  @Query(value = "truncate table sys_action_log", nativeQuery = true)
  void truncate();
}
