package com.azxc.unified.config.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * 使用 jpa 的 OpenEntityManagerInViewFilter
 *
 * @author lhy
 * @version 1.0 2020/3/27
 */
@Order(1)
@WebFilter(filterName = "openSessionFilter", urlPatterns = "/*")
public class OpenSessionFilter implements Filter {

  private final OpenEntityManagerInViewFilter filter;

  public OpenSessionFilter() {
    filter = new OpenEntityManagerInViewFilter();
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    filter.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    filter.doFilter(request, response, chain);
  }

  @Override
  public void destroy() {
    filter.destroy();
  }
}
