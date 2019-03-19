package com.fang.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Cacheg管理
 *
 * @author wangzhiyuan
 */
public class MyShiroCacheManager implements CacheManager, Destroyable {

  /**
   * ShiroCacheManager共享实现类
   */
  @Autowired
  private ShiroCacheManager shiroCacheManager;

  @Override
  public void destroy() throws Exception {
    shiroCacheManager.destroy();
  }

  @Override
  public <K, V> Cache<K, V> getCache(String name) throws CacheException {
    return shiroCacheManager.getCache(name);
  }
}
