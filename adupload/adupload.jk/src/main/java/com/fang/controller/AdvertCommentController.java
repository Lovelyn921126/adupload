/**
 * File：AdvertCommentController.java
 * Package：com.fang.controller
 * Author：wangzhiyuan
 * Date：2017年5月12日 下午9:59:13
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import com.fang.annotation.IgnoreAuth;
import com.fang.annotation.ResponseJsonp;
import com.fang.entity.AdvertComment;
import com.fang.service.AdvertCommentService;
import com.fang.utils.IdUtil;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.RJsonp;
import com.fang.utils.web.security.Check;
import com.fang.validator.Assert;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 说一说
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/api")
public class AdvertCommentController {

  /**
   * 注入advertCommentService
   */
  @Autowired
  private AdvertCommentService advertCommentService;

  /**
   * 添加说一说
   *
   * @param params
   *        params
   * @param request
   *        request
   * @param response
   *        response
   * @return 返回结果
   */
  @ResponseJsonp
  @RequestMapping("comment/savemessage")
  public RJsonp save(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
    // String panoid, String userid, String content, String avatar, String ath,String atv,
    String jsonpcallback = "jsonpcallback";
    try {
      jsonpcallback = request.getParameter("jsonpcallback"); // 客户端请求参数

      // 参数校验
      Assert.commonCheck(true, "jsonpCallback", jsonpcallback, 0, false);
      Assert.commonCheck(true, "panoid", params.get("panoid"), 0, false);
      Assert.commonCheck(true, "ath", params.get("ath"), 0, false);
      Assert.commonCheck(true, "atv", params.get("atv"), 0, false);
      Assert.commonCheck(true, "content", params.get("content"), 20, false);
      Assert.commonCheck(true, "scene", params.get("scene"), 50, false);

      if (Check.isWordsLimited(params.get("content").toString())) {
        throw new CommonException("信息中含有敏感词汇");
      }

      AdvertComment advertComment = new AdvertComment();
      advertComment.setAth(Float.parseFloat(params.get("ath").toString()));
      advertComment.setAtv(Float.parseFloat(params.get("atv").toString()));
      advertComment.setAvatar(params.get("avatar") == null ? "" : params.get("avatar").toString());
      advertComment.setContent(params.get("content").toString());
      advertComment.setCreateTime(DateTime.now().toDate());
      advertComment.setIsDelete(0);
      advertComment.setId(IdUtil.newGuid());
      advertComment.setPanoid(params.get("panoid").toString());
      advertComment.setUserid(params.get("userid").toString());
      advertComment.setScene(params.get("scene").toString());

      advertCommentService.save(advertComment);

      return RJsonp.success(jsonpcallback, null, response);
    } catch (Exception e) {
      return RJsonp.error(jsonpcallback, e, response);
    }
  }

  /**
   * 获取说一说列表
   *
   * @param params
   *        params
   * @param request
   *        request
   * @param response
   *        response
   * @return 列表
   */
  @IgnoreAuth
  @ResponseJsonp
  @RequestMapping("comment/message")
  public RJsonp pageList(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
    String jsonpcallback = "jsonpcallback";
    try {
      jsonpcallback = request.getParameter("jsonpcallback"); // 客户端请求参数

      // 参数校验
      Assert.commonCheck(true, "jsonpCallback", jsonpcallback, 0, false);
      Assert.commonCheck(true, "panoid", params.get("panoid"), 0, false);
      Assert.commonCheck(false, "page", params.get("page"), 0, true);
      Assert.commonCheck(false, "pagesize", params.get("pagesize"), 0, true);
      Assert.commonCheck(false, "scene", params.get("scene"), 0, false);

      if (params.get("page") == null) {
        params.put("page", 1);
      }
      if (params.get("pagesize") == null) {
        params.put("limit", 30);
      } else {
        int count = Integer.valueOf(params.get("pagesize").toString());
        if (count > 50 || count == 0) {
          params.put("limit", 30);
        }
        params.put("limit", params.get("pagesize"));
      }

      // 查询列表数据
      PageQuery query = new PageQuery(params);
      PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString("create_time.desc"), true);
      PageList<AdvertComment> commentList = (PageList<AdvertComment>) advertCommentService.queryList(query, pageBounds);
      PageUtil pageUtil = new PageUtil(commentList, commentList.getPaginator().getTotalCount(), query.getLimit(), query.getPage());

      Map<String, Object> data = new HashMap<>();
      data.put("totalpage", pageUtil.getTotalPage());
      data.put("current", pageUtil.getCurrPage());
      data.put("totalcount", pageUtil.getTotalCount());
      data.put("list", pageUtil.getList());

      return RJsonp.success(jsonpcallback, data, response);
    } catch (Exception e) {
      return RJsonp.error(jsonpcallback, e, response);
    }
  }

}
