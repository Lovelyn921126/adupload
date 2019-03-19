/**
 * File：SerializeUtil.java
 * Package：com.fang.utils.serializer
 * Author：wangzhiyuan
 * Date：2017年5月24日 下午3:43:30
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.fang.utils.CLogger;

/**
 * 序列化工具
 *
 * @author wangzhiyuan
 */
@SuppressWarnings("unchecked")
public class SerializeUtil {

  /**
   * 序列化成数组
   *
   * @param value
   *        value
   * @return 数组
   */
  public static byte[] serialize(Object value) {
    if (value == null) {
      throw new NullPointerException("Can't serialize null");
    }
    byte[] rv = null;
    ByteArrayOutputStream bos = null;
    ObjectOutputStream os = null;
    try {
      bos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(bos);
      os.writeObject(value);
      os.close();
      bos.close();
      rv = bos.toByteArray();
    } catch (Exception e) {
      CLogger.error("serialize erro %s", e);
    } finally {
      close(os);
      close(bos);
    }
    return rv;
  }

  /**
   * 序列化成字符串
   *
   * @param value
   *        value
   * @param encoding
   *        encoding
   * @return 字符串
   */
  public static String serialize(Object value, String encoding) {
    if (value == null) {
      throw new NullPointerException("Can't serialize null");
    }
    ByteArrayOutputStream bos = null;
    ObjectOutputStream os = null;
    String serStr = null;
    try {
      bos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(bos);
      os.writeObject(value);
      os.close();
      bos.close();
      serStr = bos.toString("ISO-8859-1");
      serStr = java.net.URLEncoder.encode(serStr, encoding);
    } catch (Exception e) {
      CLogger.error("serialize erro %s", e);
    } finally {
      close(os);
      close(bos);
    }
    return serStr;
  }

  /**
   * 反序列化数组
   *
   * @param in
   *        in
   * @return Object
   */
  public static Object deserialize(byte[] in) {
    return deserialize(in, Object.class);
  }

  /**
   * 反序列化字符串
   *
   * @param in
   *        in
   * @return Object
   */
  public static Object deserialize(String in) {
    return deserialize(in, "UTF-8", Object.class);
  }

  /**
   * 反序列化数组
   *
   * @param in
   *        in
   * @param requiredType
   *        requiredType
   * @param <T>
   *        T
   * @return T
   */
  public static <T> T deserialize(byte[] in, Class<T>... requiredType) {
    Object rv = null;
    ByteArrayInputStream bis = null;
    ObjectInputStream is = null;
    try {
      if (in != null) {
        bis = new ByteArrayInputStream(in);
        is = new ObjectInputStream(bis);
        rv = is.readObject();
      }
    } catch (Exception e) {
      CLogger.error("deserialize erro");
    } finally {
      close(is);
      close(bis);
    }
    return (T) rv;
  }

  /**
   * 反序列化字符串
   *
   * @param in
   *        in
   * @param encoding
   *        encoding
   * @param requiredType
   *        requiredType
   * @param <T>
   *        T
   * @return T
   */
  public static <T> T deserialize(String in, String encoding, Class<T>... requiredType) {
    Object rv = null;
    ByteArrayInputStream bis = null;
    ObjectInputStream is = null;
    try {
      if (in != null) {
        String deserStr = java.net.URLDecoder.decode(in, encoding);
        bis = new ByteArrayInputStream(deserStr.getBytes("ISO-8859-1"));
        is = new ObjectInputStream(bis);
        rv = is.readObject();
      }
    } catch (Exception e) {
      CLogger.error("deserialize erro");
    } finally {
      close(is);
      close(bis);
    }
    return (T) rv;
  }

  /**
   * 关闭操作
   *
   * @param closeable
   *        closeable
   */
  private static void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        CLogger.error("close stream error");
      }
    }

  }

}
