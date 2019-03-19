/**
 * File：SysUserCityServiceImpl.java
 * Package：com.fang.service.impl
 * Author：wangzhiyuan
 * Date：2017年4月11日 下午4:50:14
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.dao.SysUserCityDao;
import com.fang.entity.SysUserCity;
import com.fang.service.SysUserCityService;
import com.fang.utils.IdUtil;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;

/**
 * 用户和城市关系
 *
 * @author wangzhiyuan
 */
public class SysUserCityServiceImpl implements SysUserCityService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public List<Integer> queryCityIdList(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysUserCityDao.class).queryCityIdList(userId);
  }

  @Override
  public void saveOrUpdate(String userId, List<Integer> cityIdList) {
    if (cityIdList.size() == 0) {
      return;
    }

    // 先删除用户与角色关系
    dynamicSqlSessionTemplate.getMapper(SysUserCityDao.class).delete(userId);

    // 保存用户与角色关系
    // Map<String, Object> map = new HashMap<>();
    // map.put("id", IdUtil.newGuid());
    // map.put("userId", userId);
    // map.put("cityIdList", cityIdList);

    List<SysUserCity> list = new ArrayList<SysUserCity>();
    for (Integer cityId : cityIdList) {
      SysUserCity sysUserCity = new SysUserCity();
      sysUserCity.setId(IdUtil.newGuid());
      sysUserCity.setUserId(userId);
      sysUserCity.setCityId(cityId);
      list.add(sysUserCity);
    }

    dynamicSqlSessionTemplate.getMapper(SysUserCityDao.class).save(list);

  }

}
