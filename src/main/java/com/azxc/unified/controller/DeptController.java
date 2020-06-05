package com.azxc.unified.controller;

import com.azxc.unified.common.actionLog.action.SaveAction;
import com.azxc.unified.common.actionLog.action.base.StatusAction;
import com.azxc.unified.common.actionLog.annotation.ActionLog;
import com.azxc.unified.common.actionLog.annotation.EntityParam;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.excel.ExcelUtil;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.common.utils.HttpServletUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.SortUtil;
import com.azxc.unified.common.utils.StatusUtil;
import com.azxc.unified.entity.Dept;
import com.azxc.unified.service.DeptService;
import com.azxc.unified.service.UserService;
import com.azxc.unified.validator.DeptValid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 部门
 *
 * @author lhy
 * @version 1.0 2020/3/19
 */
@RequestMapping("/system/dept")
@Controller
public class DeptController {

  private final DeptService deptService;
  private final UserService userService;

  public DeptController(DeptService deptService, UserService userService) {
    this.deptService = deptService;
    this.userService = userService;
  }

  /**
   * 首页
   */
  @RequiresPermissions({"system:dept:index"})
  @GetMapping("/index")
  public String index(Model model) {
    String search = HttpServletUtil.getRequest().getQueryString();
    model.addAttribute("search", search);
    return "/system/dept/index";
  }

  /**
   * 部门数据列表
   */
  @RequiresPermissions({"system:dept:index"})
  @ResponseBody
  @GetMapping("/list")
  public ResultVo<Object> list(Dept dept) {
    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withMatcher("title", GenericPropertyMatcher::contains);
    Example<Dept> example = Example.of(dept, exampleMatcher);
    List<Dept> list = deptService.getList(example);
    return ResultVoUtil.success(list);
  }

  /**
   * 跳转到添加页面
   */
  @RequiresPermissions({"system:dept:add"})
  @GetMapping("/add")
  public String toAdd(Model model, @RequestParam(value = "pid", required = false) Dept pDept) {
    model.addAttribute("pDept", pDept);
    return "/system/dept/add";
  }

  /**
   * 跳转到编辑页面
   */
  @RequiresPermissions({"system:dept:edit"})
  @GetMapping("/edit")
  public String toEdit(Model model, @RequestParam("id") Dept dept) {
    Dept pDept = deptService.getById(dept.getPid());
    if (pDept == null) {
      pDept = new Dept();
      pDept.setId((long) 0);
      pDept.setTitle("顶级");
    }

    model.addAttribute("dept", dept);
    model.addAttribute("pDept", pDept);
    return "/system/dept/add";
  }

  /**
   * 获取排序功能列表
   */
  @RequiresPermissions({"system:dept:add", "system:dept:edit"})
  @ResponseBody
  @GetMapping("/sortList/{pid}/{notId}")
  public Map<Integer, String> sortList(
      @PathVariable(value = "pid", required = false) Long pid,
      @PathVariable(value = "notId", required = false) Long notId
  ) {
    notId = (notId != null) ? notId : 0L;
    List<Dept> levelDept = deptService.getListByPid(pid, notId);
    Map<Integer, String> stringMap = new TreeMap<>();
    for (int i = 1; i <= levelDept.size(); i++) {
      stringMap.put(i, levelDept.get(i - 1).getTitle());
    }
    return stringMap;
  }

  /**
   * 保存/修改
   */
  @ActionLog(name = "部门管理", message = "部门：${title}", action = SaveAction.class)
  @RequiresPermissions({"system:dept:add", "system:dept:edit"})
  @ResponseBody
  @PostMapping("/save")
  public ResultVo<Object> save(@Validated DeptValid deptValid, @EntityParam Dept dept) {
    if (dept.getId() == null) {
      // 排序为空时，添加到最后
      if (dept.getSort() == null) {
        Long sortMax = deptService.getSortMax(dept.getPid());
        dept.setSort(sortMax != null ? sortMax - 1 : 0);
      }
    }
    // 添加/更新全部上级序号
    SortUtil.sortPids(dept, deptService);
    // 复制保留无需修改的数据
    if (dept.getId() != null) {
      Dept beDept = deptService.getById(dept.getId());
      EntityBeanUtil.copyProperties(beDept, dept);
    }
    // 排序
    List<Dept> levelDept = SortUtil.sortEntity(dept, deptService);
    // 保存数据
    deptService.save(levelDept);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 跳转到详细页面
   */
  @RequiresPermissions({"system:dept:detail"})
  @GetMapping("/detail")
  public String toDetail(Model model, @RequestParam("id") Dept dept) {
    model.addAttribute("dept", dept);
    return "/system/dept/detail";
  }

  /**
   * 跳转到拥有该部门的用户列表页面
   */
  @RequiresPermissions({"system:dept:userList"})
  @GetMapping("/userList")
  public String toUserList(Model model, @RequestParam("id") Dept dept) {
    model.addAttribute("list", dept.getUsers());
    return "/system/dept/user_list";
  }

  /**
   * 设置一条或者多条数据的状态
   */
  @ActionLog(name = "部门状态", action = StatusAction.class)
  @RequiresPermissions({"system:dept:status"})
  @GetMapping("/status/{param}")
  @ResponseBody
  public ResultVo<Object> status(
      @PathVariable("param") String param,
      @RequestParam(value = "ids", required = false) List<Long> ids
  ) {
    // 更新状态
    StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
    if (deptService.updateStatus(statusEnum, ids)) {
      return ResultVoUtil.success(statusEnum.getMessage() + "成功");
    } else {
      return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
    }
  }

  /**
   * 导出数据
   */
  @RequiresPermissions("system:dept:export")
  @ResponseBody
  @GetMapping("/export")
  public void exportExcel() {
    List<Dept> depts = deptService.getList();
    Map<Long, Dept> keyDept = new HashMap<>();
    List<Dept> levelDept = new ArrayList<>();
    depts.forEach(dept -> keyDept.put(dept.getId(), dept));
    keyDept.forEach((key, dept) -> {
      Dept item = keyDept.get(dept.getPid());
      if (item != null) {
        item.getChildren().add(dept);
      } else {
        levelDept.add(dept);
      }
    });
    ExcelUtil.exportExcel(Dept.class, joinTile(levelDept, 0));
  }

  /**
   * 将树形结构摊平
   *
   * @param depts 部门
   * @param num   计数
   */
  List<Dept> joinTile(List<Dept> depts, int num) {
    List<Dept> result = new ArrayList<>();
    depts.forEach(dept -> {
      if (dept != null) {
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < num; i++) {
          prefix.append("- ");
        }
        dept.setTitle(prefix.toString() + dept.getTitle());
        result.add(dept);
        if (dept.getChildren().size() > 0) {
          result.addAll(this.joinTile(dept.getChildren(), num + 1));
        }
      }
    });
    return result;
  }
}
