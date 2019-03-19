/**
 * File：AdvertLocationVo.java
 * Package：com.fang.entity
 * Author：yaokaibao
 * Date：2017年4月14日 下午5:18:55
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity.vo;

import com.fang.entity.AdvertLocation;

/**
 * AdvertLocationVo
 *
 * @author yaokaibao
 */
public class AdvertLocationVo extends AdvertLocation {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -1711167372483110048L;

  /**
   * 频道名称
   */
  private String channelName;

  /**
   * channelName
   * @return channelName
   */
  public String getChannelName() {
    return channelName;
  }

  /**
   * channelName
   * @param channelName
   *        channelName
   */
  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }
}
