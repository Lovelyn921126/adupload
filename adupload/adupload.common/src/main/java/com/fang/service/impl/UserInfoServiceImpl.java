/**
 * File：UserInfoServiceImpl.java
 * Package：com.fang.coupon.service.userInfo
 * Author："wangzhiyuan"
 * Date：2016年9月26日 下午4:09:27
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fang.dao.UserInfoDao;
import com.fang.entity.UserInfo;
import com.fang.service.UserInfoService;
import com.fang.utils.IdUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;

/**
 * 用户相关服务层实现
 *
 * @author wangzhiyuan
 */
public class UserInfoServiceImpl implements UserInfoService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public UserInfo getUserByID(String userid) {
    return dynamicSqlSessionTemplate.getMapper(UserInfoDao.class).getUserByID(userid);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
  public UserInfo addUser(String userid, String username, String mobilephone, String email) {
    UserInfo user = null;
    user = dynamicSqlSessionTemplate.getMapper(UserInfoDao.class).getUserByID(userid);
    if (user == null) {
      user = new UserInfo();
      user.setCreateTime(DateTime.now().toDate());
      user.setEmail(email);
      user.setId(IdUtil.newGuid());
      user.setIsDelete(0);
      user.setMobile(mobilephone);
      user.setName(username);
      user.setUserId(userid);
      boolean re = dynamicSqlSessionTemplate.getMapper(UserInfoDao.class).addUser(user) > 0;
      if (!re) {
        user = null;
      }
    }

    return user;
  }

  @Override
  public boolean existsByUserId(String userid) {
    return dynamicSqlSessionTemplate.getMapper(UserInfoDao.class).existsByUserId(userid) > 0;
  }
}
