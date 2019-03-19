package com.fang.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fang.common.ShiroConfig;
import com.fang.entity.SysUser;
import com.fang.service.SysUserRoleService;
import com.fang.utils.ShiroUtil;

/**
 * Controller公共组件
 *
 * @author wangzhiyuan
 */
public abstract class AbstractController {

  /**
   * logger
   */
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * sysUserRoleService
   */
  @Autowired
  private SysUserRoleService sysUserRoleService;

  /**
   * 获得当前用户
   *
   * @return SysUser
   */
  protected SysUser getUser() {
    return ShiroUtil.getUser();
  }

  /**
   * 是否是管理员及以上角色
   *
   * @return SysUser
   */
  protected Boolean isHost() {
    List<String> roleIdList = sysUserRoleService.queryRoleIdList(getUserId());
    return getUser().getType() == 3 || roleIdList.contains(ShiroConfig.HOST_ID);
  }

  /**
   * 获得当前用户ID
   *
   * @return getUserId
   */
  protected String getUserId() {
    return getUser().getUserId();
  }

  /**
   * 获得当前用户ID
   *
   * @return getType
   */
  protected int getUserType() {
    return getUser().getType();
  }

  /**
   * cityFilter
   *
   * @param cityName
   *        cityName
   * @return cityName
   */
  protected String cityFilter(String cityName) {
    if (cityName == "漳州" || cityName == "汕头") {
      cityName = "";
    }
    return cityName;
  }

}
