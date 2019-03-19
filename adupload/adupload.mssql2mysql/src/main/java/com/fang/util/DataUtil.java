/**
 * File：DataUtil.java
 * Package：com.fang.util
 * Author：yaokaibao
 * Date：2017年4月21日 上午11:12:07
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.util;

//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.fang.dao.AdvertFileDao;
import com.fang.dao.SysCityDao;
import com.fang.dao.SysUserDao;
import com.fang.dao.SysUserToCityDao;
import com.fang.dao.SysUserToRoleDao;
import com.fang.pojoformssql.SlFile;
import com.fang.pojoformssql.SlSysCity;
import com.fang.pojoformssql.SlSysUser;
import com.fang.pojoformysql.AdvertFile;
import com.fang.pojoformysql.SysCity;
import com.fang.pojoformysql.SysUser;

/**
 * DataUtil
 *
 * @author yaokaibao
 */
public class DataUtil {

  /**
   * 每页记录数
   */
  public static final int PAGESIZE = 10000; // 00

  /**
   * getLastTime
   *
   * @return 获取时间 （昨天 20时）
   */
  private Date getLastTime() {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DAY_OF_MONTH, -1);
    date.set(Calendar.HOUR_OF_DAY, 20);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    return date.getTime();
  }

  /**
   * cityFilter 去重（将城市名称相同的ID较大的替换为ID较小的）
   *
   * @param cityId
   *        cityId
   * @return cityId
   */
  public Integer cityFilter(Integer cityId) {
    switch (cityId) {
      case 235:
        return 58; // 福州
      case 231:
        return 188; // 衡阳
      case 211:
        return 127; // 呼和浩特
      case 229:
        return 149; // 嘉兴
      case 233:
        return 129; // 南通
      case 228:
        return 168; // 芜湖
      case 230:
        return 147; // 镇江
      case 234:
        return 93; // 珠海

      default:
        return cityId;
    }
  }

  /**
   * isBlankForCharSequence
   *
   * @param cs
   *        cs
   * @return boolean
   */
  public boolean isBlankForCharSequence(final CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (Character.isWhitespace(cs.charAt(i)) == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * getDataForFile
   *
   * @param pageIndex
   *        pageIndex
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<SlFile> getDataForFile(int pageIndex) {
    List<SlFile> list = new ArrayList<SlFile>();
    Session session = null;
    Transaction transaction = null;
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createCriteria(SlFile.class).add(Restrictions.neOrIsNotNull("sourceId", "")).addOrder(Order.asc("updateTime")).addOrder(Order.asc("id"))
          .setFirstResult(pageIndex * PAGESIZE).setMaxResults(PAGESIZE).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * getDataForUser
   *
   * @param pageIndex
   *        pageIndex
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<SlSysUser> getDataForUser(int pageIndex) {
    List<SlSysUser> list = new ArrayList<SlSysUser>();
    Session session = null;
    Transaction transaction = null;
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createCriteria(SlSysUser.class).add(Restrictions.eq("isDelete", 0)).addOrder(Order.asc("updateTime")).addOrder(Order.asc("id"))
          .setFirstResult(pageIndex * PAGESIZE).setMaxResults(PAGESIZE).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * getDataForCity
   *
   * @param pageIndex
   *        pageIndex
   * @return list
   */
  @SuppressWarnings("unchecked")
  public List<SlSysCity> getDataForCity(int pageIndex) {
    List<SlSysCity> list = new ArrayList<SlSysCity>();
    Session session = null;
    Transaction transaction = null;
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createCriteria(SlSysCity.class).addOrder(Order.asc("id")).setFirstResult(pageIndex * PAGESIZE).setMaxResults(PAGESIZE).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * getDataForUserToCity
   *
   * @param pageIndex
   *        pageIndex
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<Object[]> getDataForUserToCity(int pageIndex) {
    List<Object[]> list = new ArrayList<Object[]>();
    Session session = null;
    Transaction transaction = null;
    String sql = "SELECT uc.ID ,uc.UserID ,uc.CityID FROM SlSysUserToCity as uc with(nolock) INNER JOIN SlSysUser as u with(nolock) ON uc.UserID = u.ID WHERE　u.IsDelete = 0";
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createSQLQuery(sql).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * getDataForUserToRole
   *
   * @param pageIndex
   *        pageIndex
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<Object[]> getDataForUserToRole(int pageIndex) {
    List<Object[]> list = new ArrayList<Object[]>();
    Session session = null;
    Transaction transaction = null;
    String sql = "SELECT ur.ID ,ur.SysUserID ,ur.SysRoleID FROM SlSysUserToRole as ur with(nolock) INNER JOIN SlSysUser as u with(nolock) ON ur.SysUserID = u.ID WHERE　u.IsDelete = 0";
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createSQLQuery(sql).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * setFilesbyMybatis
   *
   * @param list
   *        list
   */
  public void setFilesbyMybatis(List<AdvertFile> list) {
    SqlSession session = null;
    try {
      session = MybatisUtil.getSession();
      session.getMapper(AdvertFileDao.class).addBatch(list);
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      MybatisUtil.closeSession();
    }
  }

  /**
   * setUsersbyMybatis
   *
   * @param list
   *        list
   */
  public void setUsersbyMybatis(List<SysUser> list) {
    SqlSession session = null;
    try {
      session = MybatisUtil.getSession();
      session.getMapper(SysUserDao.class).addBatch(list);
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      MybatisUtil.closeSession();
    }

  }

  /**
   * setCitysbyMybatis
   *
   * @param list
   *        list
   */
  public void setCitysbyMybatis(List<SysCity> list) {
    SqlSession session = null;
    try {
      session = MybatisUtil.getSession();
      session.getMapper(SysCityDao.class).addBatch(list);
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      MybatisUtil.closeSession();
    }

  }

  /**
   * setUserToCitysbyMybatis
   *
   * @param list
   *        list
   */
  public void setUserToCitysbyMybatis(List<Map<String, Object>> list) {
    SqlSession session = null;
    try {
      session = MybatisUtil.getSession();
      session.getMapper(SysUserToCityDao.class).addBatch(list);
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      MybatisUtil.closeSession();
    }

  }

  /**
   * setUserToRolesbyMybatis
   *
   * @param list
   *        list
   */
  public void setUserToRolesbyMybatis(List<Map<String, Object>> list) {
    SqlSession session = null;
    try {
      session = MybatisUtil.getSession();
      session.getMapper(SysUserToRoleDao.class).addBatch(list);
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      MybatisUtil.closeSession();
    }

  }

  /**
   * getDataForUpdateFile
   *
   * @param pageIndex
   *        pageIndex
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<SlFile> getDataForUpdateFile(int pageIndex) {

    List<SlFile> list = new ArrayList<SlFile>();
    Session session = null;
    Transaction transaction = null;
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createCriteria(SlFile.class).add(Restrictions.neOrIsNotNull("sourceId", "")).add(Restrictions.ge("updateTime", getLastTime()))
          .addOrder(Order.asc("updateTime")).addOrder(Order.asc("id")).setFirstResult(pageIndex * PAGESIZE).setMaxResults(PAGESIZE).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * getDataForUpdateUser
   *
   * @param pageIndex
   *        pageIndex
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<SlSysUser> getDataForUpdateUser(int pageIndex) {

    List<SlSysUser> list = new ArrayList<SlSysUser>();
    Session session = null;
    Transaction transaction = null;
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createCriteria(SlSysUser.class).add(Restrictions.eq("isDelete", 0)).add(Restrictions.ge("updateTime", getLastTime())).addOrder(Order.asc("updateTime"))
          .addOrder(Order.asc("id")).setFirstResult(pageIndex * PAGESIZE).setMaxResults(PAGESIZE).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * getDataForUpdateUserToCity
   *
   * @param userId
   *        userId
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<Object[]> getDataForUpdateUserToCity(String userId) {

    List<Object[]> list = new ArrayList<Object[]>();
    Session session = null;
    Transaction transaction = null;
    String sql = "SELECT uc.ID ,uc.UserID ,uc.CityID FROM SlSysUserToCity as uc with(nolock) INNER JOIN SlSysUser as u with(nolock) ON uc.UserID = u.ID WHERE　u.IsDelete = 0 and u.ID=?";
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createSQLQuery(sql).setParameter(0, userId).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * getDataForUpdateUserToRole
   *
   * @param userId
   *        userId
   * @return list
   */
  @SuppressWarnings({"unchecked"})
  public List<Object[]> getDataForUpdateUserToRole(String userId) {

    List<Object[]> list = new ArrayList<Object[]>();
    Session session = null;
    Transaction transaction = null;
    String sql = "SELECT ur.ID ,ur.SysUserID ,ur.SysRoleID FROM SlSysUserToRole as ur with(nolock) INNER JOIN SlSysUser as u with(nolock) ON ur.SysUserID = u.ID WHERE　u.IsDelete = 0 and u.ID=?";
    try {
      session = HibernateUtil.getSession(HibernateUtil.MSSQL);
      transaction = session.beginTransaction();
      list = session.createSQLQuery(sql).setParameter(0, userId).list();
      transaction.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    } finally {
      HibernateUtil.closeSession();
    }
    return list;
  }

  /**
   * saveOrUpdateFile
   *
   * @param file
   *        file
   */
  public void saveOrUpdateFile(AdvertFile file) {
    SqlSession session = null;
    try {
      session = MybatisUtil.getSession();
      if (session.getMapper(AdvertFileDao.class).delete(file) > -1) {
        file.setCityId(cityFilter(file.getCityId()));
        session.getMapper(AdvertFileDao.class).add(file);
      }
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      MybatisUtil.closeSession();
    }

  }

  /**
   * saveOrUpdateUser
   *
   * @param user
   *        user
   */
  public void saveOrUpdateUser(SysUser user) {
    SqlSession session = null;
    try {
      session = MybatisUtil.getSession();
      if (session.getMapper(SysUserDao.class).delete(user) > -1) {
        session.getMapper(SysUserDao.class).add(user);
      }
      session.commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      MybatisUtil.closeSession();
    }

  }

  /**
   * saveOrUpdateUserToCity
   *
   * @param user
   *        user
   */
  public void saveOrUpdateUserToCity(SysUser user) {
    SqlSession session = null;
    List<Object[]> list = getDataForUpdateUserToCity(user.getUserId());
    if (list != null && !list.isEmpty()) {
      try {
        List<Map<String, Object>> ucLists = new ArrayList<Map<String, Object>>();
        for (Object[] objs : list) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("id", objs[0]);
          map.put("userId", objs[1]);
          map.put("cityId", cityFilter((Integer) objs[2]));
          ucLists.add(map);
        }
        if (ucLists.size() >= 110) { // 多于110(总142)个城市，则认为是选择全部 即设为全国
          ucLists.clear();
          Object id = ucLists.get(0).get("id"); // 获取一个GUID
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("id", id);
          map.put("userId", user.getUserId());
          map.put("cityId", 0); // 全国 0
          ucLists.add(map);
        }
        session = MybatisUtil.getSession();
        if (session.getMapper(SysUserToCityDao.class).delete(user) > -1) {
          session.getMapper(SysUserToCityDao.class).addBatch(ucLists);
        }
        session.commit();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        MybatisUtil.closeSession();
      }
    }
  }

  /**
   * saveOrUpdateUserToRole
   *
   * @param user
   *        user
   */
  public void saveOrUpdateUserToRole(SysUser user) {
    SqlSession session = null;
    List<Object[]> list = getDataForUpdateUserToRole(user.getUserId());
    if (list != null && !list.isEmpty()) {
      try {
        List<Map<String, Object>> ucLists = new ArrayList<Map<String, Object>>();
        for (Object[] objs : list) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("id", objs[0]);
          map.put("userId", objs[1]);
          map.put("roleId", objs[2]);
          ucLists.add(map);
        }
        session = MybatisUtil.getSession();
        if (session.getMapper(SysUserToRoleDao.class).delete(user) > -1) {
          session.getMapper(SysUserToRoleDao.class).addBatch(ucLists);
        }
        session.commit();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        MybatisUtil.closeSession();
      }
    }

  }

}
