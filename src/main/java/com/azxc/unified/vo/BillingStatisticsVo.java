package com.azxc.unified.vo;

import com.azxc.unified.entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 账单统计视图
 *
 * @author lhy
 * @version 1.0 2020/4/1
 */
@Data
public class BillingStatisticsVo {

  /**
   * 开始时间
   */
  private Date startDate;

  /**
   * 结束时间
   */
  private Date endDate;

  /**
   * 总金额
   */
  private Long totalAmount;

  /**
   * 平均金额
   */
  private Long averageAmount;

  /**
   * 每个人的价格
   */
  private List<Detail> details = new ArrayList<>();

  /**
   * 每个人
   */
  @Data
  public static class Detail {

    /**
     * 消费金额
     */
    private Long amount;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 支出还是收入
     */
    private String type;

    /**
     * 支付或收入的金额
     */
    private Long money;
  }
}
