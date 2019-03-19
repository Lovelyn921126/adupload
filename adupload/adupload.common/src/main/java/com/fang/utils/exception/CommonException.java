package com.fang.utils.exception;

/**
 * 自定义异常
 *
 * @author wangzhiyuan
 */
public class CommonException extends RuntimeException {

  /**
   * long
   */
  private static final long serialVersionUID = 1L;

  /**
   * msg
   */
  private String msg;

  /**
   * code
   */
  private int code = 500;

  /**
   * CommonException
   *
   * @param msg
   *        msg
   */
  public CommonException(String msg) {
    super(msg);
    this.msg = msg;
  }

  /**
   * CommonException
   *
   * @param msg
   *        msg
   * @param e
   *        e
   */
  public CommonException(String msg, Throwable e) {
    super(msg, e);
    this.msg = msg;
  }

  /**
   * CommonException
   *
   * @param msg
   *        msg
   * @param code
   *        code
   */
  public CommonException(String msg, int code) {
    super(msg);
    this.msg = msg;
    this.code = code;
  }

  /**
   * CommonException
   *
   * @param msg
   *        msg
   * @param code
   *        code
   * @param e
   *        e
   */
  public CommonException(String msg, int code, Throwable e) {
    super(msg, e);
    this.msg = msg;
    this.code = code;
  }

  /**
   * CommonException
   *
   * @param e
   *        e
   */
  public CommonException(Throwable e) {
    super(e);
  }

  /**
   * getMsg
   *
   * @return msg
   */
  public String getMsg() {
    return msg;
  }

  /**
   * setMsg
   *
   * @param msg
   *        set msg
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }

  /**
   * getCode
   *
   * @return code
   */
  public int getCode() {
    return code;
  }

  /**
   * setCode
   *
   * @param code
   *        set code
   */
  public void setCode(int code) {
    this.code = code;
  }

}
