package com.azxc.unified.common.utils;

import com.azxc.unified.entity.Dept;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.DeptService;
import com.azxc.unified.service.RoleService;
import com.azxc.unified.service.UserService;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.BeanUtils;

/**
 * Shiro 工具类
 *
 * @author lhy
 * @version 1.0 2020/3/20
 */
public class ShiroUtil {

  /**
   * 加密算法
   */
  private final static String HASH_ALGORITHM_NAME = EncryptUtil.HASH_ALGORITHM_NAME;

  /**
   * 循环次数
   */
  private final static int HASH_ITERATIONS = EncryptUtil.HASH_ITERATIONS;

  /**
   * 加密处理（64位字符） 备注：采用自定义的密码加密方式，其原理与SimpleHash一致， 为的是在多个模块间可以使用同一套加密方式，方便共用系统用户。
   *
   * @param password 密码
   * @param salt     密码盐
   */
  public static String encrypt(String password, String salt) {
    return EncryptUtil.encrypt(password, salt, HASH_ALGORITHM_NAME, HASH_ITERATIONS);
  }

  /**
   * 获取随机盐值
   */
  public static String getRandomSalt() {
    return EncryptUtil.getRandomSalt();
  }

  /**
   * 获取当前用户对象
   */
  public static User getSubject() {
    User user = (User) SecurityUtils.getSubject().getPrincipal();

    // 初始化延迟加载的部门信息
    if (user != null && !Hibernate.isInitialized(user.getDept())) {
      try {
        Hibernate.initialize(user.getDept());
      } catch (LazyInitializationException e) {
        // 部门数据延迟加载超时，重新查询用户数据（用于更新“记住我”状态登录的数据）
        UserService userService = SpringContextUtil.getBean(UserService.class);
        User reload = userService.getById(user.getId());
        Hibernate.initialize(reload.getDept());
        // 将重载用户数据拷贝到登录用户中
        BeanUtils.copyProperties(reload, user, "roles");
      }
    }
    return user;
  }

  /**
   * 获取当前用户角色列表
   */
  public static Set<Role> getSubjectRoles() {
    User user = (User) SecurityUtils.getSubject().getPrincipal();
    // 如果用户为空，则返回空列表
    if (user == null) {
      user = new User();
    }
    // 判断角色列表是否已缓存
    if (!Hibernate.isInitialized(user.getRoles())) {
      try {
        Hibernate.initialize(user.getRoles());
      } catch (LazyInitializationException e) {
        // 延迟加载超时，重新查询角色列表数据
        RoleService roleService = SpringContextUtil.getBean(RoleService.class);
        user.setRoles(roleService.getUserOkRoleList(user.getId()));
      }
    }
    return user.getRoles();
  }

  public static Set<Dept> getSubjectDepts(Role role) {
    if (!Hibernate.isInitialized(role.getDepts())) {
      try {
        Hibernate.initialize(role.getDepts());
      } catch (LazyInitializationException e) {
        DeptService deptService = SpringContextUtil.getBean(DeptService.class);
        role.setDepts(deptService.getOkListByRole(role.getId()));
      }
    }
    return role.getDepts();
  }

  /**
   * 获取用户IP地址
   */
  public static String getIp() {
    HttpServletRequest request = HttpServletUtil.getRequest();
    // 反向代理时获取真实ip
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Forwarded-For");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}