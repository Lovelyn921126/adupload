/**
 * File：CityLimit.java
 * Package：com.fang.common
 * Author：yaokaibao
 * Date：2017年5月24日 下午2:35:56
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.service.SysUserCityService;
import com.fang.utils.ShiroUtil;

/**
 * CityLimit
 *
 * @author yaokaibao
 */
public class CityLimit {

  /**
   * sysCityService
   */
  @Autowired
  private SysUserCityService sysUserCityService;

  /**
   * 返回当前用户权限内的城市（当前用户是超管或者当前用户拥有全国城市权限时 不做限制返回空）
   *
   * @return String
   */
  public String getSysUserToCityLimit() {
    String result = "";
    List<Integer> cityIdList = sysUserCityService.queryCityIdList(ShiroUtil.getUserId());
    // 只有超级管理员，才能查看所有城市列表
    if (cityIdList == null || cityIdList.size() == 0) {
      result = "-2";
    } else if (ShiroUtil.getUser().getType() == 3 || cityIdList.get(0) == 0) {
      result = "";
    } else {
      result += "'";
      result += join("','", cityIdList);
      result += "'";
    }

    return result;
  }

  /**
   * 将一个数字集合以指定的连接符连接成一个字符串
   *
   * @param join
   *        指定的连接符
   * @param strAry
   *        数组对象
   * @return 字符串结果
   */
  public String join(String join, List<? extends Number> strAry) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < strAry.size(); i++) {
      sb.append(strAry.get(i));
      if (i < (strAry.size() - 1)) {
        sb.append(join);
      }
    }
    return sb.toString();
  }
}
