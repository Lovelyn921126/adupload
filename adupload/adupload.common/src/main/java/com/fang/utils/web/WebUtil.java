/**
 * File：WebUtil.java
 * Package：com.fang.framework.web.servlet
 * Author：wangzhiyuan
 * Date：2016年7月21日 下午2:56:20
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fang.utils.exception.CommonException;
import com.fang.utils.lang.CharsetsUtil;
import com.fang.utils.lang.MimeUtil;

/**
 *

 */
/**
 * <pre>
 * WebUtil
 * 1 提供发送Json字符串到前端的方法
 * 自动将对象或数组转换成Json格式的字符串
 * 2 提供发送错误信息到浏览器的方法，格式为json、xml、html
 * </pre>
 *
 * @author wangzhiyuan
 *
 */
public class WebUtil {

  /**
   * 发送html(默认UTF-8编码)
   *
   * @param html
   *        html
   * @param response
   *        response
   */
  public static void sendHTMLResponse(final String html, final HttpServletResponse response) {
    sendResponse(html, MimeUtil.TEXT, response);
  }

  /**
   * 发送html
   *
   * @param html
   *        html文本
   * @param contentEncoding
   *        contentEncoding
   * @param response
   *        response
   */
  public static void sendHTMLResponse(final String html, final String contentEncoding, final HttpServletResponse response) {
    sendResponse(html, MimeUtil.TEXT, contentEncoding, response);
  }

  /**
   * 发送json(默认UTF-8编码)
   *
   * @param json
   *        json文本
   * @param response
   *        response
   */
  public static void sendJsonResponse(final String json, final HttpServletResponse response) {
    sendResponse(json, MimeUtil.JSON, response);
  }

  /**
   * 发送jsonp
   *
   * @param json
   *        json文本
   * @param contentEncoding
   *        contentEncoding
   * @param response
   *        response
   */
  public static void sendJsonpResponse(final String json, final String contentEncoding, final HttpServletResponse response) {
    sendResponse(json, MimeUtil.TEXT, contentEncoding, response);
  }

  /**
   * 发送jsonp(默认UTF-8编码)
   *
   * @param json
   *        json文本
   * @param response
   *        response
   */
  public static void sendJsonpResponse(final String json, final HttpServletResponse response) {
    writeJsonp(json, CharsetsUtil.UTF_8, response);
  }

  /**
   * 发送json
   *
   * @param json
   *        json文本
   * @param contentEncoding
   *        contentEncoding
   * @param response
   *        response
   */
  public static void sendJsonResponse(final String json, final String contentEncoding, final HttpServletResponse response) {
    writeJsonp(json, contentEncoding, response);
  }

  /**
   * 发送xml(默认UTF-8编码)
   *
   * @param xml
   *        xml文本
   * @param response
   *        response
   */
  public static void sendXmlResponse(final String xml, final HttpServletResponse response) {
    sendResponse(xml, MimeUtil.XML, response);
  }

  /**
   * 发送xml
   *
   * @param xml
   *        xml文本
   * @param contentEncoding
   *        contentEncoding
   * @param response
   *        response
   */
  public static void sendXmlResponse(final String xml, final String contentEncoding, final HttpServletResponse response) {
    sendResponse(xml, MimeUtil.XML, contentEncoding, response);
  }

  /**
   * 发送内容到浏览器(默认UTF-8编码)
   *
   * @param context
   *        context
   * @param contentType
   *        contentType
   * @param response
   *        response
   */
  public static void sendResponse(final String context, final String contentType, final HttpServletResponse response) {
    write(context, contentType, CharsetsUtil.UTF_8, response);
  }

  /**
   * 发送内容到浏览器
   *
   * @param context
   *        context
   * @param contentType
   *        contentType
   * @param contentEncoding
   *        contentEncoding
   * @param response
   *        response
   */
  public static void sendResponse(final String context, final String contentType, final String contentEncoding, final HttpServletResponse response) {
    write(context, contentType, contentEncoding, response);
  }

  /**
   * 将字符串写到response writer流中
   *
   * @param context
   *        context
   * @param contentType
   *        contentType
   * @param contentEncoding
   *        contentEncoding
   * @param response
   *        response
   */
  private static void write(final String context, final String contentType, final String contentEncoding, final HttpServletResponse response) {
    PrintWriter writer = null;
    try {
      response.setContentType(contentType);
      response.setCharacterEncoding(contentEncoding);
      writer = response.getWriter();
      writer.write(context);
    } catch (IOException e) {
      throw new CommonException(e);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  /**
   * 将字符串写到response writer流中
   *
   * @param context
   *        context
   * @param contentEncoding
   *        contentEncoding
   * @param response
   *        response
   */
  private static void writeJsonp(final String context, final String contentEncoding, final HttpServletResponse response) {
    PrintWriter writer = null;
    try {
      response.setCharacterEncoding(contentEncoding);
      response.setContentType("text/plain");
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
      response.setHeader("Access-Control-Allow-Credentials", "true");
      response.setHeader("Access-Control-Allow-Headers",
          "Origin,Accept-Language,Accept-Encoding,X-Forwarded-For,Connection,Accept,User-Agent,Host,Referer,Cookie,Content-Type,Cache-Control,*"); // 添加跨域访问
      writer = response.getWriter();
      writer.write(context);
    } catch (IOException e) {
      throw new CommonException(e);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  /**
   * 获取request流中的文本(默认UTF-8编码)
   *
   * @param request
   *        HttpServletRequest
   * @return request流中的文本
   * @throws Exception
   *         Exception
   */
  public static String getRequestContent(HttpServletRequest request) throws Exception {
    String content = "";
    InputStream inputStream = request.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CharsetsUtil.UTF_8));
    String tempStr = "";
    while ((tempStr = reader.readLine()) != null) {
      content += tempStr;
    }
    return content;
  }

  /**
   * 获取request流中的文本
   *
   * @param request
   *        HttpServletRequest
   * @param charset
   *        charsetName
   * @return request流中的文本
   * @throws Exception
   *         Exception
   */
  public static String getRequestContent(HttpServletRequest request, String charset) throws Exception {
    String content = "";
    InputStream inputStream = request.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
    String tempStr = "";
    while ((tempStr = reader.readLine()) != null) {
      content += tempStr;
    }
    return content;
  }
}
