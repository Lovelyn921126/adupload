package com.fang.utils.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fang.utils.CLogger;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.exception.CommonException;

/**
 * 返回数据
 *
 * @author wangzhiyuan
 */
public class RJsonp extends R {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 成功处理
   *
   * @param jsonpCallback
   *        jsonpCallback
   * @param data
   *        data
   * @param response
   *        response
   * @return 调用response。写入流
   */
  public static RJsonp success(String jsonpCallback, Map<String, Object> data, HttpServletResponse response) {
    R r = R.ok().put("result", "success");
    if (data != null) {
      r.putAll(data);
    }
    WebUtil.sendJsonResponse(jsonpCallback + "(" + JsonXmlObjUtils.obj2json(r) + ")", response);
    return null;
  }

  /**
   * 失败处理
   *
   * @param jsonpCallback
   *        jsonpCallback
   * @param msg
   *        msg
   * @param response
   *        response
   * @return 调用response。写入流
   */
  public static RJsonp error(String jsonpCallback, String msg, HttpServletResponse response) {
    R r = R.error(msg).put("result", "fail");
    WebUtil.sendJsonResponse(jsonpCallback + "(" + JsonXmlObjUtils.obj2json(r) + ")", response);
    return null;
  }

  /**
   * 失败处理
   *
   * @param jsonpCallback
   *        jsonpCallback
   * @param e
   *        e
   * @param response
   *        response
   * @return 调用response。写入流
   */
  public static RJsonp error(String jsonpCallback, Exception e, HttpServletResponse response) {
    String msg = "系统异常";
    if (e instanceof CommonException) {
      msg = e.getMessage();
    } else {
      CLogger.error(e);
    }
    R r = R.error(msg).put("result", "fail");
    WebUtil.sendJsonResponse(jsonpCallback + "(" + JsonXmlObjUtils.obj2json(r) + ")", response);
    return null;
  }
}
