/**
 * File：DataMove.java
 * Package：com.fang.main
 * Author：yaokaibao
 * Date：2017年4月21日 上午10:48:48
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fang.pojoformssql.SlFile;
import com.fang.pojoformssql.SlSysCity;
import com.fang.pojoformssql.SlSysUser;
import com.fang.pojoformysql.AdvertFile;
import com.fang.pojoformysql.SysCity;
import com.fang.pojoformysql.SysUser;
import com.fang.util.DataUtil;
import com.fang.util.FileLogger;

/**
 * DataMove
 *
 * @author yaokaibao
 */
public class DataMove {

  /**
   * util
   */
  private static DataUtil util = new DataUtil();

  /**
   * moveForFile
   *
   * @throws Exception
   *         Exception
   */
  public static void moveForFile() throws Exception {
    FileLogger.info("--------------------------FILE MOVE--------------------------");
    FileLogger.info("开始移动数据！");
    List<SlFile> list = null;
    List<AdvertFile> fileLists = null;
    int pageIndex = 0;
    while (true) {
      fileLists = new ArrayList<AdvertFile>();
      list = util.getDataForFile(pageIndex);
      if (list == null || list.isEmpty()) {
        break;
      }
      for (SlFile file : list) {
        AdvertFile adFile = new AdvertFile(file);
        fileLists.add(adFile);
      }

      util.setFilesbyMybatis(fileLists);

      FileLogger.info("已完成" + (list.size() + DataUtil.PAGESIZE * pageIndex));

      pageIndex++;
    }
  }

  /**
   * moveForUser
   *
   * @throws Exception
   *         Exception
   */
  public static void moveForUser() throws Exception {
    FileLogger.info("--------------------------USER MOVE--------------------------");
    FileLogger.info("开始移动数据！");
    List<SlSysUser> list = null;
    List<SysUser> userLists = null;
    int pageIndex = 0;
    while (true) {
      userLists = new ArrayList<SysUser>();
      list = util.getDataForUser(pageIndex);
      if (list == null || list.isEmpty()) {
        break;
      }
      for (SlSysUser user : list) {
        SysUser adUser = new SysUser(user);
        userLists.add(adUser);
      }

      util.setUsersbyMybatis(userLists);

      FileLogger.info("已完成" + (list.size() + DataUtil.PAGESIZE * pageIndex));

      pageIndex++;
    }
  }

  /**
   * moveForCity
   *
   * @throws Exception
   *         Exception
   */
  public static void moveForCity() throws Exception {
    FileLogger.info("--------------------------CITY MOVE--------------------------");
    FileLogger.info("开始移动数据！");
    List<SlSysCity> list = null;
    List<SysCity> cityLists = null;
    int pageIndex = 0;
    int totle = 0;
    while (true) {
      cityLists = new ArrayList<SysCity>();
      list = util.getDataForCity(pageIndex);
      if (list == null || list.isEmpty()) {
        break;
      }
      for (SlSysCity city : list) {
        if (city.getId() == util.cityFilter(city.getId())) {
          SysCity adCity = new SysCity(city);
          cityLists.add(adCity);
        }
      }

      util.setCitysbyMybatis(cityLists);

      totle += cityLists.size();
      FileLogger.info("已完成" + totle);

      pageIndex++;
    }
  }

  /**
   * moveForUserToCity
   *
   * @throws Exception
   *         Exception
   */
  public static void moveForUserToCity() throws Exception {
    FileLogger.info("--------------------------USERTOCITY MOVE--------------------------");
    FileLogger.info("开始移动数据！");
    List<Object[]> list = null;
    List<Map<String, Object>> ucLists = null;
    Map<Object, List<Map<String, Object>>> maps = new HashMap<Object, List<Map<String, Object>>>();
    int pageIndex = 0;
    int totle = 0;
    while (pageIndex < 1) {
      ucLists = new ArrayList<Map<String, Object>>();
      list = util.getDataForUserToCity(pageIndex);
      if (list == null || list.isEmpty()) {
        break;
      }
      for (Object[] objs : list) {
        if (!maps.keySet().contains(objs[1])) {
          maps.put(objs[1], new ArrayList<Map<String, Object>>());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", objs[0]);
        map.put("userId", objs[1]);
        map.put("cityId", util.cityFilter((Integer) objs[2]));
        maps.get(objs[1]).add(map);
      }

      for (Object key : maps.keySet()) {
        if (maps.get(key).size() >= 110) { // 多于110(总142)个城市，则认为是选择全部 即设为全国
          Object id = maps.get(key).get(0).get("id"); // 获取一个GUID
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("id", id);
          map.put("userId", key);
          map.put("cityId", 0); // 全国 0
          ucLists.add(map);
        } else {
          ucLists.addAll(maps.get(key));
        }
      }

      util.setUserToCitysbyMybatis(ucLists);

      totle += ucLists.size();
      FileLogger.info("已完成" + totle);

      pageIndex++;
    }
  }

  /**
   * moveForUserToRole
   *
   * @throws Exception
   *         Exception
   */
  public static void moveForUserToRole() throws Exception {
    FileLogger.info("--------------------------USERTOROLE MOVE--------------------------");
    FileLogger.info("开始移动数据！");
    List<Object[]> list = null;
    List<Map<String, Object>> urLists = null;
    int pageIndex = 0;
    int totle = 0;
    while (pageIndex < 1) {
      urLists = new ArrayList<Map<String, Object>>();
      list = util.getDataForUserToRole(pageIndex);
      if (list == null || list.isEmpty()) {
        break;
      }
      for (Object[] objs : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", objs[0]);
        map.put("userId", objs[1]);
        map.put("roleId", objs[2]);
        urLists.add(map);
      }

      util.setUserToRolesbyMybatis(urLists);

      totle += urLists.size();
      FileLogger.info("已完成" + totle);

      pageIndex++;
    }
  }

  /**
   * updateForFile
   */
  public static void updateForFile() {
    FileLogger.setLogName("logforfile");
    FileLogger.info("--------------------------FILE UPDATE------------------------------");
    FileLogger.info("开始追加或更新数据！");
    List<SlFile> list = null;
    int pageIndex = 0;
    while (true) {
      list = util.getDataForUpdateFile(pageIndex);
      if (list == null || list.isEmpty()) {
        FileLogger.info("已经没有数据需要更新了");
        break;
      }
      for (int i = 0; i < list.size(); i++) {
        SlFile file = list.get(i);
        AdvertFile adFile = new AdvertFile(file);
        util.saveOrUpdateFile(adFile);
      }

      FileLogger.info("已完成" + (list.size() + DataUtil.PAGESIZE * pageIndex) + "条");

      pageIndex++;
    }

  }

  /**
   * updateForUser
   */
  public static void updateForUser() {
    FileLogger.setLogName("logforuser");
    FileLogger.info("--------------------------USER(USER-CITY、USER-ROLE) UPDATE------------------------------");
    FileLogger.info("开始追加或更新数据！");
    List<SlSysUser> list = null;
    int pageIndex = 0;
    while (true) {
      list = util.getDataForUpdateUser(pageIndex);
      if (list == null || list.isEmpty()) {
        FileLogger.info("已经没有数据需要更新了");
        break;
      }
      for (int i = 0; i < list.size(); i++) {
        SlSysUser user = list.get(i);
        SysUser adUser = new SysUser(user);
        util.saveOrUpdateUser(adUser);
        util.saveOrUpdateUserToCity(adUser);
        util.saveOrUpdateUserToRole(adUser);
      }

      FileLogger.info("已完成" + (list.size() + DataUtil.PAGESIZE * pageIndex) + "条");

      pageIndex++;
    }

  }

}
