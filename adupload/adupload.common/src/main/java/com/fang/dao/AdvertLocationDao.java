/**
 * File：AdvertLocation.java
 * Package：com.fang.dao
 * Author：yaokaibao
 * Date：2017年4月17日 上午11:38:45
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import java.util.List;

import com.fang.entity.vo.AdvertLocationVo;

/**
 * AdvertLocation
 *
 * @author yaokaibao
 */
public interface AdvertLocationDao extends BaseDao<AdvertLocationVo> {

  /**
   * 查询指定频道下是否存在广告位
   *
   * @param channelId
   *        channelId
   * @return int
   */
  int existsByChannelId(Integer channelId);

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
