/**
 * File：AdvertPopularController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年5月12日 下午8:29:04
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import com.fang.annotation.IgnoreAuth;
import com.fang.annotation.ResponseJsonp;
import com.fang.common.Config;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.nosql.redis.JedisTemplate;
import com.fang.utils.web.RJsonp;
import com.fang.validator.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 人气接口
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/api")
public class AdvertLikeController {

  /**
   * 注入redis服务
   */
  @Autowired
  private JedisTemplate jedisTemplateW;

  /**
   * 注入redis服务
   */
  @Autowired
  private JedisTemplate jedisTemplateR;

  /**
   * 增加、返回人气点赞数
   *
   * @param panoid
   *        panoid
   * @param request
   *        request
   * @param response
   *        response
   * @return return
   */
  @IgnoreAuth
  @ResponseJsonp
  @RequestMapping("hits/pv")
  public RJsonp hits(String panoid, HttpServletRequest request, HttpServletResponse response) {
    String jsonpcallback = "jsonpcallback";
    try {
      jsonpcallback = request.getParameter("jsonpcallback"); // 客户端请求参数

      // 参数校验
      Assert.commonCheck(true, "jsonpCallback", jsonpcallback, 0, false);
      Assert.commonCheck(true, "panoid", panoid, 0, false);

      String key = Config.REDIS_ADVERT_KEY_HITS + panoid;

      String up = jedisTemplateR.hget(key, "up");
      Map<String, Object> data = new HashMap<>();
      data.put("pv", jedisTemplateW.hincrBy(key, "pv", 1));
      data.put("up", StringUtil.isBlank(up) ? 0 : up);

      return RJsonp.success(jsonpcallback, data, response);
    } catch (Exception e) {
      return RJsonp.error(jsonpcallback, e, response);
    }
  }

  /**
   * 增加点赞数
   *
   * @param panoid
   *        panoid
   * @param up
   *        up
   * @param request
   *        request
   * @param response
   *        response
   * @return return
   */
  @IgnoreAuth
  @ResponseJsonp
  @RequestMapping("hits/up")
  public RJsonp like(String panoid, String up, HttpServletRequest request, HttpServletResponse response) {
    String jsonpcallback = "jsonpcallback";
    try {
      jsonpcallback = request.getParameter("jsonpcallback"); // 客户端请求参数

      // 参数校验
      Assert.commonCheck(true, "jsonpCallback", jsonpcallback, 0, false);
      Assert.commonCheck(true, "panoid", panoid, 0, false);
      Assert.commonCheck(true, "up", up, 0, false);

      String key = Config.REDIS_ADVERT_KEY_HITS + panoid;
      if (up.equals("true")) {
        jedisTemplateW.hincrBy(key, "up", 1);
      }

      Map<String, Object> data = new HashMap<>();
      data.put("up", jedisTemplateR.hget(key, "up"));

      return RJsonp.success(jsonpcallback, data, response);
    } catch (Exception e) {
      return RJsonp.error(jsonpcallback, e, response);
    }
  }

}
