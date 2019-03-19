/**
 * File：FileUpload.java
 * Package：com.fang.common
 * Author：yaokaibao
 * Date：2017年4月27日 上午10:34:44
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.fang.utils.IdUtil;
import com.fang.utils.JsonXmlObjUtils;
import com.fang.utils.lang.CharsetsUtil;

/**
 * FileUpload
 *
 * @author yaokaibao
 */
public class FileUpload {

  /**
   * 设定post方法获取网络资源,如果参数为null,实际上设定为get方法
   *
   * @param url
   *        网络地址
   * @param file
   *        文件
   * @return 返回读取数据
   * @throws Exception
   *         Exception
   */
  public static String upload(String url, MultipartFile file) throws Exception {
    // 换行符
    final String newLine = "\r\n";
    final String boundaryPrefix = "--";
    // 定义数据分隔线
    String boundary = IdUtil.newGuid(); // DateTime.Now.Ticks.ToString("x");
    URL u = new URL(url);
    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
    // 默认为false,post方法需要写入参数,设定true
    conn.setDoOutput(true);
    conn.setDoInput(true);
    conn.setUseCaches(false);
    // 设定post方法,默认get
    conn.setRequestMethod("POST");
    // 设置请求头参数
    conn.setRequestProperty("connection", "Keep-Alive");
    conn.setRequestProperty("Charsert", "UTF-8");
    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

    // 获得输出流
    OutputStream out = conn.getOutputStream();
    StringBuffer sb = new StringBuffer();
    sb.append(boundaryPrefix);
    sb.append(boundary);
    sb.append(newLine);
    sb.append("Content-Disposition: form-data;name=\"FileData\";filename=\"" + file.getOriginalFilename()
        + "\"" + newLine);
    // sb.append("Content-Type:application/octet-stream");
    sb.append("Content-Type:" + file.getContentType());
    sb.append(newLine);
    sb.append(newLine);
    // 将参数头的数据写入到输出流中
    out.write(sb.toString().getBytes());

    // 将文件数据写入到输出流中
    byte[] buffer = file.getBytes();
    out.write(buffer);

    // 最后添加换行
    out.write(newLine.getBytes());

    sb = new StringBuffer();
    sb.append(newLine);
    sb.append(boundaryPrefix);
    sb.append(boundary);
    sb.append(boundaryPrefix);
    sb.append(newLine);
    // 将最后数据结束线写入到输出流中
    out.write(sb.toString().getBytes());
    out.flush();
    out.close();
    conn.connect(); // 建立连接
    // 获取连接状态码
    int recode = conn.getResponseCode();

    BufferedReader reader = null;
    if (recode == 200) {
      // 从连接中获取输入流
      InputStream in = conn.getInputStream();
      // 对输入流进行封装
      reader = new BufferedReader(new InputStreamReader(in, CharsetsUtil.UTF_8));
      String line = null;
      StringBuffer responseData = new StringBuffer();
      // 从输入流中读取数据
      while ((line = reader.readLine()) != null) {
        responseData.append(line);
      }
      // 关闭输入流
      reader.close();

      // 将返回的json字符串序列化
      JSONObject model = JsonXmlObjUtils.json2JsonObject(responseData.toString());
      if (model != null) {
        return model.get("furl").toString();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Zip文件上传
   *
   * @param url
   *        网络地址
   * @param fieldName
   *        字段名
   * @param fileName
   *        字段值
   * @param contentType
   *        内容内型
   * @param filePath
   *        文件路径
   * @return 返回读取数据
   * @throws Exception
   *         Exception
   */
  public static String upload(String url, String fieldName, String fileName, String contentType, String filePath) throws Exception {
    // 换行符
    final String newLine = "\r\n";
    final String boundaryPrefix = "--";
    // 定义数据分隔线
    String boundary = IdUtil.newGuid(); // DateTime.Now.Ticks.ToString("x");
    URL u = new URL(url);
    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
    // 默认为false,post方法需要写入参数,设定true
    conn.setDoOutput(true);
    conn.setDoInput(true);
    conn.setUseCaches(false);
    // 设定post方法,默认get
    conn.setRequestMethod("POST");
    // 设置请求头参数
    conn.setRequestProperty("connection", "Keep-Alive");
    conn.setRequestProperty("Charsert", "UTF-8");
    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

    // 获得输出流
    OutputStream out = conn.getOutputStream();
    StringBuffer sb = new StringBuffer();
    sb.append(boundaryPrefix);
    sb.append(boundary);
    sb.append(newLine);
    sb.append("Content-Disposition: form-data;name=\"" + fieldName + "\";filename=\"" + fileName + "\"" + newLine);
    sb.append("Content-Type: " + contentType);
    sb.append(newLine);
    sb.append(newLine);
    // 将参数头的数据写入到输出流中
    out.write(sb.toString().getBytes());

    // 将文件数据写入到输出流中
    byte[] buffer = FileUtil.toByteArray(filePath);
    out.write(buffer);

    // 最后添加换行
    out.write(newLine.getBytes());

    sb = new StringBuffer();
    sb.append(newLine);
    sb.append(boundaryPrefix);
    sb.append(boundary);
    sb.append(boundaryPrefix);
    sb.append(newLine);
    // 将最后数据结束线写入到输出流中
    out.write(sb.toString().getBytes());
    out.flush();
    out.close();
    conn.connect(); // 建立连接
    // 获取连接状态码
    int recode = conn.getResponseCode();

    BufferedReader reader = null;
    if (recode == 200) {
      // 从连接中获取输入流
      InputStream in = conn.getInputStream();
      // 对输入流进行封装
      reader = new BufferedReader(new InputStreamReader(in, CharsetsUtil.UTF_8));
      String line = null;
      StringBuffer responseData = new StringBuffer();
      // 从输入流中读取数据
      while ((line = reader.readLine()) != null) {
        responseData.append(line);
      }
      // 关闭输入流
      reader.close();

      // 将返回的json字符串序列化
      JSONObject model = JsonXmlObjUtils.json2JsonObject(responseData.toString());
      if (model != null) {
        return responseData.toString();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
