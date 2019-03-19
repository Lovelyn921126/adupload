package com.fang.utils.web;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author wangzhiyuan
 */
public class R extends HashMap<String, Object> {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 1L;

  /**
   * R
   */
  public R() {
    put("code", 0);
  }

  /**
   * error
   *
   * @return R
   */
  public static R error() {
    return error(500, "未知异常，请联系管理员");
  }

  /**
   * error
   *
   * @param msg
   *        msg
   * @return R
   */
  public static R error(String msg) {
    return error(500, msg);
  }

  /**
   * error
   *
   * @param code
   *        code
   * @param msg
   *        msg
   * @return R
   */
  public static R error(int code, String msg) {
    R r = new R();
    r.put("code", code);
    r.put("msg", msg);
    return r;
  }

  /**
   * ok
   *
   * @param msg
   *        msg
   * @return R
   */
  public static R ok(String msg) {
    R r = new R();
    r.put("msg", msg);
    return r;
  }

  /**
   * ok
   *
   * @param map
   *        map
   * @return R
   */
  public static R ok(Map<String, Object> map) {
    R r = new R();
    r.putAll(map);
    return r;
  }

  /**
   * ok
   *
   * @return R
   */
  public static R ok() {
    return new R();
  }

  /**
   * put
   *
   * @param key
   *        key
   * @param value
   *        value
   * @return R
   */
  public R put(String key, Object value) {
    super.put(key, value);
    return this;
  }
}
