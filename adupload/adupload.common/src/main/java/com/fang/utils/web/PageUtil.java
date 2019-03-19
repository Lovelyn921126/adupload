package com.fang.utils.web;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author wangzhiyuan
 */
public class PageUtil implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 总记录数
   */
  private int totalCount;

  /**
   * 每页记录数
   */
  private int pageSize;

  /**
   * 总页数
   */
  private int totalPage;

  /**
   * 当前页数
   */
  private int currPage;

  /**
   * 列表数据
   */
  private List<?> list;

  /**
   * 分页
   *
   * @param list
   *        列表数据
   * @param totalCount
   *        总记录数
   * @param pageSize
   *        每页记录数
   * @param currPage
   *        当前页数
   */
  public PageUtil(List<?> list, int totalCount, int pageSize, int currPage) {
    this.list = list;
    this.totalCount = totalCount;
    this.pageSize = pageSize;
    this.currPage = currPage;
    this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
  }

  /**
   * getTotalCount
   *
   * @return totalCount
   */
  public int getTotalCount() {
    return totalCount;
  }

  /**
   * setTotalCount
   *
   * @param totalCount
   *        set totalCount
   */
  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  /**
   * getPageSize
   *
   * @return pageSize
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * setPageSize
   *
   * @param pageSize
   *        set pageSize
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * getTotalPage
   *
   * @return totalPage
   */
  public int getTotalPage() {
    return totalPage;
  }

  /**
   * setTotalPage
   *
   * @param totalPage
   *        set totalPage
   */
  public void setTotalPage(int totalPage) {
    this.totalPage = totalPage;
  }

  /**
   * getCurrPage
   *
   * @return currPage
   */
  public int getCurrPage() {
    return currPage;
  }

  /**
   * setCurrPage
   *
   * @param currPage
   *        set currPage
   */
  public void setCurrPage(int currPage) {
    this.currPage = currPage;
  }

  /**
   * getList
   *
   * @return list
   */
  public List<?> getList() {
    return list;
  }

  /**
   * setList
   *
   * @param list
   *        set list
   */
  public void setList(List<?> list) {
    this.list = list;
  }

}
