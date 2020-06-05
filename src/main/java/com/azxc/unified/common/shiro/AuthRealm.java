package com.azxc.unified.common.shiro;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.entity.Role;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.UserService;
import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * 身份验证器
 *
 * @author lhy
 * @version 1.0 2020/3/21
 */
@Component
public class AuthRealm extends AuthorizingRealm {

  private final UserService userService;

  public AuthRealm(UserService userService) {
    this.userService = userService;
  }

  /**
   * 授权逻辑
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    // 获取用户Principal对象
    User user = (User) principal.getPrimaryPrincipal();
    // 超级管理员拥有所有权限
    if (Objects.equals(user.getId(), AdminConst.ADMIN_ID)) {
      info.addRole(AdminConst.ADMIN_ROLE_NAME);
      info.addStringPermission("*:*:*");
      return info;
    }
    // 赋予角色和资源授权
    Set<Role> roles = ShiroUtil.getSubjectRoles();
    roles.forEach(role -> {
      info.addRole(role.getName());
      role.getMenus().forEach(menu -> {
        String perms = menu.getPerms();
        if (StatusEnum.OK.getCode().equals(menu.getStatus())
            && !StrUtil.isEmpty(perms) && !perms.contains("*")) {
          info.addStringPermission(perms);
        }
      });
    });
    return info;
  }

  /**
   * 认证逻辑
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    // 获取数据库中的用户名密码
    User user = userService.getByName(token.getUsername());
    // 判断用户名是否存在
    if (user == null) {
      throw new UnknownAccountException();
    } else if (user.getStatus().equals(StatusEnum.FREEZED.getCode())) {
      throw new LockedAccountException();
    }
    // 对盐进行加密处理
    ByteSource salt = ByteSource.Util.bytes(user.getSalt());
    /* 传入密码自动判断是否正确
     * 参数1：传入对象给Principal
     * 参数2：正确的用户密码
     * 参数3：加盐处理
     * 参数4：固定写法
     */
    return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
  }

  /**
   * 自定义密码验证匹配器
   */
  @PostConstruct
  public void initCredentialsMatcher() {
    setCredentialsMatcher(new SimpleCredentialsMatcher() {
      @Override
      public boolean doCredentialsMatch(AuthenticationToken authenticationToken,
          AuthenticationInfo authenticationInfo) {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        SimpleAuthenticationInfo info = (SimpleAuthenticationInfo) authenticationInfo;
        // 获取明文密码及密码盐
        String password = String.valueOf(token.getPassword());
        String salt = CodecSupport.toString(info.getCredentialsSalt().getBytes());
        return equals(ShiroUtil.encrypt(password, salt), info.getCredentials());
      }
    });
  }
}
