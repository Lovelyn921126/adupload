package com.fang.dao;

import java.util.List;

import com.fang.entity.vo.AdvertChannelVo;

/**
 * 广告频道dao
 *
 * @author wangbingyuan
 */
public interface AdvertChannelDao extends BaseDao<AdvertChannelVo> {

  /**
   * 根据频道名查询
   *
   * @param name
   *        name
   * @return AdvertChannelVo
   */
  AdvertChannelVo getByName(String name);

  /**
   * 下拉列表
   *
   * @return List<AdvertChannelVo>
   */
  List<AdvertChannelVo> select();

}
