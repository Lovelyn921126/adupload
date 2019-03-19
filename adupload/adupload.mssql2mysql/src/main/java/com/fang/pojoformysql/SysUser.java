/**
 * File：SysUser.java
 * Package：com.fang.pojoformysql
 * Author：yaokaibao
 * Date：2017年5月18日 下午4:41:30
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.pojoformysql;

import java.io.Serializable;
import java.util.Date;

import com.fang.pojoformssql.SlSysUser;

/**
 * SysUser
 *
 * @author yaokaibao
 */
public class SysUser implements Serializable {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -3145759595463155121L;

  /**
   * 用户ID
   */
  private String userId;

  /**
   * 用户名(邮箱)
   */
  private String username;

  /**
   * 密码
   */
  private transient String password;

  /**
   * 用户名字（真实名字）
   */
  private String name;

  /**
   * 手机号
   */
  private String mobile;

  /**
   * 账户类型（(默认)0:需要在OA入口登录，(少量)1:不经过OA;）
   */
  private Integer type;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 更新时间
   */
  private Date updateTime;

  /**
   * 是否删除（1=删除，0=未删除）
   */
  private Integer isDelete;

  /**
   * 集团编号
   */
  private Integer groupId;

  /**
   * SysUser
   */
  public SysUser() {
    super();
    type = 0;
  }

  /**
   * SysUser
   *
   * @param user
   *        user
   */
  public SysUser(SlSysUser user) {
    super();
    userId = user.getId();
    username = user.getUsername();
    password = "";
    name = user.getName();
    mobile = user.getTeleNumber();
    type = user.getUsername().contains("fang.com") || user.getUsername().contains("sunfun.com") ? 0 : 1;
    createTime = user.getCreateTime();
    updateTime = user.getUpdateTime();
    groupId = convertGroupId(user.getBloc());
    isDelete = user.getIsDelete();
  }

  /**
   * convertGroupId
   *
   * @param bloc
   *        bloc
   * @return groupId
   */
  private Integer convertGroupId(String bloc) {
    if (bloc.contains("控股")) {
      groupId = 10000;
    } else if (bloc.contains("家居")) {
      if (bloc.contains("媒体")) {
        groupId = 20100;
      } else if (bloc.contains("电商")) {
        groupId = 20200;
      } else if (bloc.contains("平台")) {
        groupId = 20300;
      } else {
        groupId = 20000;
      }
    } else if (bloc.contains("租房")) {
      if (bloc.contains("媒体")) {
        groupId = 30100;
      } else if (bloc.contains("电商")) {
        groupId = 30200;
      } else if (bloc.contains("平台")) {
        groupId = 30300;
      } else {
        groupId = 30000;
      }
    } else if (bloc.contains("二手房")) {
      if (bloc.contains("媒体")) {
        groupId = 40100;
      } else if (bloc.contains("电商")) {
        groupId = 40200;
      } else if (bloc.contains("平台")) {
        groupId = 40300;
      } else {
        groupId = 40000;
      }
    } else if (bloc.contains("新房")) {
      if (bloc.contains("媒体")) {
        groupId = 50100;
      } else if (bloc.contains("电商")) {
        groupId = 50200;
      } else if (bloc.contains("平台")) {
        groupId = 50300;
      } else {
        groupId = 50000;
      }
    } else if (bloc.contains("研究院")) {
      groupId = 60000;
    } else if (bloc.contains("新城市")) {
      groupId = 70000;
    } else if (bloc.contains("媒体")) {
      groupId = 80000;
    } else if (bloc.contains("平台")) {
      groupId = 90000;
    } else {
      groupId = null;
    }
    return groupId;
  }

  /**
   * getUserId
   *
   * @return userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * setUserId
   *
   * @param userId
   *        set userId
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * getUsername
   *
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * setUsername
   *
   * @param username
   *        set username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * getPassword
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * setPassword
   *
   * @param password
   *        set password
   */
  public void setPassword(String password) {
    this.password = password;
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

  /**
   * getMobile
   *
   * @return mobile
   */
  public String getMobile() {
    return mobile;
  }

  /**
   * setMobile
   *
   * @param mobile
   *        set mobile
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * getType
   *
   * @return type
   */
  public Integer getType() {
    return type;
  }

  /**
   * setType
   *
   * @param type
   *        set type
   */
  public void setType(Integer type) {
    this.type = type;
  }

  /**
   * getGroupId
   *
   * @return groupId
   */
  public Integer getGroupId() {
    return groupId;
  }

  /**
   * setGroupId
   *
   * @param groupId
   *        set groupId
   */
  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  /**
   * createTime
   *
   * @return createTime
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * createTime
   *
   * @param createTime
   *        createTime
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * updateTime
   *
   * @return updateTime
   */
  public Date getUpdateTime() {
    return updateTime;
  }

  /**
   * updateTime
   *
   * @param updateTime
   *        updateTime
   */
  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  /**
   * isDelete
   *
   * @return isDelete
   */
  public Integer getIsDelete() {
    return isDelete;
  }

  /**
   * isDelete
   *
   * @param isDelete
   *        isDelete
   */
  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }
}
