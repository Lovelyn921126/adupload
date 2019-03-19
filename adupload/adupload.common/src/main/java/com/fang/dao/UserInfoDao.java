/**
 * File：UserInfo.java
 * Package：com.fang.coupon.dao.userInfo
 * Author："wangzhiyuan"
 * Date：2016年9月26日 下午4:01:23
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import com.fang.entity.UserInfo;

/**
 * 用户相关数据访问层
 *
 * @author wangzhiyuan
 */
public interface UserInfoDao {

  /**
   * 推送、用户工具类-根据用户ID获取用户信息
   *
   * @param userid
   *        用户ID
   * @return 相关用户信息
   */
  UserInfo getUserByID(String userid);

  /**
   * 用户工具类-添加用户
   *
   * @param user
   *        用户实体
   * @return 添加是否成功
   */
  int addUser(UserInfo user);

  /**
   * 用户工具类-通过UserID判断是否存在
   *
   * @param userid
   *        用户ID
   * @return Boolean
   */
  int existsByUserId(String userid);

}
