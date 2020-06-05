package com.azxc.unified.common.data;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.utils.HttpServletUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 分页排序
 *
 * @author lhy
 * @version 1.0 2020/3/16
 */
public class PageSort {

  /**
   * 默认每页10条
   */
  private static final Integer PAGE_SIZE_DEF = 10;
  /**
   * 默认按时间倒序排序
   */
  private static final String ORDER_BY_COLUMN_DEF = "createdDate";
  private static final Sort.Direction SORT_DIRECTION = Sort.Direction.DESC;

  private PageSort() {

  }

  /**
   * 创建分页排序对象
   *
   * @return 分页排序bean
   */
  public static PageRequest pageRequest() {
    return pageRequest(PAGE_SIZE_DEF, SORT_DIRECTION, ORDER_BY_COLUMN_DEF);
  }

  /**
   * 创建分页排序对象
   *
   * @param sortDirection 排序方式默认值
   * @return 分页排序bean
   */
  public static PageRequest pageRequest(Sort.Direction sortDirection) {
    return pageRequest(PAGE_SIZE_DEF, sortDirection, ORDER_BY_COLUMN_DEF);
  }

  /**
   * 创建分页排序对象
   *
   * @param sortDirection    排序方式默认值
   * @param orderByColumnDef 排序字段名称默认值
   * @return 分页排序bean
   */
  public static PageRequest pageRequest(Sort.Direction sortDirection, String... orderByColumnDef) {
    return pageRequest(PAGE_SIZE_DEF, sortDirection, orderByColumnDef);
  }

  /**
   * 创建分页排序对象
   *
   * @param pageSizeDef      分页数据数量默认值
   * @param sortDirection    排序方式默认值
   * @param orderByColumnDef 排序字段名称默认值
   * @return 分页排序bean
   */
  private static PageRequest pageRequest(Integer pageSizeDef, Sort.Direction sortDirection,
      String... orderByColumnDef) {
    Integer pageIndex = HttpServletUtil.getParameterInt("page", 1);
    Integer pageSize = HttpServletUtil.getParameterInt("size", pageSizeDef);
    String orderByColumn = HttpServletUtil.getParameter("orderByColumn");
    String direction = HttpServletUtil.getParameter("isAsc", sortDirection.toString());
    // 参数带排序条件，用请求参数的排序条件进行排序
    Sort sort = Sort.by(Sort.Direction.fromString(direction), orderByColumnDef);
    if (StrUtil.isNotEmpty(orderByColumn)) {
      sort = Sort.by(Sort.Direction.fromString(direction), orderByColumn);
    }
    return PageRequest.of(pageIndex - 1, pageSize, sort);
  }
}
