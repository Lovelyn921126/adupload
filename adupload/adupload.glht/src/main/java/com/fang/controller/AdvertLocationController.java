/**
 * File：LocationController.java
 * Package：com.fang.controller
 * Author：yaokaibao
 * Date：2017年4月14日 下午4:13:29
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fang.annotation.SysLog;
import com.fang.entity.vo.AdvertLocationVo;
import com.fang.service.AdvertLocationService;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * 广告位
 *
 * @author yaokaibao
 */
@RestController
@RequestMapping("/advert/sys/location")
public class AdvertLocationController extends AbstractController {

  /**
   * 注入sysMenuService
   */
  @Autowired
  private AdvertLocationService advertLocationService;

  /**
   * 所有广告位列表
   *
   * @param params
   *        params
   * @return list
   */
  @RequestMapping("/list")
  @RequiresPermissions("advert:sys:location:list")
  public R list(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    PageQuery query = new PageQuery(params);
    String order = query.getOrderby().length() == 0 ? "create_time.desc" : query.getOrderby();
    PageBounds bounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<AdvertLocationVo> list = (PageList<AdvertLocationVo>) advertLocationService.queryList(query, bounds);
    PageUtil util = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(), query.getPage());
    return R.ok().put("page", util);
  }

  /**
   * 广告位信息
   *
   * @param id
   *        id
   * @return info
   */
  @RequestMapping("/info/{id}")
  @RequiresPermissions("advert:sys:location:info")
  public R info(@PathVariable("id") String id) {
    AdvertLocationVo location = advertLocationService.queryObject(id);
    return R.ok().put("location", location);
  }

  /**
   * 广告位信息
   *
   * @param id
   *        channelId
   * @return list
   */
  @RequestMapping("/select")
  @RequiresPermissions("advert:sys:location:select")
  public R select(String id) {
    List<AdvertLocationVo> locations = advertLocationService.select(StringUtil.isBlank(id) ? null : id.trim());
    return R.ok().put("list", locations);
  }

  /**
   * 保存
   *
   * @param location
   *        location
   * @return save
   */
  @SysLog("保存广告位")
  @RequestMapping("/save")
  @RequiresPermissions("advert:sys:location:save")
  public R save(@RequestBody AdvertLocationVo location) {
    // 数据校验
    verifyForm(location);
    AdvertLocationVo oldEntity = advertLocationService.queryByName(location.getName());
    if (oldEntity != null) {
      if (oldEntity.getIsDelete() == 1) {
        throw new CommonException("此广告位已经存在！但是已经被禁用，可以尝试去修改这个广告位");
      } else {
        throw new CommonException("已存在同名广告位");
      }
    }
    location.setCreateTime(DateTime.now().toDate());
    location.setCreateUserId(getUserId());
    // location.setIsDelete(0);

    advertLocationService.save(location);

    return R.ok();
  }

  /**
   * 修改广告位
   *
   * @param location
   *        location
   * @return update
   */
  @SysLog("修改广告位")
  @RequestMapping("/update")
  @RequiresPermissions("advert:sys:location:update")
  public R update(@RequestBody AdvertLocationVo location) {
    // 数据校验
    verifyForm(location);

    location.setUpdateTime(DateTime.now().toDate());

    advertLocationService.update(location);

    return R.ok();
  }

  /**
   * 删除广告位
   *
   * @param ids
   *        ids
   * @return select
   */
  @SysLog("删除广告位")
  @RequestMapping("/delete")
  @RequiresPermissions("advert:sys:location:delete")
  public R delete(@RequestBody String[] ids) {

    advertLocationService.deleteBatch(ids);

    return R.ok();
  }

  /**
   * 验证参数是否正确
   *
   * @param location
   *        location
   */
  private void verifyForm(AdvertLocationVo location) {
    if (StringUtils.isBlank(location.getName())) {
      throw new CommonException("广告位名称不能为空");
    }

    if (location.getChannelId() == null) {
      throw new CommonException("频道不能为空");
    }
  }

}
