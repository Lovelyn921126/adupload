/**
 * File：IdUtil.java
 * Package：com.fang.framework.utils
 * Author：wangzhiyuan
 * Date：2016年7月20日 下午3:04:22
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 *
 * @author wangzhiyuan
 */
public class IdUtil {

  /**
   * SecureRandom
   */
  private static SecureRandom random = new SecureRandom();

  /**
   * 生成默认的guid "00000000-0000-0000-0000-000000000000"
   */
  public static final String EMPTY = "00000000-0000-0000-0000-000000000000";

  /**
   * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
   *
   * @return guid
   */
  public static String newGuid2() {
    return UUID.randomUUID().toString();
  }

  /**
   * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
   *
   * @return guid
   */
  public static String newGuid() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * 使用SecureRandom随机生成Long.
   *
   * @return 随机生成Long.
   */
  public static long randomLong() {
    return Math.abs(random.nextLong());
  }

  /**
   * 基于Base62编码的SecureRandom随机生成bytes.
   *
   * @param length
   *        长度
   * @return bytes
   */
  public static String randomBase62(int length) {
    byte[] randomBytes = new byte[length];
    random.nextBytes(randomBytes);
    return CodecUtil.encodeBase62(randomBytes);
  }

  /**
   * 生成指定位数的 数字随机数
   *
   * @param units
   *        长度
   * @return 随机数
   */
  public static String randomInt(int units) {
    StringBuffer randomNum = new StringBuffer();
    for (int i = 0; i < units; i++) {
      int newNum = Math.abs(random.nextInt(10));
      randomNum.append(newNum);
    }
    return randomNum.toString();
  }

  /**
   * 生成指定位数的 数字随机数
   *
   * @param units
   *        长度
   * @param uppercase
   *        是否大小写
   * @return randomChar
   */
  public static String randomChar(int units, boolean uppercase) {
    StringBuffer randomNum = new StringBuffer();
    int temp = uppercase ? 65 : 97;
    for (int i = 0; i < units; i++) {
      randomNum.append((char) (temp + random.nextInt(26)));
    }
    return randomNum.toString();
  }

  /**
   * 生成指定位数的 数字加字母随机数
   *
   * @param units
   *        长度
   * @return 随机数
   */
  public static String randomIntAndChar(int units) {
    StringBuffer randomNum = new StringBuffer();
    for (int i = 0; i < units; i++) {

      String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
      // 输出字母还是数字
      if ("char".equalsIgnoreCase(charOrNum)) {
        // 输出是大写字母还是小写字母
        int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
        randomNum.append((random.nextInt(26) + temp));
      } else if ("num".equalsIgnoreCase(charOrNum)) {
        randomNum.append(Math.abs(random.nextInt(10)));
      }
    }
    return randomNum.toString();
  }

  /**
   * 生成指定位数的 数字随机数
   *
   * @param units
   *        长度
   * @return 随机数
   */
  public static String randomIntOrChar(int units) {
    StringBuffer randomNum = new StringBuffer();
    for (int i = 0; i < units; i++) {

      String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
      // 输出字母还是数字
      if ("char".equalsIgnoreCase(charOrNum)) {
        // 输出是大写字母还是小写字母
        int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
        randomNum.append((random.nextInt(26) + temp));
      } else if ("num".equalsIgnoreCase(charOrNum)) {
        randomNum.append(Math.abs(random.nextInt(10)));
      }
    }
    return randomNum.toString();
  }
}
