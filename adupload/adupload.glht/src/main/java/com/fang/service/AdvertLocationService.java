/**
 * File：AdvertLocationService.java
 * Package：com.fang.service
 * Author：yaokaibao
 * Date：2017年4月14日 下午4:27:00
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;

import com.fang.entity.vo.AdvertLocationVo;
import com.fang.utils.web.PageQuery;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 广告位
 *
 * @author yaokaibao
 */
public interface AdvertLocationService {

  /**
   * 按页查询列表
   *
   * @param query
   *        query
   * @param bounds
   *        bounds
   * @return list
   */
  List<AdvertLocationVo> queryList(PageQuery query, PageBounds bounds);

  /**
   * 查询广告位
   *
   * @param id
   *        id
   * @return advertLocationVo
   */
  AdvertLocationVo queryObject(String id);

  /**
   * 保存广告位
   *
   * @param location
   *        location
   */
  void save(AdvertLocationVo location);

  /**
   * 修改广告位
   *
   * @param location
   *        location
   */
  void update(AdvertLocationVo location);

  /**
   * 批量删除广告位
   *
   * @param ids
   *        ids
   */
  void deleteBatch(String[] ids);

  /**
   * 广告位名称是否存在
   *
   * @param location
   *        location
   * @return boolean
   */
  boolean exists(AdvertLocationVo location);

  /**
   * 查询指定频道下是否存在广告位
   *
   * @param channelId
   *        channelId
   * @return boolean
   */
  boolean existsByChannelId(Integer channelId);

  /**
   * 查询指定频道下可用的广告位 频道ID为空时，查询所有
   *
   * @param id
   *        频道ID
   * @return list
   */
  List<AdvertLocationVo> select(String id);

  /**
   * 根据广告位名查寻实体
   *
   * @param name
   *        name
   * @return AdvertLocationVo
   */
  AdvertLocationVo queryByName(String name);
}
