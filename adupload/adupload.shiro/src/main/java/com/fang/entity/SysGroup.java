/**
 * File：SysGroup.java
 * Package：com.fang.adupload.entity.po
 * Author：wangzhiyuan
 * Date：2017年3月24日 下午3:43:20
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.entity;

import java.io.Serializable;

/**
 * 集团
 *
 * @author wangzhiyuan
 */
public class SysGroup extends BaseEntity implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 6383435278527837996L;

  /**
   * 集团ID
   */
  private Long groupId;

  /**
   * 父集团ID，一级菜单为0
   */
  private Long parentId;

  /**
   * 集团名称
   */
  private String name;

  /**
   * getGroupId
   *
   * @return groupId
   */
  public Long getGroupId() {
    return groupId;
  }

  /**
   * setGroupId
   *
   * @param groupId
   *        set groupId
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * getParentId
   *
   * @return parentId
   */
  public Long getParentId() {
    return parentId;
  }

  /**
   * setParentId
   *
   * @param parentId
   *        set parentId
   */
  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  /**
   * getName
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * setName
   *
   * @param name
   *        set name
   */
  public void setName(String name) {
    this.name = name;
  }

}
