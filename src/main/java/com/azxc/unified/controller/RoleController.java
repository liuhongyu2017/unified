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
 * 角色
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
   * 角色列表页
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
   * 跳转到添加页面
   */
  @RequiresPermissions({"system:role:add"})
  @GetMapping("/add")
  public String toAdd() {
    return "/system/role/add";
  }

  /**
   * 跳转到修改页面
   */
  @RequiresPermissions({"system:role:edit"})
  @GetMapping("/edit")
  public String toEdit(Model model, @RequestParam("id") Role role) {
    model.addAttribute("role", role);
    return "/system/role/add";
  }

  /**
   * 保存/修改 角色
   */
  @ActionLog(key = "角色管理", message = "角色：${title}", action = SaveAction.class)
  @RequiresPermissions({"system:role:add", "system:role:edit"})
  @ResponseBody
  @PostMapping("/save")
  public ResultVo<Object> save(@Validated RoleValid roleValid, @EntityParam Role role) {
    // 不允许操作超级管理员角色
    if (role.getId() != null && Objects.equals(role.getId(), AdminConst.ADMIN_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ID)) {
      throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
    }
    // 判断角色编号是否重复
    if (roleService.repeatByName(role)) {
      throw new ResultException(ResultEnum.ROLE_EXIST);
    }
    // 负责无需修改的数据
    if (role.getId() != null) {
      Role beRole = roleService.getById(role.getId());
      String[] fieIds = {"users", "menus"};
      EntityBeanUtil.copyProperties(beRole, role, fieIds);
    }
    // 保存数据
    roleService.save(role);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 跳转到详情页面
   */
  @RequiresPermissions({"system:role:detail"})
  @GetMapping("/detail")
  public String toDetail(Model model, @RequestParam("id") Role role) {
    model.addAttribute("role", role);
    return "/system/role/detail";
  }

  /**
   * 跳转到拥有该角色的用户列表页面
   */
  @RequiresPermissions({"system:role:userList"})
  @GetMapping("/userList")
  public String toUserList(Model model, @RequestParam("id") Role role) {
    List<User> users = userService.getListByRole(role);

    model.addAttribute("list", users);
    return "/system/role/user_list";
  }

  /**
   * 跳转到授权功能页面
   */
  @RequiresPermissions({"system:role:authMenu"})
  @GetMapping("/authMenu")
  public String toAuthMenu(Model model, @RequestParam(value = "ids") Long id) {
    model.addAttribute("id", id);
    return "/system/role/auth_menu";
  }

  /**
   * 获取权限资源列表
   */
  @RequiresPermissions({"system:role:authMenu"})
  @ResponseBody
  @GetMapping("/authMenuList")
  public ResultVo<Object> authMenuList(@RequestParam(value = "ids") Role role) {
    // 获取指定角色权限资源
    Set<Menu> authMenus = role.getMenus();
    // 获取全部功能列表
    List<Menu> list = menuService.getListBySortOk();
    // 融合两项数据
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
   * 保存授权功能信息
   */
  @ActionLog(key = RoleAction.ROLE_AUTH, action = RoleAction.class)
  @RequiresPermissions({"system:role:authMenu"})
  @ResponseBody
  @PostMapping("/authMenu")
  public ResultVo<Object> authMenu(
      @RequestParam(value = "id") Role role,
      @RequestParam(value = "authId", required = false) HashSet<Menu> menus
  ) {
    // 不允许操作超级管理员数据
    if (Objects.equals(role.getId(), AdminConst.ADMIN_ROLE_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ROLE_ID)) {
      throw new ResultException((ResultEnum.NO_ADMIN_ROLE_AUTH));
    }
    // 更新角色功能
    role.setMenus(menus);
    // 保存数据
    roleService.save(role);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 跳转到授权部门页面
   */
  @RequiresPermissions({"system:role:authDept"})
  @GetMapping("/authDept")
  public String toAuthDept(Model model, @RequestParam(value = "ids") Long id) {
    model.addAttribute("id", id);
    return "/system/role/auth_dept";
  }

  /**
   * 获取权限资源列表
   */
  @RequiresPermissions({"system:role:authDept"})
  @ResponseBody
  @GetMapping("/authDeptList")
  public ResultVo<Object> authDeptList(@RequestParam(value = "ids") Role role) {
    // 获取指定角色权限资源
    Set<Dept> authDepts = role.getDepts();
    // 获取全部功能列表
    List<Dept> list = deptService.getListBySortOk();
    // 融合两项数据
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
   * 保存授权功能信息
   */
  @ActionLog(key = RoleAction.ROLE_AUTH, action = RoleAction.class)
  @RequiresPermissions({"system:role:authDept"})
  @ResponseBody
  @PostMapping("/authDept")
  public ResultVo<Object> authDept(
      @RequestParam(value = "id") Role role,
      @RequestParam(value = "authId", required = false) HashSet<Dept> dept
  ) {
    // 不允许操作超级管理员数据
    if (Objects.equals(role.getId(), AdminConst.ADMIN_ROLE_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ROLE_ID)) {
      throw new ResultException((ResultEnum.NO_ADMIN_ROLE_AUTH));
    }
    // 更新角色功能
    role.setDepts(dept);
    // 保存数据
    roleService.save(role);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 更新状态
   */
  @ActionLog(name = "角色状态", action = StatusAction.class)
  @RequiresPermissions({"system:role:status"})
  @ResponseBody
  @GetMapping("/status/{param}")
  public ResultVo<Object> status(
      @PathVariable("param") String param,
      @RequestParam(value = "ids", required = false) List<Long> ids
  ) {
    // 不能修改超级管理员数据
    if (ids.contains(AdminConst.ADMIN_ROLE_ID)) {
      throw new ResultException(ResultEnum.NO_ADMIN_ROLE_STATUS);
    }
    StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
    if (roleService.updateStatus(statusEnum, ids)) {
      return ResultVoUtil.success(statusEnum.getMessage() + "成功");
    } else {
      return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
    }
  }
}
