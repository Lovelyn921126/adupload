/**
 * File：ShiroCacheManager.java
 * Package：com.fang.shiro
 * Author：wangzhiyuan
 * Date：2017年5月24日 下午8:16:54
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.shiro;

import org.apache.shiro.cache.Cache;

/**
 * 缓存管理
 *
 * @author wangzhiyuan
 */
public interface ShiroCacheManager {

  /**
   * getCache
   *
   * @param name
   *        name
   * @param <K>
   *        k
   * @param <V>
   *        V
   * @return getCache
   */
  <K, V> Cache<K, V> getCache(String name);

  /**
   * destroy
   */
  void destroy();
}
