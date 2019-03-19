package com.fang.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fang.dao.SysRoleDao;
import com.fang.entity.SysRole;
import com.fang.entity.vo.SysRoleVo;
import com.fang.service.SysRoleMenuService;
import com.fang.service.SysRoleService;
import com.fang.service.SysUserRoleService;
import com.fang.service.SysUserService;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 角色
 *
 * @author wangzhiyuan
 */
public class SysRoleServiceImpl implements SysRoleService {

  /**
   * sysRoleMenuService
   */
  @Autowired
  private SysRoleMenuService sysRoleMenuService;

  /**
   * sysUserRoleService
   */
  @Autowired
  private SysUserRoleService sysUserRoleService;

  /**
   * sysUserService
   */
  @Autowired
  private SysUserService sysUserService;

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public SysRole queryObject(String roleId) {
    return dynamicSqlSessionTemplate.getMapper(SysRoleDao.class).queryObject(roleId);
  }

  @Override
  public List<SysRoleVo> queryList(Map<String, Object> map, PageBounds pageBounds) {
    return dynamicSqlSessionTemplate.getMapper(SysRoleDao.class).queryList(map, pageBounds);
  }

  @Override
  public int queryTotal(Map<String, Object> map) {
    return dynamicSqlSessionTemplate.getMapper(SysRoleDao.class).queryTotal(map);
  }

  @Override
  @Transactional
  public void save(SysRoleVo role) {
    role.setCreateTime(DateTime.now().toDate());
    dynamicSqlSessionTemplate.getMapper(SysRoleDao.class).save(role);

    // 检查权限是否越权
    checkPrems(role);

    // 保存角色与菜单关系
    sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
  }

  @Override
  @Transactional
  public void update(SysRoleVo role) {
    dynamicSqlSessionTemplate.getMapper(SysRoleDao.class).update(role);

    // 检查权限是否越权
    checkPrems(role);

    // 更新角色与菜单关系
    sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
  }

  @Override
  @Transactional
  public void deleteBatch(String[] roleIds) {
    dynamicSqlSessionTemplate.getMapper(SysRoleDao.class).deleteBatch(roleIds);
  }

  @Override
  public List<String> queryRoleIdList(String createUserId) {
    return dynamicSqlSessionTemplate.getMapper(SysRoleDao.class).queryRoleIdList(createUserId);
  }

  /**
   * 检查权限是否越权
   *
   * @param role
   *        role
   */
  private void checkPrems(SysRoleVo role) {
    // 如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
    if (sysUserService.querySuperOrNot(role.getCreateUserId())) {
      return;
    }

    // 查询用户所拥有的菜单列表
    List<String> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());

    // 判断是否越权
    if (!menuIdList.containsAll(role.getMenuIdList())) {
      throw new CommonException("新增角色的权限，已超出你的权限范围");
    }
  }
}
