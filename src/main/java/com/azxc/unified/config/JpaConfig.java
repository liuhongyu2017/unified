package com.azxc.unified.config;

import javax.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * SpringDataJpa 配置
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
@EnableJpaAuditing
@Configuration
public class JpaConfig {

}
