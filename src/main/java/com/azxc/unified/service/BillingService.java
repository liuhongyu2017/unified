package com.azxc.unified.service;

import com.azxc.unified.entity.Billing;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

/**
 * 账单
 *
 * @author lhy
 * @version 1.0 2020/4/1
 */
public interface BillingService {

  /**
   * 分页查询
   *
   * @param example 查询实例
   * @return 查询结果
   */
  Page<Billing> getPage(Example<Billing> example);

  /**
   * 保存
   *
   * @param billing 实体类
   * @return 保存结果
   */
  Billing save(Billing billing);

  /**
   * 根据时间范围查询
   *
   * @param startDate 开始时间
   * @param endDate   结束时间
   * @return 查询结果
   */
  List<Billing> getListByDateBetween(Date startDate, Date endDate);

  /**
   * 删除
   *
   * @param billing 实体类
   */
  void delete(Billing billing);

  /**
   * 根据id查询
   *
   * @param id 主键
   * @return 查询结果
   */
  Billing getById(Long id);
}
