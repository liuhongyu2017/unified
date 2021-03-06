package com.azxc.unified.controller;

import com.azxc.unified.common.actionLog.action.RoleAction;
import com.azxc.unified.common.actionLog.action.SaveAction;
import com.azxc.unified.common.actionLog.action.base.StatusAction;
import com.azxc.unified.common.actionLog.annotation.ActionLog;
import com.azxc.unified.common.actionLog.annotation.EntityParam;
import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.exception.ResultException;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.common.utils.StatusUtil;
import com.azxc.unified.entity.Dept;
import com.azxc.unified.entity.Menu;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.DeptService;
import com.azxc.unified.service.MenuService;
import com.azxc.unified.service.RoleService;
import com.azxc.unified.service.UserService;
import com.azxc.unified.validator.RoleValid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
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
 * ??????
 *
 * @author lhy
 * @version 1.0 2020/3/19
 */
@RequestMapping("/system/role")
@Controller
public class RoleController {

  private final RoleService roleService;
  private final MenuService menuService;
  private final UserService userService;
  private final DeptService deptService;

  public RoleController(RoleService roleService, MenuService menuService, UserService userService,
      DeptService deptService) {
    this.roleService = roleService;
    this.menuService = menuService;
    this.userService = userService;
    this.deptService = deptService;
  }

  /**
   * ???????????????
   */
  @RequiresPermissions({"system:role:index"})
  @GetMapping("/index")
  public String index(Model model, Role role) {
    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withMatcher("title", GenericPropertyMatcher::contains);

    Example<Role> example = Example.of(role, exampleMatcher);
    Page<Role> list = roleService.getPageList(example);

    model.addAttribute("list", list.getContent());
    model.addAttribute("page", list);
    return "/system/role/index";
  }

  /**
   * ?????????????????????
   */
  @RequiresPermissions({"system:role:add"})
  @GetMapping("/add")
  public String toAdd() {
    return "/system/role/add";
  }

  /**
   * ?????????????????????
   */
  @RequiresPermissions({"system:role:edit"})
  @GetMapping("/edit")
  public String toEdit(Model model, @RequestParam("id") Role role) {
    model.addAttribute("role", role);
    return "/system/role/add";
  }

  /**
   * ??????/?????? ??????
   */
  @ActionLog(key = "????????????", message = "?????????${title}", action = SaveAction.class)
  @RequiresPermissions({"system:role:add", "system:role:edit"})
  @ResponseBody
  @PostMapping("/save")
  public ResultVo<Object> save(@Validated RoleValid roleValid, @EntityParam Role role) {
    // ????????????????????????????????????
    if (role.getId() != null && Objects.equals(role.getId(), AdminConst.ADMIN_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ID)) {
      throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
    }
    // ??????????????????????????????
    if (roleService.repeatByName(role)) {
      throw new ResultException(ResultEnum.ROLE_EXIST);
    }
    // ???????????????????????????
    if (role.getId() != null) {
      Role beRole = roleService.getById(role.getId());
      String[] fieIds = {"users", "menus"};
      EntityBeanUtil.copyProperties(beRole, role, fieIds);
    }
    // ????????????
    roleService.save(role);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * ?????????????????????
   */
  @RequiresPermissions({"system:role:detail"})
  @GetMapping("/detail")
  public String toDetail(Model model, @RequestParam("id") Role role) {
    model.addAttribute("role", role);
    return "/system/role/detail";
  }

  /**
   * ?????????????????????????????????????????????
   */
  @RequiresPermissions({"system:role:userList"})
  @GetMapping("/userList")
  public String toUserList(Model model, @RequestParam("id") Role role) {
    List<User> users = userService.getListByRole(role);

    model.addAttribute("list", users);
    return "/system/role/user_list";
  }

  /**
   * ???????????????????????????
   */
  @RequiresPermissions({"system:role:authMenu"})
  @GetMapping("/authMenu")
  public String toAuthMenu(Model model, @RequestParam(value = "ids") Long id) {
    model.addAttribute("id", id);
    return "/system/role/auth_menu";
  }

  /**
   * ????????????????????????
   */
  @RequiresPermissions({"system:role:authMenu"})
  @ResponseBody
  @GetMapping("/authMenuList")
  public ResultVo<Object> authMenuList(@RequestParam(value = "ids") Role role) {
    // ??????????????????????????????
    Set<Menu> authMenus = role.getMenus();
    // ????????????????????????
    List<Menu> list = menuService.getListBySortOk();
    // ??????????????????
    list.forEach(menu -> {
      if (authMenus.contains(menu)) {
        menu.setRemark("auth:true");
      } else {
        menu.setRemark("");
      }
    });
    return ResultVoUtil.success(list);
  }

  /**
   * ????????????????????????
   */
  @ActionLog(key = RoleAction.ROLE_AUTH, action = RoleAction.class)
  @RequiresPermissions({"system:role:authMenu"})
  @ResponseBody
  @PostMapping("/authMenu")
  public ResultVo<Object> authMenu(
      @RequestParam(value = "id") Role role,
      @RequestParam(value = "authId", required = false) HashSet<Menu> menus
  ) {
    // ????????????????????????????????????
    if (Objects.equals(role.getId(), AdminConst.ADMIN_ROLE_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ROLE_ID)) {
      throw new ResultException((ResultEnum.NO_ADMIN_ROLE_AUTH));
    }
    // ??????????????????
    role.setMenus(menus);
    // ????????????
    roleService.save(role);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * ???????????????????????????
   */
  @RequiresPermissions({"system:role:authDept"})
  @GetMapping("/authDept")
  public String toAuthDept(Model model, @RequestParam(value = "ids") Long id) {
    model.addAttribute("id", id);
    return "/system/role/auth_dept";
  }

  /**
   * ????????????????????????
   */
  @RequiresPermissions({"system:role:authDept"})
  @ResponseBody
  @GetMapping("/authDeptList")
  public ResultVo<Object> authDeptList(@RequestParam(value = "ids") Role role) {
    // ??????????????????????????????
    Set<Dept> authDepts = role.getDepts();
    // ????????????????????????
    List<Dept> list = deptService.getListBySortOk();
    // ??????????????????
    list.forEach(dept -> {
      if (authDepts.contains(dept)) {
        dept.setRemark("auth:true");
      } else {
        dept.setRemark("");
      }
    });
    return ResultVoUtil.success(list);
  }

  /**
   * ????????????????????????
   */
  @ActionLog(key = RoleAction.ROLE_AUTH, action = RoleAction.class)
  @RequiresPermissions({"system:role:authDept"})
  @ResponseBody
  @PostMapping("/authDept")
  public ResultVo<Object> authDept(
      @RequestParam(value = "id") Role role,
      @RequestParam(value = "authId", required = false) HashSet<Dept> dept
  ) {
    // ????????????????????????????????????
    if (Objects.equals(role.getId(), AdminConst.ADMIN_ROLE_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ROLE_ID)) {
      throw new ResultException((ResultEnum.NO_ADMIN_ROLE_AUTH));
    }
    // ??????????????????
    role.setDepts(dept);
    // ????????????
    roleService.save(role);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * ????????????
   */
  @ActionLog(name = "????????????", action = StatusAction.class)
  @RequiresPermissions({"system:role:status"})
  @ResponseBody
  @GetMapping("/status/{param}")
  public ResultVo<Object> status(
      @PathVariable("param") String param,
      @RequestParam(value = "ids", required = false) List<Long> ids
  ) {
    // ?????????????????????????????????
    if (ids.contains(AdminConst.ADMIN_ROLE_ID)) {
      throw new ResultException(ResultEnum.NO_ADMIN_ROLE_STATUS);
    }
    StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
    if (roleService.updateStatus(statusEnum, ids)) {
      return ResultVoUtil.success(statusEnum.getMessage() + "??????");
    } else {
      return ResultVoUtil.error(statusEnum.getMessage() + "????????????????????????");
    }
  }
}
