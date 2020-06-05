package com.azxc.unified.common.utils;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

/**
 * 分页工具类
 *
 * @author lhy
 * @version 1.0 2020/3/13
 */
public class PageUtil {

  private String paramHref;

  /**
   * 初始化分页
   */
  public boolean pageInit(Page<?> page) {
    if (page.getTotalElements() > page.getSize() && page.getNumberOfElements() != 0) {
      HttpServletRequest request = HttpServletUtil.getRequest();
      // 获取分页参数地址
      String servletPath = request.getServletPath();
      StringBuilder param = new StringBuilder(servletPath + "?");
      Enumeration<?> em = request.getParameterNames();
      while (em.hasMoreElements()) {
        String name = (String) em.nextElement();
        if (!"page".equals(name)) {
          String value = request.getParameter(name);
          param.append(name).append("=").append(value).append("&");
        }
      }
      this.paramHref = param.toString();
      return true;
    }
    return false;
  }

  /**
   * 处理页码
   */
  public List<String> pageCode(Page<?> page) {
    int number = page.getNumber() + 1;
    int totalPages = page.getTotalPages();
    int start = 0;
    int length = 7;
    int half = length / 2 + 1;
    List<String> codeList = new ArrayList<>();

    if (totalPages > length && number > half) {
      start = number - half;
    }
    if (totalPages > length && number > totalPages - half) {
      start = totalPages - length;
    }
    for (int i = 1; i <= (Math.min(totalPages, length)); i++) {
      codeList.add(String.valueOf(i + start));
    }
    if (totalPages > length && number > half) {
      codeList.set(0, "1");
      codeList.set(1, "…");
    }
    if (totalPages > length && number < totalPages - (half - 1)) {
      codeList.set(length - 2, "…");
      codeList.set(length - 1, String.valueOf(totalPages));
    }
    return codeList;
  }

  /**
   * 是否是当前页
   */
  public String pageActive(Page<?> page, String pageCode, String className) {
    int number = page.getNumber();
    if (!"…".equals(pageCode)) {
      if (number == Integer.parseInt(pageCode) - 1) {
        return " " + className;
      }
    }
    return "";
  }

  /**
   * 是否有上一页
   */
  public boolean isPrevious(Page<?> page) {
    return page.getNumber() != 0;
  }

  /**
   * 是否有下一页
   */
  public boolean isNext(Page<?> page) {
    return page.getNumber() != page.getTotalPages() - 1;
  }

  /**
   * 是否显示 ...
   */
  public boolean isCode(String pageCode) {
    return "…".equals(pageCode);
  }

  /**
   * 根据页码拼接分页地址
   */
  public String pageHref(String pageCode) {
    return paramHref + "page=" + pageCode;
  }

}
