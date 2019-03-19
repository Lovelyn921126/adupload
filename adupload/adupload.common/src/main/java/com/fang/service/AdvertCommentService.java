/**
 * File：AdvertCommentService.java
 * Package：com.fang.service
 * Author：wangzhiyuan
 * Date：2017年5月15日 上午11:03:45
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.service;

import java.util.List;
import java.util.Map;

import com.fang.entity.AdvertComment;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 说一说
 *
 * @author wangzhiyuan
 */
public interface AdvertCommentService {

  /**
   * 保存
   *
   * @param comment
   *        comment
   * @return 成功与否
   */
  boolean save(AdvertComment comment);

  /**
   * 查询全部列表
   *
   * @param map
   *        map
   * @param pageBounds
   *        pageBounds
   * @return 菜单列表
   */
  List<AdvertComment> queryList(Map<String, Object> map, PageBounds pageBounds);

  /**
   * 删除
   *
   * @param commentIds
   *        commentIds
   */
  void deleteBatch(String[] commentIds);

}
