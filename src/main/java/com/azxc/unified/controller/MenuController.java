package com.azxc.unified.controller;

import com.azxc.unified.common.actionLog.action.SaveAction;
import com.azxc.unified.common.actionLog.action.base.StatusAction;
import com.azxc.unified.common.actionLog.annotation.ActionLog;
import com.azxc.unified.common.actionLog.annotation.EntityParam;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.common.utils.HttpServletUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.SortUtil;
import com.azxc.unified.common.utils.StatusUtil;
import com.azxc.unified.entity.Menu;
import com.azxc.unified.service.MenuService;
import com.azxc.unified.validator.MenuValid;
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
 * 菜单
 *
 * @author lhy
 * @version 1.0 2020/3/18
 */
@RequestMapping("/system/menu")
@Controller
public class MenuController {

  private final MenuService menuService;

  public MenuController(MenuService menuService) {
    this.menuService = menuService;
  }

  /**
   * 列表页面
   */
  @RequiresPermissions({"system:menu:index"})
  @GetMapping("/index")
  public String index(Model model) {
    String search = HttpServletUtil.getRequest().getQueryString();

    model.addAttribute("search", search);
    return "/system/menu/index";
  }

  /**
   * 菜单数据列表
   */
  @RequiresPermissions({"system:menu:index"})
  @ResponseBody
  @GetMapping("/list")
  public ResultVo<Object> list(Menu menu) {
    ExampleMatcher exampleMatcher =
        ExampleMatcher.matching().withMatcher("title", GenericPropertyMatcher::contains);
    Example<Menu> example = Example.of(menu, exampleMatcher);
    List<Menu> list = menuService.getListByExampleSort(example);

    return ResultVoUtil.success(list);
  }


  /**
   * 跳转到添加菜单
   */
  @RequiresPermissions({"system:menu:add"})
  @GetMapping("/add")
  public String toAdd(Model model, @RequestParam(value = "pid", required = false) Menu menu) {
    model.addAttribute("pMenu", menu);
    return "/system/menu/add";
  }

  /**
   * 跳转到编辑页面
   */
  @RequiresPermissions({"system:menu:edit"})
  @GetMapping("/edit")
  public String toEdit(Model model, @RequestParam("id") Menu menu) {
    Menu pMenu = menuService.getById(menu.getPid());

    model.addAttribute("menu", menu);
    model.addAttribute("pMenu", pMenu);
    return "/system/menu/add";
  }

  /**
   * 跳转到详情页
   */
  @RequiresPermissions({"system:menu:detail"})
  @GetMapping("/detail")
  public String toDetail(Model model, @RequestParam("id") Menu menu) {
    model.addAttribute("menu", menu);
    return "/system/menu/detail";
  }

  /**
   * 获取排序菜单列表
   */
  @RequiresPermissions({"system:menu:add", "system:menu:edit"})
  @ResponseBody
  @GetMapping("/sortList/{pid}/{notId}")
  public Map<Integer, String> sortList(
      @PathVariable(value = "pid", required = false) Long pid,
      @PathVariable(value = "notId", required = false) Long notId
  ) {
    notId = (notId != null) ? notId : 0L;
    List<Menu> levelMenus = menuService.getListByPid(pid, notId);
    Map<Integer, String> sortMap = new TreeMap<>();
    for (int i = 1; i <= levelMenus.size(); i++) {
      sortMap.put(i, levelMenus.get(i - 1).getTitle());
    }
    return sortMap;
  }

  /**
   * 保存添加/修改的数据
   */
  @ActionLog(name = "菜单管理", message = "菜单：${title}", action = SaveAction.class)
  @RequiresPermissions({"system:menu:add", "system:menu:edit"})
  @ResponseBody
  @PostMapping("/save")
  public ResultVo<Object> save(@Validated MenuValid MenuValid, @EntityParam Menu menu) {
    if (menu.getId() == null) {
      // 排序为空添加到最后
      if (menu.getSort() == null) {
        Long sortMax = menuService.getSortMaxByPid(menu.getPid());
        menu.setSort(sortMax != null ? sortMax - 1 : 0);
      }
    }
    // 添加/更新全部上级序号
    SortUtil.sortPids(menu, menuService);
    // 复制保留无需修改的数据
    if (menu.getId() != null) {
      Menu beMenu = menuService.getById(menu.getId());
      EntityBeanUtil.copyProperties(beMenu, menu);
    }
    // 排序菜单
    List<Menu> levelMenus = SortUtil.sortEntity(menu, menuService);
    menuService.save(levelMenus);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 设置多条数据状态
   */
  @ActionLog(name = "菜单状态", action = StatusAction.class)
  @RequiresPermissions({"system:menu:status"})
  @ResponseBody
  @GetMapping("/status/{param}")
  public ResultVo<Object> status(
      @PathVariable("param") String param,
      @RequestParam(value = "ids", required = false) List<Long> ids
  ) {
    StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
    if (menuService.updateStatus(statusEnum, ids)) {
      return ResultVoUtil.success(statusEnum.getMessage() + "成功");
    } else {
      return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
    }
  }
}
