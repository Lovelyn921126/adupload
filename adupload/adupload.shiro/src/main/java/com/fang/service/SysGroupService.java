/**
 * File：SysGroupService.java
 * Package：com.fang.service
 * Author：wangzhiyuan
 * Date：2017年4月10日 下午4:15:14
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;
import java.util.Map;

import com.fang.entity.SysGroup;
import com.fang.entity.vo.SysGroupVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 系统集团
 *
 * @author wangzhiyuan
 */
public interface SysGroupService {

  /**
   * 查询全部集团信息
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return 集团列表
   */
  List<SysGroup> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * queryObject
   *
   * @param groupId
   *        groupId
   * @return SysGroup
   */
  SysGroup queryObject(String groupId);

  /**
   * save
   *
   * @param groupVo
   *        groupVo
   */
  void save(SysGroupVo groupVo);

  /**
   * update
   *
   * @param groupVo
   *        groupVo
   */
  void update(SysGroupVo groupVo);

  /**
   * deleteBatch
   *
   * @param menuIds
   *        menuIds
   */
  void deleteBatch(String[] menuIds);
}
