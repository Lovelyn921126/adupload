package com.fang.utils.web;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 查询参数
 *
 * @author wangzhiyuan
 */
public class PageQuery extends LinkedHashMap<String, Object> {

  private static final long serialVersionUID = 1L;

  /**
   * 当前页码
   */
  private int page;

  /**
   * 每页条数
   */
  private int limit;

  /**
   * 排序列
   */
  private String sort;

  /**
   * 排序规则
   */
  private String order;

  /**
   * 排序列
   */
  private String orderby;

  /**
   * 参数构造
   *
   * @param params
   *        params
   */
  public PageQuery(Map<String, Object> params) {
    this.putAll(params);

    // 分页参数
    this.page = Integer.parseInt(params.get("page").toString());
    this.limit = Integer.parseInt(params.get("limit").toString());
    this.sort = StringUtils.defaultIfBlank(ObjectUtils.anyNotNull(params.get("sort")) ? params.get("sort").toString() : "", StringUtils.EMPTY);
    this.order = StringUtils.defaultIfBlank(ObjectUtils.anyNotNull(params.get("order")) ? params.get("order").toString() : "", StringUtils.EMPTY);
    this.sort = this.sort.toLowerCase().equals("undefined") ? "" : this.sort;
    this.order = this.order.toLowerCase().equals("undefined") ? "" : this.order;
    this.orderby = sort.length() == 0 || order.length() == 0 ? StringUtils.EMPTY : MessageFormat.format("{0}.{1}", sort, order);
    this.put("offset", (page - 1) * limit);
    this.put("page", page);
    this.put("limit", limit);
    this.put("orderby", orderby);
  }

  /**
   * getPage
   *
   * @return page
   */
  public int getPage() {
    return page;
  }

  /**
   * setPage
   *
   * @param page
   *        set page
   */
  public void setPage(int page) {
    this.page = page;
  }

  /**
   * getLimit
   *
   * @return limit
   */
  public int getLimit() {
    return limit;
  }

  /**
   * setLimit
   *
   * @param limit
   *        set limit
   */
  public void setLimit(int limit) {
    this.limit = limit;
  }

  /**
   * getSort
   *
   * @return sort
   */
  public String getSort() {
    return sort;
  }

  /**
   * setSort
   *
   * @param sort
   *        set sort
   */
  public void setSort(String sort) {
    this.sort = sort;
  }

  /**
   * getOrder
   *
   * @return order
   */
  public String getOrder() {
    return order;
  }

  /**
   * setOrder
   *
   * @param order
   *        set order
   */
  public void setOrder(String order) {
    this.order = order;
  }

  /**
   * getOrderby
   *
   * @return orderby
   */
  public String getOrderby() {
    return orderby;
  }

  /**
   * setOrderby
   *
   * @param orderby
   *        set orderby
   */
  public void setOrderby(String orderby) {
    this.orderby = orderby;
  }

}
