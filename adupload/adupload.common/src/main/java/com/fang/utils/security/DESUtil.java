/**
 * File：DESUtil.java
 * Package：com.fang.framework.lang.security
 * Author："wangzhiyuan"
 * Date：2016年10月28日 下午2:11:19
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.security;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.fang.utils.lang.CharsetsUtil;

/**
 * DES安全
 *
 * @author wangzhiyuan
 */
public class DESUtil {

  /**
   * 加密方法
   *
   * @param data
   *        待加密的字符串
   * @param key
   *        加密的密码（只能为8位长）
   * @return 加密之后的文本
   * @throws Exception
   *         Exception
   */
  public static String encrypt(String data, String key) throws Exception {
    return encrypt(data, key, CharsetsUtil.UTF_8);
  }

  /**
   * 加密方法
   *
   * @param data
   *        待加密的字符串
   * @param key
   *        加密的密码（只能为8位长）
   * @param encoding
   *        编码方式
   * @return 加密之后的文本
   * @throws Exception
   *         Exception
   */
  public static String encrypt(String data, String key, String encoding) throws Exception {
    final byte[] dESkey = key.getBytes(encoding); // 设置密钥，略去
    DESKeySpec keySpec = new DESKeySpec(dESkey); // 设置密钥参数
    AlgorithmParameterSpec iv = new IvParameterSpec(key.getBytes(encoding)); // 设置向量
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); // 获得密钥工厂
    Key rekey = keyFactory.generateSecret(keySpec); // 得到密钥对象
    Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); // 得到加密对象Cipher
    enCipher.init(Cipher.ENCRYPT_MODE, rekey, iv); // 设置工作模式为加密模式，给出密钥和向量
    byte[] pasByte = enCipher.doFinal(data.getBytes(encoding));
    return Base64.encodeBase64String(pasByte);
  }

  /**
   * 解密方法
   *
   * @param data
   *        待解密的字符串
   * @param key
   *        加密的密码（只能为8位长）
   * @return 加密之后的文本
   * @throws Exception
   *         Exception
   */
  public static String decrypt(String data, String key) throws Exception {
    return decrypt(data, key, CharsetsUtil.UTF_8);
  }

  /**
   * 解密方法
   *
   * @param data
   *        待解密的字符串
   * @param key
   *        加密的密码（只能为8位长）
   * @param encoding
   *        编码方式
   * @return 加密之后的文本
   * @throws Exception
   *         Exception
   */
  public static String decrypt(String data, String key, String encoding) throws Exception {
    final byte[] dESkey = key.getBytes(encoding); // 设置密钥，略去
    DESKeySpec keySpec = new DESKeySpec(dESkey); // 设置密钥参数
    AlgorithmParameterSpec iv = new IvParameterSpec(key.getBytes(encoding)); // 设置向量
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); // 获得密钥工厂
    Key rekey = keyFactory.generateSecret(keySpec); // 得到密钥对象
    Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    deCipher.init(Cipher.DECRYPT_MODE, rekey, iv);
    byte[] pasByte = deCipher.doFinal(Base64.decodeBase64(data));
    return new String(pasByte, encoding);
  }
}
