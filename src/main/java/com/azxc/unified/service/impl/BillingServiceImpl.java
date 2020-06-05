package com.azxc.unified.service.impl;

import com.azxc.unified.common.data.PageSort;
import com.azxc.unified.entity.Billing;
import com.azxc.unified.repository.BillingRepository;
import com.azxc.unified.service.BillingService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/4/1
 */
@Service
public class BillingServiceImpl implements BillingService {

  private final BillingRepository billingRepository;

  public BillingServiceImpl(
      BillingRepository billingRepository) {
    this.billingRepository = billingRepository;
  }

  @Override
  public Page<Billing> getPage(Example<Billing> example) {
    PageRequest pageRequest = PageSort.pageRequest(Direction.DESC, "date");
    return billingRepository.findAll(example, pageRequest);
  }

  @Override
  public Billing save(Billing billing) {
    return billingRepository.save(billing);
  }

  @Override
  public List<Billing> getListByDateBetween(Date startDate, Date endDate) {
    Sort sort = Sort.by(Direction.DESC, "date");
    return billingRepository.findAll(
        (Specification<Billing>) (root, query, cb) -> {
          List<Predicate> preList = new ArrayList<>();
          if (startDate != null) {
            preList.add(cb.greaterThanOrEqualTo(root.get("date").as(Date.class), startDate));
          }
          if (endDate != null) {
            preList.add(cb.lessThanOrEqualTo(root.get("date").as(Date.class), endDate));
          }
          Predicate[] pres = new Predicate[preList.size()];
          return query.where(preList.toArray(pres)).getRestriction();
        }, sort);
  }

  @Override
  public void delete(Billing billing) {
    billingRepository.delete(billing);
  }

  @Override
  public Billing getById(Long id) {
    return billingRepository.findById(id).orElse(null);
  }
}
