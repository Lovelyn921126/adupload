/**
 * File：MyShiroCacheManager.java
 * Package：com.fang.shiro.impl
 * Author：wangzhiyuan
 * Date：2017年5月24日 下午8:20:41
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.shiro.impl;

import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;

import com.fang.shiro.ShiroCacheManager;
import com.fang.utils.nosql.redis.JedisTemplate;

/**
 * MyShiroCacheManager
 *
 * @author wangzhiyuan
 */
public class MyShiroCacheImpl implements ShiroCacheManager {

  /**
   * 注入redis服务
   */
  @Autowired
  private JedisTemplate jedisTemplateW;

  @Override
  public <K, V> Cache<K, V> getCache(String name) {
    return new JedisShiroCache<K, V>(name, jedisTemplateW);
  }

  @Override
  public void destroy() {

  }

}
