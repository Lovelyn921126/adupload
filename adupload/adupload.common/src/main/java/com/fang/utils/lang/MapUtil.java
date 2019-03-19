/**
 * File：MapUtil.java
 * Package：com.fang.framework.lang
 * Author：wangzhiyuan
 * Date：2016年8月1日 下午1:58:07
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.lang;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;

import com.fang.utils.lang.time.DateTime;
import com.fang.utils.security.CollatorComparator;


/**
 * <pre>
 * ArrayUtil 继承了apache commons MapUtils的所有功能
 * ThreadSafe
 * </pre>
 *
 * @author wangzhiyuan
 */
public class MapUtil extends MapUtils {

  /**
   * map to Obj
   *
   * @param map
   *        待转换的map
   * @param beanClass
   *        目标bean类型
   * @return bean
   * @throws Exception
   *         Exception
   */
  public static Object mapToObj(Map<String, Object> map, Class<?> beanClass) throws Exception {
    if (map == null) {
      return null;
    }
    Object obj = beanClass.newInstance();
    BeanUtils.populate(obj, map);
    return obj;
  }

  /**
   * obj to hashmap
   *
   * @param obj
   *        待转换的bean
   * @return hashmap
   */
  public static Map<?, ?> objToHashMap(Object obj) {
    if (obj == null) {
      return null;
    }
    return new BeanMap(obj);
  }

  /**
   * obj to hashmap(对DateTime类型做特殊处理)
   *
   * @param obj
   *        待转换的bean
   * @param dateTimeType
   *        时间类型、1：字符串时间，2：毫秒数
   * @return hashmap
   * @throws Exception
   *         Exception
   */
  public static Map<String, String> objToHashMap(Object obj, int dateTimeType) throws Exception {
    if (obj == null) {
      return null;
    }

    Map<String, String> map = new HashMap<String, String>();

    BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    for (PropertyDescriptor property : propertyDescriptors) {
      String key = property.getName();
      if (key.compareToIgnoreCase("class") == 0) {
        continue;
      }
      Method getter = property.getReadMethod();
      Object value = getter != null ? getter.invoke(obj) : null;
      value = value == null ? "" : value;
      if (value instanceof DateTime) {
        map.put(key, dateTimeType == 1 ? ((DateTime) value).toDateTimeMillisString() : String.valueOf(((DateTime) value).getTimeInMillis()));
      } else {
        map.put(key, value.toString());
      }

    }

    return map;
  }

  /**
   * obj to treeMap
   *
   * @param obj
   *        待转换的bean
   * @return treeMap
   */
  @SuppressWarnings("unchecked")
  public static Map<?, ?> objToTreeMap(Object obj) {
    if (obj == null) {
      return null;
    }
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.putAll(new BeanMap(obj));
    return treeMap;
  }

  /**
   * obj to treeMap
   *
   * @param obj
   *        待转换的bean
   * @return treeMap
   */
  public static TreeMap<?, ?> objToTreeMapWithComparator(TreeMap<?, ?> obj) {
    if (obj == null) {
      return null;
    }
    CollatorComparator comparator = new CollatorComparator();
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>(comparator);
    treeMap.putAll(obj);
    return treeMap;
  }
}
