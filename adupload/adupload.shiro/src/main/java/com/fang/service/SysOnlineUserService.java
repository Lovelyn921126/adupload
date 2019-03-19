/**
 * File：OnlineUserService.java
 * Package：com.fang.adupload.service
 * Author：wangzhiyuan
 * Date：2017年3月27日 上午9:21:36
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;

import com.fang.entity.vo.OnlineUser;

/**
 * 在线用户服务层
 *
 * @author wangzhiyuan
 */
public interface SysOnlineUserService {

  /**
   * 获取所有在线用户的登录信息
   *
   * @return 在线用户的登录信息
   */
  List<OnlineUser> queryAll();
}
