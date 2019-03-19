package com.fang.controller;

import com.fang.annotation.SysLog;
import com.fang.entity.vo.AdvertChannelVo;
import com.fang.service.AdvertChannelService;
import com.fang.service.AdvertLocationService;
import com.fang.utils.lang.time.DateTime;
import com.fang.utils.web.PageQuery;
import com.fang.utils.web.PageUtil;
import com.fang.utils.web.R;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 广告频道管理
 *
 * @author wangbingyuan
 */
@RestController
@RequestMapping("/advert/sys/channel")
public class AdvertChannelController extends AbstractController {

  /**
   * advertChannelService
   */
  @Autowired
  private AdvertChannelService advertChannelService;

  /**
   * advertLocationService
   */
  @Autowired
  private AdvertLocationService advertLocationService;

  /**
   * 加载频道列表
   *
   * @param params
   *        params
   * @return R
   */
  @RequestMapping("/list")
  @RequiresPermissions("advert:sys:channel:list")
  public R list(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    PageQuery query = new PageQuery(params);

    String order = query.getOrderby().length() == 0 ? "create_time.desc" : query.getOrderby();
    PageBounds pageBounds = new PageBounds(query.getPage(), query.getLimit(), Order.formString(order), true);
    PageList<AdvertChannelVo> channelList = (PageList<AdvertChannelVo>) advertChannelService.queryList(query, pageBounds);
    PageUtil pageUtil = new PageUtil(channelList, channelList.getPaginator().getTotalCount(), query.getLimit(), query.getPage());
    return R.ok().put("page", pageUtil);

  }

  /**
   * 下拉列表
   *
   * @return R
   */
  @RequestMapping("/select")
  // @RequiresPermissions("advert:sys:channel:select")
  public R select() {

    List<AdvertChannelVo> list = advertChannelService.select();
    return R.ok().put("list", list);

  }

  /**
   * 根据id查询
   *
   * @param id
   *        id
   * @return R
   */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Integer id) {
    AdvertChannelVo info = advertChannelService.queryObject(id);
    return R.ok().put("channel", info);
  }

  /**
   * 保存频道
   *
   * @param channel
   *        channel
   * @return r
   */
  @SysLog("保存频道")
  @RequestMapping("/save")
  public R save(@RequestBody AdvertChannelVo channel) {

    AdvertChannelVo info = advertChannelService.getByName(channel.getName());
    if (info == null) {
      channel.setCreateTime(DateTime.now().toDate());
      advertChannelService.save(channel);
    } else {
      if (info.getIsDelete() == 1) {
        return R.error("此频道名已经存在！但是已经被禁用，可以尝试去修改这个频道");
      } else {
        return R.error("频道名已存在");
      }
    }
    return R.ok();
  }

  /**
   * 修改频道
   *
   * @param channel
   *        channel
   * @return R
   */
  @SysLog("修改频道")
  @RequestMapping("/update")
  public R update(@RequestBody AdvertChannelVo channel) {

    channel.setUpdateTime(DateTime.now().toDate());
    advertChannelService.update(channel);
    return R.ok();
  }

  /**
   * 删除用户
   *
   * @param ids
   *        ids
   * @return R
   */
  @SysLog("删除频道")
  @RequestMapping("/delete")
  public R delete(@RequestBody Integer[] ids) {

    List<Integer> list = Arrays.asList(ids);
    List<Integer> delIds = new ArrayList<>(list);
    List<Integer> useIds = new ArrayList<>();
    for (Integer id : ids) {
      if (advertLocationService.existsByChannelId(id)) {
        useIds.add(id);
      }
    }
    delIds.removeAll(useIds);
    if (!delIds.isEmpty()) {
      advertChannelService.deleteBatch(delIds.toArray(new Integer[] {}));
    }

    return R.ok().put("prompt", useIds.isEmpty() ? null : 1);
  }
}
