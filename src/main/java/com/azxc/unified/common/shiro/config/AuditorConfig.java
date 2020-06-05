package com.azxc.unified.common.shiro.config;

import com.azxc.unified.entity.User;
import java.util.Optional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

/**
 * 审核员自动赋值配置
 *
 * @author lhy
 * @version 1.0 2020/3/21
 */
@Configuration
public class AuditorConfig implements AuditorAware<User> {

  @Override
  public Optional<User> getCurrentAuditor() {
    Subject subject = SecurityUtils.getSubject();
    User user = (User) subject.getPrincipal();
    return Optional.ofNullable(user);
  }
}
