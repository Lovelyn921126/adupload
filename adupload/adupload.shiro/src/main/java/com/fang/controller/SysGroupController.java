package com.fang.controller;

import java.util.HashMap;
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
import com.fang.entity.SysGroup;
import com.fang.entity.vo.SysGroupVo;
import com.fang.service.SysGroupService;
import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * 系统集团
 *
 * @author wangzhiyuan
 */
@RestController
@RequestMapping("/sys/group")
public class SysGroupController extends AbstractController {

  /**
   * sysUserService
   */
  @Autowired
  private SysGroupService sysGroupService;

  /**
   * 集团列表
   *
   * @return select
   */
  @RequestMapping("/select")
  //@RequiresPermissions("sys:group:select")
  public R select() {
    Map<String, Object> map = new HashMap<>();

    // 如果不是超级管理员，则只查询自己所拥有的集团列表
    if (getUserType() != 3) {
      map.put("groupId", getUser().getGroupId());
    }
    List<SysGroup> list = sysGroupService.queryList(map, new PageBounds());

    return R.ok().put("list", list);
  }

  /**
   * 所有菜单列表
   *
   * @param params
   *        params
   * @return R
   */
  @RequestMapping("/list")
  @RequiresPermissions("sys:group:list")
  public R list(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    PageQuery query = new PageQuery(params);
    String order = query.getOrderby().length() == 0 ? "order_num.desc" : query.getOrderby();
    PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(),
        Order.formString(order), true);
    PageList<SysGroup> list = (PageList<SysGroup>) sysGroupService.queryList(query, pageBounds);
    PageUtil pageUtil = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(),
        query.getPage());

    return R.ok().put("page", pageUtil);
  }

  /**
   * 菜单信息
   *
   * @param groupId
   *        groupId
   * @return R
   */
  @RequestMapping("/info/{groupId}")
  @RequiresPermissions("sys:group:info")
  public R info(@PathVariable("groupId") String groupId) {
    SysGroup group = sysGroupService.queryObject(groupId);

    return R.ok().put("group", group);
  }

  /**
   * 保存
   *
   * @param groupVo
   *        groupVo
   * @return R
   */
  @SysLog("新增集团")
  @RequestMapping("/save")
  @RequiresPermissions("sys:group:save")
  public R save(@RequestBody SysGroupVo groupVo) {
    verifyForm(groupVo);
    groupVo.setCreateTime(DateTime.now().toDate());
    sysGroupService.save(groupVo);
    return R.ok();
  }

  /**
   * 更改
   *
   * @param groupVo
   *        groupVo
   * @return R
   */
  @SysLog("更改集团")
  @RequestMapping("/update")
  @RequiresPermissions("sys:group:update")
  public R update(@RequestBody SysGroupVo groupVo) {
    verifyForm(groupVo);
    groupVo.setUpdateTime(DateTime.now().toDate());

    sysGroupService.update(groupVo);
    return R.ok();
  }

  /**
   * 删除
   *
   * @param menuIds
   *        menuId
   * @return info
   */
  @SysLog("删除集团")
  @RequestMapping("/delete")
  @RequiresPermissions("sys:group:delete")
  public R delete(@RequestBody String[] menuIds) {

    sysGroupService.deleteBatch(menuIds);
    return R.ok();
  }

  /**
   * 验证参数
   *
   * @param groupVo
   *        groupVo
   */
  private void verifyForm(SysGroupVo groupVo) {
    if (StringUtils.isBlank(String.valueOf(groupVo.getGroupId()))) {
      throw new CommonException("集团ID不能为空");
    }
    if (StringUtils.isBlank(groupVo.getName())) {
      throw new CommonException("集团名称不能为空");
    }
    if (groupVo.getParentId() == 0) {
      groupVo.setParentId(null);
    }
  }

}
