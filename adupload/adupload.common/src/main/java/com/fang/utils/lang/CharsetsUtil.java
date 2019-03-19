/**
 * File：CharsetsUtil.java
 * Package：com.fang.framework.lang
 * Author：wangzhiyuan
 * Date：2016年7月20日 上午11:00:30
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.lang;

import org.apache.commons.codec.CharEncoding;

/**
 * <p>
 * Character encoding names required of every implementation of the Java platform.
 * </p>
 *
 * <cite>Every implementation of the Java platform is required to support the following character
 * encodings. Consult the
 * release documentation for your implementation to see if any other encodings are supported.</cite>
 *
 * @see org.apache.commons.lang3.CharEncoding
 * @author wangzhiyuan
 * @since 2.1
 * @version $Id: CharEncoding.java 437554 2006-08-28 06:21:41Z bayard $
 */
public class CharsetsUtil extends CharEncoding {

  /**
   * <p>
   * ISO Latin Alphabet #1, also known as GB2312.
   * </p>
   * <p>
   * Every implementation of the Java platform is required to support this character encoding.
   * </p>
   *
   * @see <a href="http://java.sun.com/j2se/1.3/docs/api/java/lang/package-summary.html#charenc">JRE
   *      character encoding names</a>
   */
  public static final String GB2312 = "GB2312";

  /**
   * <p>
   * ISO Latin Alphabet #1, also known as GBK.
   * </p>
   * <p>
   * Every implementation of the Java platform is required to support this character encoding.
   * </p>
   *
   * @see <a href="http://java.sun.com/j2se/1.3/docs/api/java/lang/package-summary.html#charenc">JRE
   *      character encoding names</a>
   */
  public static final String GBK = "GBK";
}
