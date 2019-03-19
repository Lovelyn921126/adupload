/**
 * File：MybatisUtil.java
 * Package：com.fang.util
 * Author：yaokaibao
 * Date：2017年4月21日 下午9:23:42
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.util;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * MybatisUtil
 *
 * @author yaokaibao
 */
public class MybatisUtil {

  /**
   * cfg path
   */
  private static final String XML_PATH = "mybatis/mybatisformysql.cfg.xml";

  /**
   * config
   */
  private static InputStream config = null;

  /**
   * factory
   */
  private static SqlSessionFactory factory;

  /**
   * session
   */
  private static SqlSession session = null;

  /**
   * static
   */
  static {
    config = MybatisUtil.class.getClassLoader().getResourceAsStream(XML_PATH);
  }

  /**
   * getFactory
   *
   * @return SqlSessionFactory
   * @throws Exception
   *         Exception
   */
  private static SqlSessionFactory getFactory() throws Exception {
    try {
      if (config == null) {
        config = MybatisUtil.class.getClassLoader().getResourceAsStream(XML_PATH);
      }

      factory = new SqlSessionFactoryBuilder().build(config);
      return factory;
    } catch (Exception ex) {
      throw new Exception("Error Creating SessionFactory");
    }
  }

  /**
   * getSession
   *
   * @return SqlSession
   * @throws Exception
   *         Exception
   */
  public static SqlSession getSession() throws Exception {
    if (factory == null) {
      factory = getFactory();
    }
    session = factory.openSession();
    return session;
  }

  /**
   * closeSession
   */
  public static void closeSession() {
    if (session != null) {
      session.close();

    }
  }
}
