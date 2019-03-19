/**
 * File：Program.java
 * Package：com.fang.main
 * Author：yaokaibao
 * Date：2017年4月21日 上午10:47:23
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fang.util.FileLogger;

/**
 * Program
 *
 * @author yaokaibao
 */
public class Program {

  /**
   * main
   *
   * @param args
   *        args
   */
  public static void main(String[] args) {
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      Date begin = new Date();
      FileLogger.info("Data move start time " + format.format(begin));
      // Move (once)
      // DataMove.moveForFile();
      // DataMove.moveForUser();
      // DataMove.moveForCity();
      // DataMove.moveForUserToCity();
      // DataMove.moveForUserToRole();

      // Update (per day)
      // DataMove.updateForFile();
      DataMove.updateForUser();
      Date end = new Date();
      FileLogger.info("Data move end time " + format.format(end));
      FileLogger.info("起始时间：" + format.format(begin));
      FileLogger.info("结束时间：" + format.format(end));
    } catch (Exception ex) {
      FileLogger.error(ex);
    }
  }

  /*
   * sql 去重（将城市名称相同的ID较大的替换为ID较小的）
   *
   * update advert_file set city_id=58 where city_id=235;
   * update advert_file set city_id=188 where city_id=231;
   * update advert_file set city_id=127 where city_id=211;
   * update advert_file set city_id=149 where city_id=229;
   * update advert_file set city_id=129 where city_id=233;
   * update advert_file set city_id=168 where city_id=228;
   * update advert_file set city_id=147 where city_id=230;
   * update advert_file set city_id=93 where city_id=234;
   *
   * update sys_user_city set city_id=58 where city_id=235;
   * update sys_user_city set city_id=188 where city_id=231;
   * update sys_user_city set city_id=127 where city_id=211;
   * update sys_user_city set city_id=149 where city_id=229;
   * update sys_user_city set city_id=129 where city_id=233;
   * update sys_user_city set city_id=168 where city_id=228;
   * update sys_user_city set city_id=147 where city_id=230;
   * update sys_user_city set city_id=93 where city_id=234;
   */
}
