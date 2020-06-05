package com.azxc.unified;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 启动类
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
@ServletComponentScan
@SpringBootApplication
public class UnifiedApplication {

  public static void main(String[] args) {
    SpringApplication.run(UnifiedApplication.class, args);
  }
}
