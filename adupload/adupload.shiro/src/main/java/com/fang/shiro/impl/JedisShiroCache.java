package com.fang.shiro.impl;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;

import com.fang.utils.CLogger;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.serializer.SerializeUtil;

/**
 * 缓存
 *
 * @author wangzhiyuan
 * @param <K>
 *        k
 * @param <V>
 *        v
 */
public class JedisShiroCache<K, V> implements Cache<K, V> {

  /**
   * redis Cache key前缀
   */
  private static final String REDIS_SHIRO_CACHE = "shiro-cache:";

  /**
   * name
   */
  private String name;

  /**
   * name
   */
  private JedisTemplate jedisTemplate;

  /**
   * JedisShiroCacheManager
   *
   * @param name
   *        name
   * @param jedisTemplate
   *        jedisTemplate
   */
  public JedisShiroCache(String name, JedisTemplate jedisTemplate) {
    this.name = name;
    this.jedisTemplate = jedisTemplate;
  }

  /**
   * 自定义relm中的授权/认证的类名加上授权/认证英文名字
   *
   * @return name
   */
  public String getName() {
    if (name == null) {
      return "";
    }
    return name;
  }

  /**
   * setName
   *
   * @param name
   *        name
   */
  public void setName(String name) {
    this.name = name;
  }

  @SuppressWarnings("unchecked")
  @Override
  public V get(K key) throws CacheException {
    String byteValue = StringUtils.EMPTY;
    try {
      if (key == null) {
        return null;
      } else {
        byteValue = jedisTemplate.get(getRedisSessionKey(key));
      }
    } catch (Exception e) {
      CLogger.error("get value by cache throw exception", e);
    }
    return (V) SerializeUtil.deserialize(byteValue);
  }

  @Override
  public V put(K key, V value) throws CacheException {
    V previos = get(key);
    try {
      jedisTemplate.set(getRedisSessionKey(key), SerializeUtil.serialize(value, "utf-8"));
    } catch (Exception e) {
      CLogger.error("put cache throw exception", e);
    }
    return previos;
  }

  @Override
  public V remove(K key) throws CacheException {
    V previos = get(key);
    try {
      jedisTemplate.del(getRedisSessionKey(key));
    } catch (Exception e) {
      CLogger.error("remove cache  throw exception", e);
    }
    return previos;
  }

  @Override
  public void clear() throws CacheException {
  }

  @Override
  public int size() {
    if (keys() == null) {
      return 0;
    }
    return keys().size();
  }

  @Override
  public Set<K> keys() {
    return null;
  }

  @Override
  public Collection<V> values() {
    return null;
  }

  /**
   * 拼装key
   *
   * @param key
   *        key
   * @return key
   */
  private String getRedisSessionKey(Object key) {
    if (key instanceof String) {
      return REDIS_SHIRO_CACHE + key;
    } else if (key instanceof PrincipalCollection) {
      return REDIS_SHIRO_CACHE + key.toString();
    } else {
      return SerializeUtil.serialize(key, "utf-8");
    }
  }

  /**
   * getJedisTemplate
   *
   * @return jedisTemplate
   */
  public JedisTemplate getJedisTemplate() {
    return jedisTemplate;
  }

  /**
   * setJedisTemplate
   *
   * @param jedisTemplate
   *        set jedisTemplate
   */
  public void setJedisTemplate(JedisTemplate jedisTemplate) {
    this.jedisTemplate = jedisTemplate;
  }

}
