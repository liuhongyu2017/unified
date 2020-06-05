package com.azxc.unified.controller;

import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.entity.ActionLog;
import com.azxc.unified.service.ActionLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 日志
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
@RequestMapping("/system/actionLog")
@Controller
public class ActionLogController {

  private final ActionLogService actionLogService;

  public ActionLogController(ActionLogService actionLogService) {
    this.actionLogService = actionLogService;
  }

  /**
   * 列表页面
   */
  @RequiresPermissions("system:actionLog:index")
  @GetMapping("/index")
  public String index(Model model, ActionLog actionLog) {
    ExampleMatcher matcher = ExampleMatcher.matching();

    Example<ActionLog> example = Example.of(actionLog, matcher);
    Page<ActionLog> list = actionLogService.getPageList(example);

    model.addAttribute("list", list.getContent());
    model.addAttribute("page", list);
    return "/system/actionLog/index";
  }

  /**
   * 跳转到详情页面
   */
  @RequiresPermissions("system:actionLog:detail")
  @GetMapping("/detail")
  public String toDetail(Model model, @RequestParam("id") ActionLog actionLog) {
    model.addAttribute("actionLog", actionLog);
    return "/system/actionLog/detail";
  }

  /**
   * 清空日志
   */
  @RequiresPermissions("system:actionLog:emptyLog")
  @ResponseBody
  @GetMapping("/emptyLog")
  public ResultVo<Object> emptyLog() {
    actionLogService.emptyLog();
    return ResultVoUtil.success("日志清空成功");
  }
}
