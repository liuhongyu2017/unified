package com.azxc.unified;

import com.azxc.unified.service.DeptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UnifiedApplicationTests {

  @Autowired
  private DeptService deptService;

  @Test
  void contextLoads() {

  }
}
