/**
 * File：AdvertFileController.java
 * Package：com.fang.controller
 * Author：yaokaibao
 * Date：2017年4月24日 下午6:18:17
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import com.fang.annotation.SysLog;
import com.fang.common.CityLimit;
import com.fang.entity.vo.AdvertFileVo;
import com.fang.service.AdvertFileService;
import com.fang.utils.IdUtil;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 广告文件
 *
 * @author yaokaibao
 */
@RestController
@RequestMapping("/advert/advert")
public class AdvertFileController extends AbstractController {

  /**
   * advertFileService
   */
  @Autowired
  private AdvertFileService advertFileService;

  /**
   * 注入cityLimit
   */
  @Autowired
  private CityLimit cityLimit;

  /**
   * 所有广告列表
   *
   * @param params
   *        params
   * @return list
   * @throws ParseException
   *         ParseException
   */
  @RequestMapping("/list")
  @RequiresPermissions("advert:advert:list")
  public R list(@RequestParam Map<String, Object> params) throws ParseException {
    verifyForm(params);
    // 查询列表数据
    PageQuery query = new PageQuery(params);
    query.put("type", getUser().getType());

    // 搜索条件中不限制城市时，需要根据当前用户的权限做限制
    if ("".equals(query.get("cityId")) || query.get("cityId") == null) {
      query.put("cityId", cityLimit.getSysUserToCityLimit());
    } else {
      query.put("cityId", "'" + query.get("cityId") + "'");
    }

    // 只查找自己创建的
    if (query.get("myOwn") != null && query.get("myOwn").toString().equals("true")) {
      query.put("uploadUsername", getUser().getUsername().replaceAll("@[a-zA-Z0-9.]*", "@"));
    }

    String order = query.getOrderby().length() == 0 ? "create_time.desc" : query.getOrderby();
    PageBounds bounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<AdvertFileVo> list = (PageList<AdvertFileVo>) advertFileService.queryListByIds(query, bounds);
    PageUtil util = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(), query.getPage());
    return R.ok().put("page", util);
  }

  /**
   * 选中文件预览
   *
   * @param params
   *        params
   * @param result
   *        result
   * @return 返回结果
   */
  @RequestMapping("/selectedlist")
  public ModelAndView selectedlist(@RequestParam Map<String, Object> params, Map<String, Object> result) {
    if (params.get("idcollection") == null || StringUtils.isBlank(params.get("idcollection").toString())) {
      return null;
    }
    try {
      String[] ids = params.get("idcollection").toString().split(",");
      List<AdvertFileVo> list = advertFileService.querySelectedlist(ids);

      result.put("adverList", JsonXmlObjUtils.obj2json(list));
      result.put("count", list.size());
    } catch (Exception e) {
      result.put("content", "你没有权限！");
    }
    return new ModelAndView("advert/selectedList.jsp", result);
  }

  /**
   * 广告信息
   *
   * @param id
   *        id
   * @return info
   */
  @RequestMapping("/info/{id}")
  @RequiresPermissions("advert:advert:info")
  public R info(@PathVariable("id") String id) {
    AdvertFileVo advert = advertFileService.queryObject(id);
    return R.ok().put("advert", advert);
  }

  /**
   * 保存
   *
   * @param advert
   *        advert
   * @return save
   */
  @SysLog("保存广告")
  @RequestMapping("/save")
  @RequiresPermissions("advert:advert:save")
  public R save(@RequestBody AdvertFileVo advert) {
    verifyForm(advert);

    advert.setId(IdUtil.newGuid());
    advert.setCreateTime(DateTime.now().toDate());
    advert.setUploadUserId(getUserId());
    advert.setUploadUsername(getUser().getUsername());
    advert.setIsDelete(0);

    advertFileService.save(advert);

    return R.ok();
  }

  /**
   * 修改
   *
   * @param advert
   *        advert
   * @return save
   */
  @SysLog("修改广告")
  @RequestMapping("/update")
  @RequiresPermissions("advert:advert:update")
  public R update(@RequestBody AdvertFileVo advert) {
    verifyForm(advert);

    advert.setUpdateTime(DateTime.now().toDate());

    advertFileService.update(advert);

    return R.ok();
  }

  /**
   * 删除广告
   *
   * @param ids
   *        ids
   * @return select
   */
  @SysLog("删除广告")
  @RequestMapping("/delete")
  @RequiresPermissions("advert:advert:delete")
  public R delete(@RequestBody String[] ids) {

    advertFileService.deleteBatch(ids);

    return R.ok();
  }

  /**
   * 验证参数
   *
   * @param advert
   *        advert
   */
  private void verifyForm(AdvertFileVo advert) {
    if (StringUtils.isBlank(advert.getProjectName())) {
      throw new CommonException("广告项目名称不能为空");
    }

  }

  /**
   * 验证参数
   *
   * @param params
   *        params
   */
  private void verifyForm(Map<String, Object> params) {
    try {
      String startDate = (String) params.get("startDate");
      if (startDate != null && !startDate.trim().equals("")) {
        startDate = DateTime.parse(startDate).toDateString();
        params.put("startDate", startDate);
      }
      String endDate = (String) params.get("endDate");

      if (endDate != null && !endDate.trim().equals("")) {
        endDate = DateTime.parse(endDate).addDays(1).toDateString();
        params.put("endDate", endDate);
      }
    } catch (ParseException ex) {
      throw new CommonException("时间格式不正确，请检查");
    }
  }

}
