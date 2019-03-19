/**
 * File：SysUserVo.java
 * Package：com.fang.shiro.entity.vo
 * Author：wangzhiyuan
 * Date：2017年3月29日 下午5:56:01
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity.vo;

import java.util.List;

import com.fang.entity.SysUser;

/**
 * 系统用户
 *
 * @author wangzhiyuan
 */
public class SysUserVo extends SysUser {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -8702636405095110547L;

  /**
   * 角色ID列表
   */
  private List<String> roleIdList;

  /**
   * 角色ID列表
   */
  private List<Integer> cityIdList;

  /**
   * 是否管理员以及以上
   */
  private Boolean isHost;

  /**
   * 集团名称
   */
  private String groupName;

  /**
   * getRoleIdList
   *
   * @return roleIdList
   */
  public List<String> getRoleIdList() {
    return roleIdList;
  }

  /**
   * setRoleIdList
   *
   * @param roleIdList
   *        set roleIdList
   */
  public void setRoleIdList(List<String> roleIdList) {
    this.roleIdList = roleIdList;
  }

  /**
   * getGroupName
   *
   * @return groupName
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * setGroupName
   *
   * @param groupName
   *        set groupName
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  /**
   * getCityIdList
   *
   * @return cityIdList
   */
  public List<Integer> getCityIdList() {
    return cityIdList;
  }

  /**
   * setCityIdList
   *
   * @param cityIdList
   *        set cityIdList
   */
  public void setCityIdList(List<Integer> cityIdList) {
    this.cityIdList = cityIdList;
  }

  /**
   * isHost
   *
   * @return isHost
   */
  public Boolean getIsHost() {
    return isHost;
  }

  /**
   * setHost
   *
   * @param isHost
   *        set isHost
   */
  public void setIsHost(Boolean isHost) {
    this.isHost = isHost;
  }

  /**
   * 无参构造
   */
  public SysUserVo() {
    super();
  }

  /**
   * 有参构造
   *
   * @param sysUser
   *        sysUser
   */
  public SysUserVo(SysUser sysUser) {
    super.setCreateTime(sysUser.getCreateTime());
    super.setCreateUserId(sysUser.getCreateUserId());
    super.setGroupId(sysUser.getGroupId());
    super.setIsDelete(sysUser.getIsDelete());
    super.setMobile(sysUser.getMobile());
    super.setName(sysUser.getName());
    super.setOrderNum(sysUser.getOrderNum());
    super.setPassword(sysUser.getPassword());
    super.setType(sysUser.getType());
    super.setUpdateTime(sysUser.getUpdateTime());
    super.setUserId(sysUser.getUserId());
    super.setUsername(sysUser.getUsername());
  }

}
