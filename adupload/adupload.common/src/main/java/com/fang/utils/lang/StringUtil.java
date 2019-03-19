/**
 * File：StringUtil.java
 * Package：com.fang.framework.lang
 * Author：wangzhiyuan
 * Date：2016年7月20日 上午10:23:45
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.lang;

import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * StringUtil 继承了apache commons StringUtils的所有功能
 * ThreadSafe
 * </pre>
 *
 * @author wangzhiyuan
 */
public class StringUtil extends StringUtils {

  /**
   * 从指定位置开始截取指定长度的字符串
   *
   * @param str
   *        输入字符串
   * @param start
   *        截取位置，左侧第一个字符索引值是 0
   * @param count
   *        截取长度
   * @return 截取字符串
   */
  public static String middle(final String str, int start, int count) {
    if (isEmpty(str)) {
      return "";
    }
    count = (count > str.length() - start + 1) ? str.length() - start + 1 : count;
    return substring(str, start, start + count);
  }

}
