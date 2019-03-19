/**
 * File：ArrayUtil.java
 * Package：com.fang.framework.lang
 * Author：wangzhiyuan
 * Date：2016年8月1日 下午1:56:12
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

/**
 * <pre>
 * ArrayUtil 继承了apache commons ArrayUtils的所有功能
 * ThreadSafe
 * </pre>
 *
 * @author wangzhiyuan
 */
public class ArrayUtil extends ArrayUtils {

  /**
   * 将一个二元数组转换成Map
   *
   * @param array
   *        二元数组
   * @param ordered
   *        是否要排序，true：不排序；false：排序
   * @return Map
   */
  public static Map<Object, Object> toMap(final Object[][] array, boolean ordered) {
    if (ordered) {
      return toLinkedMap(array);
    } else {
      return toMap(array);
    }
  }

  /**
   * 将一个二元数组转换成LinkedHashMap
   *
   * @param array
   *        二元数组
   * @return LinkedHashMap
   */
  public static Map<Object, Object> toLinkedMap(final Object[] array) {
    if (array == null) {
      return null;
    }
    final Map<Object, Object> map = new LinkedHashMap<Object, Object>((int) (array.length * 1.5));
    for (int i = 0; i < array.length; i++) {
      final Object object = array[i];
      if (object instanceof Map.Entry<?, ?>) {
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
        map.put(entry.getKey(), entry.getValue());
      } else if (object instanceof Object[]) {
        final Object[] entry = (Object[]) object;
        if (entry.length < 2) {
          throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2");
        }
        map.put(entry[0], entry[1]);
      } else {
        throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
      }
    }
    return map;
  }

  /**
   * List<Map<Object, Object>> to List<Object[]>
   * 只取值，忽略键
   *
   * @param listMap
   *        listMap
   * @return List<Object[]>
   */
  public static List<Object[]> toList(List<Map<Object, Object>> listMap) {

    List<Object[]> rows = new ArrayList<Object[]>();
    for (Map<Object, Object> map : listMap) {
      Iterator<Object> it = map.keySet().iterator();
      List<Object> object = new ArrayList<Object>();
      while (it.hasNext()) {
        object.add(map.get(it.next()));
      }
      rows.add(object.toArray());
    }

    return rows;

  }

  /**
   * 字符串转Integer
   *
   * @param str
   *        str
   * @param separatorChar
   *        separatorChar
   * @return Integer[]
   */
  public static Integer[] split(final String str, final char separatorChar) {
    return splitWorker(str, separatorChar, false);
  }

  /**
   * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods that do not
   * return a
   * maximum array length.
   *
   * @param str
   *        the String to parse, may be {@code null}
   * @param separatorChar
   *        the separate character
   * @param preserveAllTokens
   *        if {@code true}, adjacent separators are
   *        treated as empty token separators; if {@code false}, adjacent
   *        separators are treated as one separator.
   * @return an array of parsed Strings, {@code null} if null String input
   */
  private static Integer[] splitWorker(final String str, final char separatorChar, final boolean preserveAllTokens) {
    // Performance tuned for 2.0 (JDK1.4)

    if (str == null) {
      return null;
    }
    final int len = str.length();
    if (len == 0) {
      return EMPTY_INTEGER_OBJECT_ARRAY;
    }
    final List<Integer> list = new ArrayList<Integer>();
    int i = 0, start = 0;
    boolean match = false;
    boolean lastMatch = false;
    while (i < len) {
      if (str.charAt(i) == separatorChar) {
        if (match || preserveAllTokens) {
          list.add(Integer.parseInt(str.substring(start, i)));
          match = false;
          lastMatch = true;
        }
        start = ++i;
        continue;
      }
      lastMatch = false;
      match = true;
      i++;
    }
    if (match || preserveAllTokens && lastMatch) {
      list.add(Integer.parseInt(str.substring(start, i)));
    }
    return list.toArray(new Integer[list.size()]);
  }

  /**
   * list去重
   *
   * @param list
   *        原list
   * @param <T>
   *        T
   * @return 去重后的list
   */
  public static <T> List<T> distinct(List<T> list) {
    ArrayList<T> re = new ArrayList<T>();
    for (T s : list) {
      if (Collections.frequency(re, s) < 1) {
        re.add(s);
      }
    }
    return re;
  }

  /**
   * 数组去重添加到集合中
   *
   * @param c
   *        返回的集合
   * @param elements
   *        数组元素
   * @param <T>
   *        T
   * @return 返回集合
   */
  @SafeVarargs
  public static <T> boolean distinctAddAll(Collection<? super T> c, T... elements) {
    boolean result = false;
    for (T element : elements) {
      if (Collections.frequency(c, element) < 1) {
        result |= c.add(element);
      }
    }
    return result;
  }
}
