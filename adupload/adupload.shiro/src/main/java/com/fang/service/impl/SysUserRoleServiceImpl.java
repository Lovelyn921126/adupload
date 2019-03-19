package com.fang.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fang.dao.SysUserRoleDao;
import com.fang.entity.SysUserRole;
import com.fang.service.SysUserRoleService;
import com.fang.utils.IdUtil;
import com.fang.utils.orm.mybatis.DynamicSqlSessionTemplate;

/**
 * 用户与角色对应关系
 *
 * @author wangzhiyuan
 */
public class SysUserRoleServiceImpl implements SysUserRoleService {

  /**
   * SqlSessionTemplate
   */
  @Autowired
  private DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

  @Override
  public void saveOrUpdate(String userId, List<String> roleIdList) {
    if (roleIdList.size() == 0) {
      return;
    }

    // 先删除用户与角色关系
    dynamicSqlSessionTemplate.getMapper(SysUserRoleDao.class).delete(userId);

    // 保存用户与角色关系

    // Map<String, Object> map = new HashMap<>();
    // map.put("id", IdUtil.newGuid());
    // map.put("userId", userId);
    // map.put("roleIdList", roleIdList);

    List<SysUserRole> list = new ArrayList<SysUserRole>();
    for (String roleId : roleIdList) {
      SysUserRole sysUserRole = new SysUserRole();
      sysUserRole.setId(IdUtil.newGuid());
      sysUserRole.setUserId(userId);
      sysUserRole.setRoleId(roleId);
      list.add(sysUserRole);
    }

    dynamicSqlSessionTemplate.getMapper(SysUserRoleDao.class).save(list);
  }

  @Override
  public List<String> queryRoleIdList(String userId) {
    return dynamicSqlSessionTemplate.getMapper(SysUserRoleDao.class).queryRoleIdList(userId);
  }

  @Override
  public void delete(String userId) {
    dynamicSqlSessionTemplate.getMapper(SysUserRoleDao.class).delete(userId);
  }
}
