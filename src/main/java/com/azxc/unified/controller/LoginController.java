package com.azxc.unified.controller;

import com.azxc.unified.common.actionLog.action.UserAction;
import com.azxc.unified.common.actionLog.annotation.ActionLog;
import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.data.URL;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.exception.ResultException;
import com.azxc.unified.common.properties.ProjectProperties;
import com.azxc.unified.common.utils.EhCacheUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.common.utils.SpringContextUtil;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.RoleService;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lhy
 * @version 1.0 2020/3/21
 */
@Controller
public class LoginController implements ErrorController {

  private final RoleService roleService;

  public LoginController(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * 跳转到登录页面
   */
  @GetMapping("/login")
  public String toLogin(Model model) {
    ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
    return "/system/login";
  }

  /**
   * 实现登录
   */
  @ActionLog(key = UserAction.USER_LOGIN, action = UserAction.class)
  @ResponseBody
  @PostMapping("/login")
  public ResultVo<Object> login(String username, String password, String captcha,
      String rememberMe) {
    // 判断账号密码是否为空
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      throw new ResultException(ResultEnum.USER_NAME_PWD_NULL);
    }
    // 1.获取Subject主体对象
    Subject subject = SecurityUtils.getSubject();
    // 2.封装用户数据
    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
    // 3.执行登录，进入自定义Realm类中
    try {
      // 判断是否自动登录
      token.setRememberMe(rememberMe != null);
      subject.login(token);
      // 判断是否拥有后台角色 - 超级管理员跳过检查
      User user = ShiroUtil.getSubject();
      if (Objects.equals(user.getId(), AdminConst.ADMIN_ID)
          || roleService.existsUserOk(user.getId())) {
        return ResultVoUtil.success("登录成功", new URL("/system/main/index"));
      } else {
        SecurityUtils.getSubject().logout();
        return ResultVoUtil.error("您不是后台管理员！");
      }
    } catch (LockedAccountException e) {
      return ResultVoUtil.error("该账号已被冻结");
    } catch (AuthenticationException e) {
      return ResultVoUtil.error("用户名或密码错误");
    }
  }

  /**
   * 退出登录
   */
  @GetMapping("/logout")
  public String logout() {
    EhCacheUtil.getMenuCache().clear();
    SecurityUtils.getSubject().logout();
    return "redirect:/login";
  }

  /**
   * 自定义错误页面
   */
  @Override
  public String getErrorPath() {
    return "/error";
  }

  /**
   * 处理错误页面
   */
  @RequestMapping("/error")
  public String handleError(Model model, HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String errorMsg = "好像出错了呢！";
    if (statusCode == 404) {
      errorMsg = "页面找不到了！好像是去火星了~";
    }
    model.addAttribute("statusCode", statusCode);
    model.addAttribute("msg", errorMsg);
    return "/system/main/error";
  }
}
