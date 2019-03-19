/**
 * File：CollatorComparator.java
 * Package：com.fang.framework.lang.security
 * Author：wangzhiyuan
 * Date：2017年1月3日 下午8:12:42
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.security;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

/**
 * 按照字母排序比较器
 *
 * @author wangzhiyuan
 */
public class CollatorComparator implements Comparator<Object> {

  /**
   * 比较器
   */
  Collator collator = Collator.getInstance();

  /**
   * 自定义比较器
   *
   * @param element1
   *        element1
   * @param element2
   *        element2
   * @return 排序
   */
  public int compare(Object element1, Object element2) {
    CollationKey key1 = collator.getCollationKey(element1.toString().toLowerCase());
    CollationKey key2 = collator.getCollationKey(element2.toString().toLowerCase());
    return key1.compareTo(key2);
  }
}
