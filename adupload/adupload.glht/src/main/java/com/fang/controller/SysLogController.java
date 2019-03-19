package com.fang.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fang.entity.SysLog;
import com.fang.service.SysLogService;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import java.util.Map;

/**
 * 系统日志
 *
 * @author wangzhiyuan
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {

  /**
   * sysLogService
   */
  @Autowired
  private SysLogService sysLogService;

  /**
   * 列表
   *
   * @param params
   *        params
   * @return 列表
   */
  @ResponseBody
  @RequestMapping("/list")
  @RequiresPermissions("sys:log:list")
  public R list(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    PageQuery query = new PageQuery(params);
    String order = query.getOrderby().length() == 0 ? "create_date.desc" : query.getOrderby();
    PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<SysLog> list = (PageList<SysLog>) sysLogService.queryList(query, pageBounds);
    PageUtil pageUtil = new PageUtil(list, list.getPaginator().getTotalCount(), query.getLimit(), query.getPage());

    return R.ok().put("page", pageUtil);
  }

}
