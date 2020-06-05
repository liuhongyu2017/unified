package com.azxc.unified.repository;

import com.azxc.unified.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 账单
 *
 * @author lhy
 * @version 1.0 2020/4/1
 */
public interface BillingRepository extends JpaRepository<Billing, Long>,
    JpaSpecificationExecutor<Billing> {

}
