package com.fang.controller;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fang.annotation.SysLog;
import com.fang.common.ShiroConfig;
import com.fang.entity.SysUser;
import com.fang.entity.vo.SysUserVo;
import com.fang.service.SysUserCityService;
import com.fang.service.SysUserRoleService;
import com.fang.service.SysUserService;
import com.fang.utils.IdUtil;
import com.fang.utils.RefUtil;
import com.fang.utils.ShiroUtil;
import com.fang.utils.SsoUtil;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.fang.validator.ValidatorUtil;
import com.fang.validator.group.AddGroup;
import com.fang.validator.group.UpdateGroup;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {

  /**
   * sysUserService
   */
  @Autowired
  private SysUserService sysUserService;

  /**
   * sysUserRoleService
   */
  @Autowired
  private SysUserRoleService sysUserRoleService;

  /**
   * sysCityService
   */
  @Autowired
  private SysUserCityService sysUserCityService;

  /**
   * 注入单点登录
   */
  @Autowired
  private SsoUtil ssoUtil;

  /**
   * 获取登录的用户信息
   *
   * @return info
   */
  @RequestMapping("/info")
  public R info() {
    SysUserVo user = new SysUserVo(getUser());
    user.setIsHost(isHost());
    return R.ok().put("user", user);
  }

  /**
   * 所有用户列表
   *
   * @param params
   *        params
   * @return list
   */
  @RequestMapping("/list")
  @RequiresPermissions("sys:user:list")
  public R list(@RequestParam Map<String, Object> params) {
    // 只有超级管理员，才能查看所有管理员列表
    if (getUserType() != 3) {
      params.put("createUserId", getUserId());
      List<Integer> cityidList = sysUserCityService.queryCityIdList(getUserId());
      if (cityidList == null || cityidList.size() == 0) {
        params.put("cityid", "-2");
      } else if (cityidList.get(0) != 0) {
        params.put("cityid", cityidList);
      }

      if (params.get("listcheck") != null && params.get("listcheck").toString().equals("true")) {
        params.put("createUserId", "");
      }

    }

    // 查询列表数据
    PageQuery query = new PageQuery(params);
    String order = query.getOrderby().length() == 0 ? "create_time.desc" : query.getOrderby();
    PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<SysUserVo> userList = (PageList<SysUserVo>) sysUserService.queryList(query, pageBounds);

    PageUtil pageUtil = new PageUtil(userList, userList.getPaginator().getTotalCount(), query.getLimit(), query.getPage());

    return R.ok().put("page", pageUtil);
  }

  /**
   * 更新用户的创建者
   *
   * @param userId
   *        userId
   * @return changecreateuser
   */
  @SysLog("修改用户创建者")
  @RequestMapping("/changecreateuser/{userId}")
  @RequiresPermissions("sys:user:update")
  public R changecreateuser(@PathVariable("userId") String userId) {
    SysUserVo user = new SysUserVo();
    user.setCreateUserId(getUserId());
    user.setUserId(userId);
    user.setUpdateTime(DateTime.now().toDate());
    sysUserService.changecreateuser(user);
    return R.ok();
  }

  /**
   * 修改登录用户密码
   *
   * @param password
   *        password
   * @param newPassword
   *        newPassword
   * @return password
   */
  @SysLog("修改密码")
  @RequestMapping("/password")
  public R password(String password, String newPassword) {
    // Assert.isBlank(newPassword, "新密码不为能空");

    // sha256加密
    password = new Sha256Hash(password).toHex();
    // sha256加密
    newPassword = new Sha256Hash(newPassword).toHex();

    // 更新密码
    int count = sysUserService.updatePassword(getUserId(), password, newPassword);
    if (count == 0) {
      return R.error("原密码不正确");
    }

    // 退出
    ShiroUtil.logout();

    return R.ok();
  }

  /**
   * 用户信息
   *
   * @param userId
   *        userId
   * @return info
   */
  @RequestMapping("/info/{userId}")
  @RequiresPermissions("sys:user:info")
  public R info(@PathVariable("userId") String userId) {
    SysUserVo user = sysUserService.queryObject(userId);

    // 获取用户所属的角色列表
    List<String> roleIdList = sysUserRoleService.queryRoleIdList(userId);
    List<Integer> cityIdList = sysUserCityService.queryCityIdList(userId);

    user.setRoleIdList(roleIdList);
    user.setCityIdList(cityIdList);

    return R.ok().put("user", user);
  }

  /**
   * 保存用户
   *
   * @param user
   *        user
   * @return save
   * @throws Exception
   *         Exception
   */
  @SysLog("保存用户")
  @RequestMapping("/save")
  @RequiresPermissions("sys:user:save")
  public R save(@RequestBody SysUserVo user) throws Exception {
    ValidatorUtil.validateEntity(user, AddGroup.class);

    SysUser oldEntity = sysUserService.queryByUserName(user.getUsername());
    if (oldEntity != null) {
      if (oldEntity.getIsDelete() == 1) {
        return R.error("此用户已经存在！但是已经被禁用，可以尝试去修改这个用户");
      }
      return R.error("此用户已经存在！" + oldEntity.getUsername());
    }

    user.setUserId(IdUtil.newGuid());
    user.setType(0);
    user.setCreateUserId(getUserId());
    user.setCreateTime(DateTime.now().toDate());

    RefUtil<String> re = new RefUtil<String>("");
    boolean result = ssoUtil.active(ShiroConfig.SSOAPIURL, ShiroConfig.SSOSERVICEID, user.getUsername(), ShiroConfig.OASIGNKEY, re);
    if (result) {
      sysUserService.save(user);
      return R.ok();
    }

    return R.error(re.get());
  }

  /**
   * 修改用户
   *
   * @param user
   *        user
   * @return update
   */
  @SysLog("修改用户")
  @RequestMapping("/update")
  @RequiresPermissions("sys:user:update")
  public R update(@RequestBody SysUserVo user) {
    ValidatorUtil.validateEntity(user, UpdateGroup.class);

    // 超级管理员
    if (getUserType() != 3) {
      user.setCreateUserId(getUserId());
    }

    user.setUpdateTime(DateTime.now().toDate());

    sysUserService.update(user);
    ShiroUtil.clear(user.getUsername());

    return R.ok();
  }

  /**
   * 删除用户
   *
   * @param userIds
   *        userIds
   * @return delete
   * @throws Exception
   *         Exception
   */
  @SysLog("删除用户")
  @RequestMapping("/delete")
  @RequiresPermissions("sys:user:delete")
  public R delete(@RequestBody String[] userIds) throws Exception {
    /*
     * if (ArrayUtils.contains(userIds, 1L)) {
     * return R.error("系统管理员不能删除");
     * }
     */

    if (ArrayUtils.contains(userIds, getUserId())) {
      return R.error("当前用户不能删除");
    }

    List<String> arr = new ArrayList<String>();

    for (String userId : userIds) {
      SysUser oldEntity = sysUserService.queryObject(userId);
      if (oldEntity != null) {
        boolean result = ssoUtil.deActive(ShiroConfig.SSOAPIURL, ShiroConfig.SSOSERVICEID, oldEntity.getUsername(), ShiroConfig.OASIGNKEY);
        if (result) {
          arr.add(userId);
        } else {
          return R.error("单点登录未注销失败，错误信息：".concat(oldEntity.getUsername()));
        }
      }
    }

    String[] strings = new String[arr.size()];
    sysUserService.deleteBatch(arr.toArray(strings));
    for (String string : arr.toArray(strings)) {
      ShiroUtil.clear(string);
    }

    return R.ok();
  }
}
