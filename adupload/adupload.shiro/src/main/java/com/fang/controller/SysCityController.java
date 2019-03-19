package com.fang.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fang.annotation.SysLog;
import com.fang.entity.SysCity;
import com.fang.service.SysCityService;
import com.fang.service.SysUserCityService;
import com.fang.utils.exception.CommonException;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统城市
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/sys/city")
public class SysCityController extends AbstractController {

  /**
   * sysCityService
   */
  @Autowired
  private SysCityService sysCityService;

  /**
   * sysCityService
   */
  @Autowired
  private SysUserCityService sysUserCityService;

  /**
   * 城市列表
   *
   * @param params
   *        params
   * @return list
   */
  @RequestMapping("/list")
  @RequiresPermissions("sys:city:list")
  public R list(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    PageQuery query = new PageQuery(params);
    String order = query.getOrderby().length() == 0 ? "city_id.desc" : query.getOrderby();
    PageBounds bounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order),
        true);
    PageList<SysCity> list = (PageList<SysCity>) sysCityService.queryList(query, bounds);
    PageUtil util = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(),
        query.getPage());
    return R.ok().put("page", util);
  }

  /**
   * 城市列表
   *
   * @return select
   */
  @RequestMapping("/select")
  // @RequiresPermissions("sys:city:select")
  public R select() {
    // 查询列表数据
    List<SysCity> cityList = null;
    List<SysCity> cityListSelect = null;
    List<Integer> cityIdList = sysUserCityService.queryCityIdList(getUserId());
    // 只有超级管理员，才能查看所有城市列表
    if (cityIdList == null || cityIdList.size() == 0) {
      cityList = null;
    } else if (getUserType() == 3 || cityIdList.get(0) == 0) {
      cityList = sysCityService.select();
    } else {
      cityListSelect = sysCityService.select();
      cityList = new ArrayList<SysCity>();
      if (cityIdList.size() > 0) {
        for (Integer cityId : cityIdList) {
          for (SysCity city : cityListSelect) {
            if (city.getCityId().equals(cityId)) {
              cityList.add(city);
            }
          }
        }
      }
    }

    return R.ok().put("cityList", cityList);
  }

  /**
   * 城市
   *
   * @param cityId
   *        cityId
   * @return info
   */
  @RequestMapping("/info/{cityId}")
  // @RequiresPermissions("sys:city:info")
  public R info(@PathVariable("cityId") String cityId) {
    SysCity city = sysCityService.queryObject(cityId);
    return R.ok().put("city", city);
  }

  /**
   * 城市
   *
   * @param city
   *        city
   * @return save
   */
  @RequestMapping("/save")
  // @RequiresPermissions("sys:city:save")
  public R save(@RequestBody SysCity city) {
    verifyForm(city);
    SysCity sysCity = sysCityService.queryObjectByName(city.getName());
    if (sysCity != null) {
      if (sysCity.getIsDelete() == 1) {
        return R.error("城市已经存在！但是已经被禁用，可以尝试去修改这个城市");
      }
      return R.error("此城市已经存在！" + sysCity.getName());
    }
    sysCityService.save(city);

    return R.ok();
  }

  /**
   * 城市
   *
   * @param city
   *        city
   * @return update
   */
  @RequestMapping("/update")
  // @RequiresPermissions("sys:city:update")
  public R update(@RequestBody SysCity city) {
    verifyForm(city);

    sysCityService.update(city);

    return R.ok();
  }

  /**
   * 删除
   *
   * @param cityIds
   *        cityIds
   * @return delete
   */
  @SysLog("删除菜单")
  @RequestMapping("/delete")
  // @RequiresPermissions("sys:menu:delete")
  public R delete(@RequestBody String[] cityIds) {

    sysCityService.deleteBatch(cityIds);

    return R.ok();
  }

  /**
   * 查找全部城市个数
   *
   * @return count
   */
  @RequestMapping("/getcount")
  public R allCityCount() {
    int count = sysCityService.getAllCityCount();
    return R.ok().put("allcitycount", count);
  }

  /**
   * 验证参数是否正确
   *
   * @param city
   *        city
   */
  private void verifyForm(SysCity city) {
    if (StringUtils.isBlank(city.getName())) {
      throw new CommonException("城市名称不能为空");
    }

    if (StringUtils.isBlank(city.getCode())) {
      throw new CommonException("城市编码不能为空");
    }
  }
}
