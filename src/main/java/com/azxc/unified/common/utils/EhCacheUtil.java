package com.azxc.unified.common.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * ehcache缓存操作工具类
 *
 * @author lhy
 * @version 1.0 2020/3/13
 */
public class EhCacheUtil {

  /**
   * 获取EhcacheManger管理对象
   */
  private static CacheManager getCacheManger() {
    return SpringContextUtil.getBean(CacheManager.class);
  }

  /**
   * 获取Ehcache缓存对象
   */
  public static Cache getCache(String name) {
    return getCacheManger().getCache(name);
  }

  /**
   * 获取字典缓存对象
   */
  public static Cache getDictCache() {
    return getCache("dictionary");
  }

  /**
   * 获取字典缓存对象
   */
  public static Cache getMenuCache() {
    return getCache("menu");
  }

  /**
   * 用户缓存
   */
  public static Cache getUserCache() {
    return getCache("user");
  }
}
