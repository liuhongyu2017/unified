package com.azxc.unified.controller;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.data.URL;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.exception.ResultException;
import com.azxc.unified.common.utils.EhCacheUtil;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.common.utils.FormUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.common.utils.SpringContextUtil;
import com.azxc.unified.entity.Upload;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.MenuService;
import com.azxc.unified.service.UserService;
import com.azxc.unified.validator.UserValid;
import java.util.Objects;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台框架
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
@RequestMapping("/system/main")
@Controller
public class MainController {

  private final UserService userService;
  private final MenuService menuService;

  public MainController(MenuService menuService, UserService userService) {
    this.menuService = menuService;
    this.userService = userService;
  }

  /**
   * 首页
   */
  @RequiresPermissions({"system:main:index"})
  @GetMapping("/index")
  public String index(Model model) {

    model.addAttribute("treeMenu", menuService.getTreeMenu());
    return "/system/main";
  }

  /**
   * 跳转到个人信息页面
   */
  @RequiresPermissions({"system:main:index"})
  @GetMapping("/userInfo")
  public String toUserInfo(Model model) {
    User user = ShiroUtil.getSubject();

    model.addAttribute("user", user);
    return "/system/main/userInfo";
  }

  /**
   * 修改用户头像
   */
  @RequiresPermissions({"system:main:index"})
  @ResponseBody
  @PostMapping("/userPicture")
  public ResultVo<Object> userPicture(@RequestParam("picture") MultipartFile picture) {
    UploadController uploadController = SpringContextUtil.getBean(UploadController.class);
    ResultVo<Object> imageResult = uploadController.uploadPicture(picture);
    if (Objects.equals(imageResult.getCode(), ResultEnum.SUCCESS.getCode())) {
      User subject = ShiroUtil.getSubject();
      subject.setPicture(((Upload) imageResult.getData()).getPath());
      userService.save(subject);
      return ResultVoUtil.SAVE_SUCCESS;
    } else {
      return imageResult;
    }
  }

  /**
   * 保存修改个人信息
   */
  @RequiresPermissions({"system:main:index"})
  @ResponseBody
  @PostMapping("/userInfo")
  public ResultVo<Object> userInfo(User user) {
    // 复制保留无需修改的数据
    User subUser = ShiroUtil.getSubject();
    String[] ignores = {"id", "username", "password", "salt", "picture", "dept", "roles"};
    EntityBeanUtil.copyPropertiesIgnores(user, subUser, ignores);
    // 验证参数
    FormUtil.validateParameter(UserValid.class, subUser);
    // 保存数据
    userService.save(subUser);
    return ResultVoUtil.success("保存成功", new URL("/system/main/userInfo"));
  }

  /**
   * 跳转到修改密码页面
   */
  @RequiresPermissions({"system:main:index"})
  @GetMapping("/editPwd")
  public String toEditPwd() {
    return "/system/main/editPwd";
  }

  /**
   * 保存修改密码
   */
  @RequiresPermissions({"system:main:index"})
  @ResponseBody
  @PostMapping("/editPwd")
  public ResultVo<Object> editPwd(String original, String password, String confirm) {
    // 判断原来密码是否有误
    User subUser = ShiroUtil.getSubject();
    String oldPwd = ShiroUtil.encrypt(original, subUser.getSalt());
    if (StrUtil.isEmpty(original)
        || StrUtil.isEmpty(StrUtil.trim(original))
        || !StrUtil.equals(oldPwd, subUser.getPassword())) {
      throw new ResultException(ResultEnum.USER_OLD_PWD_ERROR);
    }
    // 判断密码是否为空
    if (StrUtil.isEmpty(password) || StrUtil.isEmpty(StrUtil.trim(password))) {
      throw new ResultException(ResultEnum.USER_PWD_NULL);
    }
    // 判断两次密码是否一致
    if (!StrUtil.equals(password, confirm)) {
      throw new ResultException(ResultEnum.USER_INEQUALITY);
    }
    // 修改密码，对密码进行加密
    String salt = ShiroUtil.getRandomSalt();
    String encrypt = ShiroUtil.encrypt(password, salt);
    subUser.setPassword(encrypt);
    subUser.setSalt(salt);
    // 保存数据
    userService.save(subUser);
    return ResultVoUtil.success("修改成功");
  }
}
