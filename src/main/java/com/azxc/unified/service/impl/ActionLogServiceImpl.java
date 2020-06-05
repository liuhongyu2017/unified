package com.azxc.unified.service.impl;

import com.azxc.unified.common.data.PageSort;
import com.azxc.unified.entity.ActionLog;
import com.azxc.unified.repository.ActionLogRepository;
import com.azxc.unified.service.ActionLogService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Service
public class ActionLogServiceImpl implements ActionLogService {

  private final ActionLogRepository actionLogRepository;

  public ActionLogServiceImpl(ActionLogRepository actionLogRepository) {
    this.actionLogRepository = actionLogRepository;
  }

  @Override
  public ActionLog save(ActionLog actionLog) {
    return actionLogRepository.save(actionLog);
  }

  @Override
  public Page<ActionLog> getPageList(Example<ActionLog> example) {
    PageRequest page = PageSort.pageRequest();
    return actionLogRepository.findAll(example, page);
  }

  @Override
  public void emptyLog() {
    actionLogRepository.truncate();
  }
}
