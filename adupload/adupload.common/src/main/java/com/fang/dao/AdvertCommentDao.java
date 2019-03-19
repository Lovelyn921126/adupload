/**
 * File：AdvertCommentDao.java
 * Package：com.fang.dao
 * Author：wangzhiyuan
 * Date：2017年5月15日 上午11:35:32
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.dao;

import com.fang.entity.AdvertComment;

/**
 * 说一说
 *
 * @author wangzhiyuan
 */
public interface AdvertCommentDao extends BaseDao<AdvertComment> {

  /**
   * saveBack
   *
   * @param advertComment
   *        advertComment
   * @return 返回值
   */
  int saveBack(AdvertComment advertComment);
}
