package com.azxc.unified.controller;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.actionLog.action.SaveAction;
import com.azxc.unified.common.actionLog.action.UserAction;
import com.azxc.unified.common.actionLog.action.base.StatusAction;
import com.azxc.unified.common.actionLog.annotation.ActionLog;
import com.azxc.unified.common.actionLog.annotation.EntityParam;
import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.excel.ExcelUtil;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.exception.ResultException;
import com.azxc.unified.common.properties.ProjectProperties;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.common.utils.SpringContextUtil;
import com.azxc.unified.common.utils.StatusUtil;
import com.azxc.unified.entity.Dept;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.DeptService;
import com.azxc.unified.service.RoleService;
import com.azxc.unified.service.UserService;
import com.azxc.unified.validator.UserValid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户
 *
 * @author lhy
 * @version 1.0 2020/3/13
 */
@RequestMapping("/system/user")
@Controller
public class UserController {

  private final UserService userService;
  private final RoleService roleService;
  private final DeptService deptService;

  public UserController(UserService userService, RoleService roleService, DeptService deptService) {
    this.userService = userService;
    this.roleService = roleService;
    this.deptService = deptService;
  }

  /**
   * 列表首页
   */
  @RequiresPermissions({"system:user:index"})
  @GetMapping("/index")
  public String index(Model model, User user) {

    Page<User> list = userService.getPageList(user);

    model.addAttribute("list", list.getContent());
    model.addAttribute("page", list);
    model.addAttribute("dept", user.getDept());
    return "/system/user/index";
  }

  /**
   * 跳转到添加页面
   */
  @RequiresPermissions({"system:user:add"})
  @GetMapping("/add")
  public String toAdd() {
    return "/system/user/add";
  }

  /**
   * 跳转到编辑页面
   */
  @RequiresPermissions({"system:user:edit"})
  @GetMapping("/edit")
  public String toEdit(Model model, @RequestParam("id") User user) {
    model.addAttribute("user", user);
    return "/system/user/add";
  }

  /**
   * 保存
   *
   * @param userValid 验证对象
   * @param user      实体对象
   */
  @ActionLog(name = "用户管理", message = "用户：${username}", action = SaveAction.class)
  @RequiresPermissions({"system:user:index", "system:user:edit"})
  @ResponseBody
  @PostMapping("/save")
  public ResultVo<Object> save(@Validated UserValid userValid, @EntityParam User user) {
    // 验证数据是否合格
    if (user.getId() == null) {
      // 密码非空验证
      if (StrUtil.isEmpty(user.getPassword()) || StrUtil
          .isEmpty(StrUtil.trim(user.getPassword()))) {
        throw new ResultException(ResultEnum.USER_PWD_NULL);
      }
      // 两个密码是否一致
      if (!StrUtil.equals(user.getPassword(), userValid.getConfirm())) {
        throw new ResultException(ResultEnum.USER_INEQUALITY);
      }
      // 对密码进行加密处理
      String salt = ShiroUtil.getRandomSalt();
      String encrypt = ShiroUtil.encrypt(user.getPassword(), salt);
      user.setPassword(encrypt);
      user.setSalt(salt);
    }
    // 判断用户名是否重复
    if (userService.repeatByUsername(user)) {
      throw new ResultException(ResultEnum.USER_EXIST);
    }
    // 复制保留无需修改的数据
    if (user.getId() != null) {
      if (Objects.equals(user.getId(), AdminConst.ADMIN_ID)
          && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ID)) {
        throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
      }
      User beUser = userService.getById(user.getId());
      String[] fields = {"password", "salt", "picture", "sort"};
      EntityBeanUtil.copyProperties(beUser, user, fields);
    }
    // 获取排序
    if (user.getSort() == null) {
      Long maxSort = userService.getMaxSort(user.getDept());
      if (maxSort != null) {
        user.setSort(maxSort + 1L);
      } else {
        // 查不到最大的排序字典从0开始
        user.setSort(0L);
      }
    }
    // 保存数据
    userService.save(user);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 设置一条或者多条数据的状态
   */
  @ActionLog(name = "用户状态", action = StatusAction.class)
  @RequiresPermissions({"system:user:status"})
  @ResponseBody
  @GetMapping("/status/{param}")
  public ResultVo<Object> updateStatus(@PathVariable("param") String param,
      @RequestParam(value = "ids", required = false) List<Long> ids) {
    // 不允许修改超级管理员数据
    if (ids.contains(AdminConst.ADMIN_ID)) {
      throw new ResultException(ResultEnum.NO_ADMIN_STATUS);
    }
    // 更新状态
    StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
    if (userService.updateStatus(statusEnum, ids)) {
      return ResultVoUtil.success(statusEnum.getMessage() + "成功");
    } else {
      return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
    }
  }

  /**
   * 跳转到角色分配页面
   */
  @RequiresPermissions({"system:user:role"})
  @GetMapping("/role")
  public String toRole(Model model, @RequestParam(value = "ids") User user) {
    // 获取指定用户角色列表
    Set<Role> authRoles = user.getRoles();
    // 获取全部角色列表
    List<Role> list = roleService.getListBySortOk();

    model.addAttribute("id", user.getId());
    model.addAttribute("list", list);
    model.addAttribute("authRoles", authRoles);
    return "/system/user/role";
  }

  /**
   * 保存角色分配信息
   */
  @ActionLog(key = UserAction.EDIT_ROLE, action = UserAction.class)
  @RequiresPermissions({"system:user:role"})
  @ResponseBody
  @PostMapping("/role")
  public ResultVo<Object> auth(
      @RequestParam(value = "id") User user,
      @RequestParam(value = "roleId", required = false) HashSet<Role> roles
  ) {
    // 不允许修改超级管理员数据
    if (Objects.equals(user.getId(), AdminConst.ADMIN_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ID)) {
      throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
    }
    // 更新用户角色
    user.setRoles(roles);
    // 保存数据
    userService.save(user);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 跳转到修改密码页面
   */
  @RequiresPermissions({"system:user:pwd"})
  @GetMapping("/pwd")
  public String toEditPassword(Model model,
      @RequestParam(value = "ids", required = false) List<Long> ids) {
    model.addAttribute("idList", ids);
    return "/system/user/pwd";
  }

  /**
   * 修改密码
   */
  @ActionLog(key = UserAction.EDIT_PWD, action = UserAction.class)
  @RequiresPermissions({"system:user:pwd"})
  @ResponseBody
  @PostMapping("/pwd")
  public ResultVo<Object> editPassword(String password, String confirm,
      @RequestParam(value = "ids", required = false) List<Long> ids,
      @RequestParam(value = "ids", required = false) List<User> users
  ) {
    // 不允许修改超级管理员数据
    if (ids.contains(AdminConst.ADMIN_ID)
        && !Objects.equals(ShiroUtil.getSubject().getId(), AdminConst.ADMIN_ID)) {
      throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
    }
    // 判断密码是否为空
    if (password.isEmpty() || "".equals(password.trim())) {
      throw new ResultException(ResultEnum.USER_PWD_NULL);
    }
    // 判断两次密码是否一致
    if (!password.equals(confirm)) {
      throw new ResultException(ResultEnum.USER_INEQUALITY);
    }
    // 修改密码，对密码进行加密
    users.forEach(user -> {
      String salt = ShiroUtil.getRandomSalt();
      String encrypt = ShiroUtil.encrypt(password, salt);
      user.setPassword(encrypt);
      user.setSalt(salt);
    });
    // 保存数据
    userService.save(users);
    return ResultVoUtil.success("修改成功");
  }

  /**
   * 获取用户头像
   */
  @GetMapping("/picture")
  public void picture(HttpServletResponse response, @RequestParam("p") String p)
      throws IOException {
    String defaultPath = "/images/user-picture.jpg";
    if (!(StrUtil.isEmpty(p) || p.equals(defaultPath))) {
      ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
      String fuPath = properties.getFilePath();
      String spPath = properties.getStaticPath().replace("*", "");
      File file = new File(fuPath + p.replace(spPath, ""));
      if (file.exists()) {
        FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        return;
      }
    }
    Resource resource = new ClassPathResource("static" + defaultPath);
    FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
  }

  /**
   * 跳转到部门选择
   */
  @RequiresPermissions("system:user:sort")
  @GetMapping("/dept")
  public String toDept(Model model) {
    return "/system/user/user_dept";
  }

  /**
   * 获取部门列表
   */
  @RequiresPermissions({"system:user:sort"})
  @ResponseBody
  @GetMapping("/deptList")
  public ResultVo<Object> deptList() {
    List<Dept> list = deptService.getListBySortOk();
    return ResultVoUtil.success(list);
  }

  /**
   * 跳转到排序页面
   */
  @RequiresPermissions("system:user:sort")
  @GetMapping("/sort")
  public String toSort(Model model, @RequestParam("id") Dept dept) {
    List<User> list = userService.getListByDeptSort(dept);
    model.addAttribute("list", list);
    model.addAttribute("dept", dept);
    return "/system/user/sort";
  }

  /**
   * 修改用户排序
   */
  @RequiresPermissions("system:user:sort")
  @ResponseBody
  @PostMapping("/sort")
  public ResultVo<Object> sort(
      @RequestParam("id") Dept dept,
      @RequestParam("ids[]") List<Long> ids
  ) {
    List<User> users = userService.getListByDeptSort(dept);
    if (users.size() != ids.size()) {
      return ResultVoUtil.error("数据有误，请刷新重试");
    }
    // 需要修改排序的用户
    List<User> sUser = new ArrayList<>();
    for (int i = 0; i < ids.size(); i++) {
      Long id = ids.get(i);
      Long sort = (long) i;
      // id相等，排序序号不相等的用户
      User user = users
          .stream().filter(item ->
              Objects.equals(item.getId(), id) && !Objects.equals(item.getSort(), sort))
          .findFirst().orElse(null);
      if (user != null) {
        user.setSort((long) i);
        sUser.add(user);
      }
    }
    // 保存修改
    userService.updateSortById(sUser);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 导出用户数据
   */
  @RequiresPermissions("system:user:export")
  @ResponseBody
  @GetMapping("/export")
  public void exportExcel() {
    ExcelUtil.exportExcel(User.class, userService.getList());
  }
}
