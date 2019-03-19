/**
 * File：HitsCommentController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年6月1日 下午4:34:32
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import com.fang.common.Config;
import com.fang.entity.AdvertComment;
import com.fang.service.AdvertCommentService;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 人气点赞说一说
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/advert")
public class HitsCommentController {

  /**
   * 注入advertCommentService
   */
  @Autowired
  private AdvertCommentService advertCommentService;

  /**
   * 注入redis服务
   */
  @Autowired
  private JedisTemplate jedisTemplateR;

  /**
   * 加载说一说
   *
   * @param params
   *        params
   * @return 数据
   */
  @RequestMapping("comment/message")
  @RequiresPermissions("advert:comment:list")
  public R message(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    PageQuery query = new PageQuery(params);
    String order = query.getOrderby().length() == 0 ? "create_time.desc" : query.getOrderby();
    PageBounds bounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<AdvertComment> commentList = (PageList<AdvertComment>) advertCommentService.queryList(query, bounds);
    PageUtil pageUtil = new PageUtil(commentList, commentList.getPaginator().getTotalCount(), query.getLimit(), query.getPage());
    return R.ok().put("page", pageUtil);

  }

  /**
   * 删除
   *
   * @param commentIds
   *        commentIds
   * @return delete
   */
  @RequestMapping("comment/delete")
  @RequiresPermissions("advert:comment:list")
  public R delete(@RequestBody String[] commentIds) {
    advertCommentService.deleteBatch(commentIds);
    return R.ok();
  }

  /**
   * 获取人气点赞
   *
   * @param panoid
   *        panoid
   * @return delete
   */
  @RequestMapping("hits/pvup")
  @RequiresPermissions("advert:comment:list")
  public R hits(String panoid) {
    String key = Config.REDIS_ADVERT_KEY_HITS + panoid;
    String pv = jedisTemplateR.hget(key, "pv");
    String up = jedisTemplateR.hget(key, "up");

    Map<String, Object> data = new HashMap<>();
    data.put("pv", StringUtil.isBlank(pv) ? 0 : pv);
    data.put("up", StringUtil.isBlank(up) ? 0 : up);
    return R.ok(data);
  }

}
