package com.azxc.unified.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.entity.Billing;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.BillingService;
import com.azxc.unified.service.UserService;
import com.azxc.unified.validator.BillingValid;
import com.azxc.unified.vo.BillingStatisticsVo;
import com.azxc.unified.vo.BillingStatisticsVo.Detail;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 账单
 *
 * @author lhy
 * @version 1.0 2020/4/1
 */
@RequestMapping("/module/billing")
@Controller
public class BillingController {

  private final BillingService billingService;
  private final UserService userService;

  public BillingController(BillingService billingService, UserService userService) {
    this.billingService = billingService;
    this.userService = userService;
  }

  /**
   * 账单表格
   */
  @GetMapping("/index")
  public String index(Model model, Billing billing) {
    ExampleMatcher exampleMatcher = ExampleMatcher.matching();
    Example<Billing> example = Example.of(billing, exampleMatcher);
    Page<Billing> page = billingService.getPage(example);

    model.addAttribute("list", page.getContent());
    model.addAttribute("page", page);
    return "/module/billing/index";
  }

  /**
   * 跳转到添加页面
   */
  @GetMapping("/add")
  public String toAdd() {
    return "/module/billing/add";
  }

  /**
   * 跳转到编辑页面
   */
  @GetMapping("/edit")
  public String toEdit(Model model, @RequestParam("id") Billing billing) {
    model.addAttribute("billing", billing);
    return "/module/billing/add";
  }

  /**
   * 保存或修改
   */
  @ResponseBody
  @PostMapping("/save")
  public ResultVo<Object> save(@Validated BillingValid billingValid, Billing billing) {
    // 金额 *100 转long类型存入数据库
    BigDecimal money = new BigDecimal(billingValid.getVAmount()).multiply(new BigDecimal(100));
    billing.setAmount(money.longValue());
    Date data = DateUtil.parseDate(billingValid.getVDate());
    billing.setDate(data);
    // 复制保留无需修改的数据
    if (billing.getId() != null) {
      Billing beBilling = billingService.getById(billing.getId());
      EntityBeanUtil.copyProperties(beBilling, billing);
    }
    billingService.save(billing);
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 跳转到统计页面
   */
  @GetMapping("/statistics")
  public String toStatistics(
      Model model,
      @RequestParam(value = "start", required = false) String start,
      @RequestParam(value = "end", required = false) String end,
      @RequestParam(value = "userIds", required = false) List<Long> userIds
  ) {
    BillingStatisticsVo billingStatisticsVo = new BillingStatisticsVo();
    if (StrUtil.isNotEmpty(start) && StrUtil.isNotEmpty(end) && userIds != null
        && userIds.size() > 0) {
      Date startDate = DateUtil.parseDate(start);
      Date endDate = DateUtil.parseDate(end);
      List<Billing> billings = billingService.getListByDateBetween(startDate, endDate);
      // 按人员分组
      Map<User, List<Billing>> levelBilling = new HashMap<>();
      for (Long userId : userIds) {
        User user = userService.getById(userId);
        if (user == null) {
          continue;
        }
        List<Billing> items = billings.stream()
            .filter(billing -> Objects.equals(billing.getCreatedBy().getId(), userId))
            .collect(Collectors.toList());
        levelBilling.put(user, items);
      }
      // 统计总价格
      BigDecimal totalAmount = new BigDecimal(0);
      for (Billing billing : billings) {
        totalAmount = totalAmount.add(new BigDecimal(billing.getAmount()));
      }
      // 平均价格
      BigDecimal averageAmount =
          totalAmount.divide(new BigDecimal(levelBilling.size()), RoundingMode.HALF_DOWN);
      // 统计每个人
      levelBilling.forEach((key, billing) -> {
        BigDecimal amount = new BigDecimal(0);
        for (Billing item : billing) {
          amount = amount.add(new BigDecimal(item.getAmount()));
        }
        BillingStatisticsVo.Detail detail = new Detail();
        detail.setAmount(amount.longValue());
        detail.setUser(key);
        BigDecimal money = amount.subtract(averageAmount);
        detail.setMoney(money.longValue());
        billingStatisticsVo.getDetails().add(detail);
      });
      billingStatisticsVo.setStartDate(startDate);
      billingStatisticsVo.setEndDate(endDate);
      billingStatisticsVo.setTotalAmount(totalAmount.longValue());
      billingStatisticsVo.setAverageAmount(averageAmount.longValue());
    }

    model.addAttribute("users", userService.getListOk(AdminConst.ADMIN_ID));
    model.addAttribute("statistics", billingStatisticsVo);
    return "/module/billing/statistics";
  }

  /**
   * 删除
   */
  @ResponseBody
  @GetMapping("/delete")
  public ResultVo<Object> delete(@RequestParam("id") Billing billing) {
    billingService.delete(billing);
    return ResultVoUtil.success("删除成功");
  }
}
