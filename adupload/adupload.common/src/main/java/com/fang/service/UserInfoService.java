/**
 * File：UserInfoService.java
 * Package：com.fang.service
 * Author：wangzhiyuan
 * Date：2017年5月12日 下午6:47:42
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import com.fang.entity.UserInfo;

/**
 * 用户
 *
 * @author wangzhiyuan
 */
public interface UserInfoService {

  /**
   * 根据用户ID获取用户信息
   *
   * @param userid
   *        用户ID
   * @return 相关用户信息
   */
  UserInfo getUserByID(String userid);

  /**
   * 添加用户
   *
   * @param userid
   *        用户ID
   * @param username
   *        用户名
   * @param mobilephone
   *        用户手机号
   * @param email
   *        用户邮箱
   * @return 返回相关用户实体
   */
  UserInfo addUser(String userid, String username, String mobilephone, String email);

  /**
   * 通过UserID判断是否存在
   *
   * @param userid
   *        用户ID
   * @return Boolean
   */
  boolean existsByUserId(String userid);
}
