package com.azxc.unified.common.actionLog.exception;

import com.azxc.unified.common.exception.advice.ResultExceptionAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将异常切入程序添加到异常通知器中
 *
 * @author lhy
 * @version 1.0 2020/3/25
 */
@Configuration
public class ActionLogProceedAdviceConfig {

  @Bean
  public ActionLogProceedAdvice actionLogProceedAdvice(ResultExceptionAdvice advice) {
    ActionLogProceedAdvice authorization = new ActionLogProceedAdvice();
    advice.putProceed(authorization);
    return authorization;
  }
}
