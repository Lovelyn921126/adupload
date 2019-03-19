/**
 * File：DataBaseManager.java
 * Package：com.fang.util
 * Author：yaokaibao
 * Date：2017年4月21日 上午9:37:15
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * DataBaseManager
 *
 * @author yaokaibao
 */
public class HibernateUtil {

  /**
   * cfg path
   */
  private static final String XML_PATH_FOR_MSSQL = "hibernates/hibernateformssql.cfg.xml";

  /**
   * cfg path
   */
  // private static final String XML_PATH_FOR_MYSQL = "hibernates/hibernateformysql.cfg.xml";

  /**
   * DATABASEMANAGER
   */
  private static final Map<String, Configuration> DATABASEMANAGER = new HashMap<String, Configuration>();

  /**
   * BASETYPE MSSQL
   */
  public static final String MSSQL = "dataForMSSQL";

  /**
   * BASETYPE MYSQL
   */
  // public static final String MYSQL = "dataForMYSQL";

  /**
   * Session
   */
  public static Session session;

  /**
   * static
   */
  static {
    Configuration mssqlCfg = new Configuration().configure(XML_PATH_FOR_MSSQL);
    DATABASEMANAGER.put(MSSQL, mssqlCfg);
    // Configuration mysqlCfg = new Configuration().configure(XML_PATH_FOR_MYSQL);
    // DATABASEMANAGER.put(MYSQL, mysqlCfg);
  }

  /**
   * getFactory
   *
   * @param baseType
   *        数据库类型: MSSQL/MSSQL
   * @return SessionFactory
   * @throws Exception
   *         Exception
   */
  private static SessionFactory getFactory(String baseType) throws Exception {
    try {
      Configuration cfg = DATABASEMANAGER.get(baseType);

      return cfg.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build());
    } catch (Exception ex) {
      throw new Exception("Error Creating SessionFactory");
    }
  }

  /**
   * getSession
   *
   * @param baseType
   *        数据库类型: MSSQL/MSSQL
   * @return Session
   * @throws Exception
   *         Exception
   */
  public static Session getSession(String baseType) throws Exception {
    session = getFactory(baseType).openSession();
    return session;
  }

  /**
   * closeSession
   */
  public static void closeSession() {
    if (session != null && session.isOpen()) {
      session.close();
    }
  }
}
