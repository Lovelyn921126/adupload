package com.fang.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fang.dao.SysUserDao;
import com.fang.entity.SysUser;
import com.fang.entity.vo.SysUserVo;
import com.fang.service.SysRoleService;
import com.fang.service.SysUserCityService;
import com.fang.service.SysUserRoleService;
import com.fang.service.SysUserService;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.StringUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 系统用户
 *
 * @author wangzhiyuan
 */
public class SysUserServiceImpl implements SysUserService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  /**
   * sysUserRoleService
   */
  @Autowired
  private SysUserRoleService sysUserRoleService;

  /**
   * sysUserCityService
   */
  @Autowired
  private SysUserCityService sysUserCityService;

  /**
   * sysRoleService
   */
  @Autowired
  private SysRoleService sysRoleService;

  @Override
  public List<String> queryAllPerms(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).queryAllPerms(userId);
  }

  @Override
  public List<String> queryAllMenuId(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).queryAllMenuId(userId);
  }

  @Override
  public SysUser queryByUserName(String username) {
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).queryByUserName(username);
  }

  @Override
  public SysUserVo queryObject(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).queryObject(userId);
  }

  @Override
  public List<SysUserVo> queryList(Map<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).queryList(map, pageBounds);
  }

  @Override
  public int queryTotal(Map<String, Object> map) {
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).queryTotal(map);
  }

  @Override
  @Transactional
  public void save(SysUserVo user) {
    user.setCreateTime(DateTime.now().toDate());
    // sha256加密
    // user.setPassword(new Sha256Hash(user.getPassword()).toHex());
    user.setPassword(null);
    dynamicSqlSessionTemplate.getMapper(SysUserDao.class).save(user);

    // 检查角色是否越权
    // checkRole(user);

    // 保存用户与角色关系
    sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    // 保存用户与城市关系
    sysUserCityService.saveOrUpdate(user.getUserId(), user.getCityIdList());
  }

  @Override
  @Transactional
  public void update(SysUserVo user) {
    if (StringUtils.isBlank(user.getPassword())) {
      user.setPassword(null);
    } else {
      user.setPassword(new Sha256Hash(user.getPassword()).toHex());
    }
    dynamicSqlSessionTemplate.getMapper(SysUserDao.class).update(user);

    // 检查角色是否越权
    // checkRole(user);

    // 保存用户与角色关系
    sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    // 保存用户与城市关系
    sysUserCityService.saveOrUpdate(user.getUserId(), user.getCityIdList());
  }

  @Override
  @Transactional
  public void deleteBatch(String[] userId) {
    dynamicSqlSessionTemplate.getMapper(SysUserDao.class).deleteBatch(userId);
  }

  @Override
  public int updatePassword(String userId, String password, String newPassword) {
    Map<String, Object> map = new HashMap<>();
    map.put("userId", userId);
    map.put("password", password);
    map.put("newPassword", newPassword);
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).updatePassword(map);
  }

  @Override
  public boolean querySuperOrNot(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysUserDao.class).querySuperOrNot(userId) == 3;
  }

  /**
   * 检查角色是否越权
   *
   * @param user
   *        user
   */
  @SuppressWarnings("unused")
  private void checkRole(SysUserVo user) {
    // 如果不是超级管理员，则需要判断用户的角色是否自己创建
    if (StringUtil.isBlank(user.getCreateUserId()) || querySuperOrNot(user.getCreateUserId())) {
      return;
    }

    // 查询用户创建的角色列表
    List<String> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

    // 判断是否越权
    if (!roleIdList.containsAll(user.getRoleIdList())) {
      throw new CommonException("新增用户所选角色，不是本人创建");
    }
  }

  @Override
  @Transactional
  public void changecreateuser(SysUserVo user) {
    dynamicSqlSessionTemplate.getMapper(SysUserDao.class).changecreateuser(user);
  }

}
