package com.azxc.unified.config;

import net.sf.ehcache.CacheManager;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * ehcache 配置类
 *
 * @author lhy
 * @version 1.0 2020/3/13
 */
@EnableCaching
@Configuration
public class CacheConfig {

  @Bean
  public CacheManager ehCacheCacheManager(CacheProperties cacheProperties) {
    Resource location = cacheProperties
        .resolveConfigLocation(cacheProperties.getEhcache().getConfig());
    if (location != null) {
      return EhCacheManagerUtils.buildCacheManager(location);
    }
    return EhCacheManagerUtils.buildCacheManager();
  }
}
