package com.azxc.unified.service;

import com.azxc.unified.entity.ActionLog;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

/**
 * 行为日志
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
public interface ActionLogService {

  /**
   * 保存行为日志
   *
   * @param actionLog 行为日志实体类
   * @return 保存结果
   */
  ActionLog save(ActionLog actionLog);

  /**
   * 获取分页
   *
   * @param example 查询实体
   * @return 查询结果
   */
  Page<ActionLog> getPageList(Example<ActionLog> example);

  /**
   * 清空日志
   */
  void emptyLog();
}
