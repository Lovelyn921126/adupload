/**
 * File：HttpClientUtil.java
 * Package：com.fang.framework.web
 * Author：wangzhiyuan
 * Date：2016年7月27日 下午2:58:25
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.fang.utils.lang.CharsetsUtil;

/**
 * HttpClientUtil
 *
 * @author wangzhiyuan
 */
public class HttpClientUtil {

  /**
   * 连接超时时间
   * 20s
   */
  private static final int CONNECT_TIMEOUT = 20000;

  /**
   * 请求超时时间
   * 20s
   */
  private static final int CONNECT_REQUEST_TIMEOUT = 20000;

  /**
   * socket超时时间
   * 20s
   */
  private static final int SOCKET_TIMEOUT = 20000;

  /**
   * 连接超时时间
   * 20s
   */
  private int connectTimeout = CONNECT_TIMEOUT;

  /**
   * 请求超时时间
   * 20s
   */
  private int connectionRequestTimeout = CONNECT_REQUEST_TIMEOUT;

  /**
   * socket超时时间
   * 20s
   */
  private int socketTimeout = SOCKET_TIMEOUT;

  /**
   * 头部文件
   */
  private List<Header> headers;

  /**
   * 获取HttpClient
   * 并配置初始化参数
   *
   * @return HttpClient
   */
  private CloseableHttpClient getHttpClient() {
    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout)
        .build();
    CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    return httpClient;
  }

  /**
   * 获取SSLHttpClient
   *
   * @return SSLHttpClient
   */
  private CloseableHttpClient getSSLHttpClient() {
    // 设置超时时间
    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout)
        .build();
    // 设置ssl
    SSLContext sslContext = SSLContexts.createSystemDefault();
    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(sslsf).build();
    return httpClient;
  }

  /**
   * 执行post或者get请求
   *
   * @param url
   *        目的地址
   * @param encoding
   *        编码
   * @param httpUriRequest
   *        HttpUriRequest
   * @param httpClient
   *        httpClient
   * @return 执行post或者get请求的返回结果
   * @throws Exception
   *         Exception
   */
  private String execute(String url, String encoding, HttpUriRequest httpUriRequest, CloseableHttpClient httpClient) throws Exception {
    CloseableHttpResponse response = null;
    try {
      if (headers != null) {
        httpUriRequest.setHeaders(headers.toArray(new Header[] {}));
      }
      response = httpClient.execute(httpUriRequest);
      HttpEntity entity = response.getEntity();
      String result = EntityUtils.toString(entity, encoding);
      return result;
    } finally {
      response.close();
      httpClient.close();
    }
  }

  /*-----------------------------------------------------------------------------*/
  /**
   * http get或者https get
   * 通用方法
   *
   * @param url
   *        目的地址
   * @param encoding
   *        编码
   * @param httpClient
   *        httpClient
   * @return 执行get请求的返回结果
   * @throws Exception
   *         Exception
   */
  private String get(String url, String encoding, CloseableHttpClient httpClient) throws Exception {
    HttpGet httpget = new HttpGet(url);
    return execute(url, encoding, httpget, httpClient);
  }

  /**
   * http post或者https post
   *
   * @param url
   *        目的地址
   * @param nvps
   *        A name / value pair parameter list used as an element of HTTP messages.
   * @param encoding
   *        编码
   * @param httpClient
   *        httpClient
   * @return 执行post请求的返回结果
   * @throws Exception
   *         Exception
   */
  private String post(String url, List<NameValuePair> nvps, String encoding, CloseableHttpClient httpClient) throws Exception {
    HttpPost httpPost = new HttpPost(url);
    httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
    return execute(url, encoding, httpPost, httpClient);
  }

  /*-----------------------------------------------------------------------------*/
  /**
   * <pre>
   * http get请求
   * </pre>
   *
   * @param url
   *        目的地址
   * @param encoding
   *        编码
   * @return 执行get请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String get(String url, String encoding) throws Exception {
    CloseableHttpClient httpClient = getHttpClient();
    return get(url, encoding, httpClient);
  }

  /**
   * http get请求
   * 默认utf-8编码
   *
   * @param url
   *        目的地址
   * @return 执行get请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String get(String url) throws Exception {
    return get(url, CharsetsUtil.UTF_8);
  }

  /*-----------------------------------------------------------------------------*/
  /**
   * ssl get请求
   *
   * @param url
   *        目的地址
   * @param encoding
   *        编码
   * @return 执行ssl get请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String getSSL(String url, String encoding) throws Exception {
    CloseableHttpClient httpClient = getSSLHttpClient();
    return get(url, encoding, httpClient);
  }

  /**
   * ssl get请求
   * 默认utf-8编码
   *
   * @param url
   *        目的地址
   * @return 执行ssl get请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String getSSL(String url) throws Exception {
    return getSSL(url, CharsetsUtil.UTF_8);
  }

  /*-----------------------------------------------------------------------------*/
  /**
   * http post请求
   *
   * <pre>
   * List&lt;NameValuePair&gt; nvps = new ArrayList&lt;NameValuePair&gt;();
   * nvps.add(new BasicNameValuePair(&quot;user.userName&quot;, &quot;哈哈&quot;));
   * HttpClientUtil.post(&quot;http://localhost:8080/login/login.do&quot;, nvps, &quot;utf-8&quot;);
   * </pre>
   *
   * @param url
   *        目的地址
   * @param nvps
   *        A name / value pair parameter list used as an element of HTTP messages.
   * @param encoding
   *        编码
   * @return 执行post请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String post(String url, List<NameValuePair> nvps, String encoding) throws Exception {
    CloseableHttpClient httpClient = getHttpClient();
    return post(url, nvps, encoding, httpClient);
  }

  /**
   * http post请求
   * 默认utf-8编码
   *
   * <pre>
   * List&lt;NameValuePair&gt; nvps = new ArrayList&lt;NameValuePair&gt;();
   * nvps.add(new BasicNameValuePair(&quot;user.userName&quot;, &quot;哈哈&quot;));
   * HttpClientUtil.doPost(&quot;http://localhost:8080/login/login.do&quot;, nvps);
   * </pre>
   *
   * @param url
   *        目的地址
   * @param nvp
   *        A name / value pair parameter list used as an element of HTTP messages.
   * @return 执行post请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String post(String url, List<NameValuePair> nvp) throws Exception {
    return post(url, nvp, CharsetsUtil.UTF_8);
  }

  /**
   * post文本
   * 默认编码UTF_8
   *
   * @param url
   *        目的地址
   * @param content
   *        文本
   * @return 执行post请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String post(String url, String content) throws Exception {
    CloseableHttpClient httpClient = getHttpClient();
    HttpPost httpPost = new HttpPost(url);
    StringEntity se = new StringEntity(content, CharsetsUtil.UTF_8);
    se.setContentEncoding(CharsetsUtil.UTF_8);
    httpPost.setEntity(se);
    return execute(url, CharsetsUtil.UTF_8, httpPost, httpClient);
  }

  /*-----------------------------------------------------------------------------*/
  /**
   * ssl post 请求
   *
   * <pre>
   * List&lt;NameValuePair&gt; nvps = new ArrayList&lt;NameValuePair&gt;();
   * nvps.add(new BasicNameValuePair(&quot;user.userName&quot;, &quot;哈哈&quot;));
   * HttpClientUtil.doPost(&quot;http://localhost:8080/login/login.do&quot;, nvps, &quot;utf-8&quot;);
   * </pre>
   *
   * @param url
   *        目的地址
   * @param nvps
   *        A name / value pair parameter list used as an element of HTTP messages.
   * @param encoding
   *        编码
   * @return 执行ssl post请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String postSSL(String url, List<NameValuePair> nvps, String encoding) throws Exception {
    CloseableHttpClient httpClient = getSSLHttpClient();
    return post(url, nvps, encoding, httpClient);
  }

  /**
   * ssl post 请求
   * 默认编码UTF_8
   *
   * <pre>
   * List&lt;NameValuePair&gt; nvps = new ArrayList&lt;NameValuePair&gt;();
   * nvps.add(new BasicNameValuePair(&quot;user.userName&quot;, &quot;哈哈&quot;));
   * HttpClientUtil.doPost(&quot;http://localhost:8080/login/login.do&quot;, nvps, &quot;utf-8&quot;);
   * </pre>
   *
   * @param url
   *        目的地址
   * @param nvps
   *        A name / value pair parameter list used as an element of HTTP messages.
   * @return 执行ssl post请求的返回结果
   * @throws Exception
   *         Exception
   */
  public String postSSL(String url, List<NameValuePair> nvps) throws Exception {
    return postSSL(url, nvps, CharsetsUtil.UTF_8);
  }

  /*-----------------------------------------------------------------------------*/
  /**
   * socket
   *
   * @param ip
   *        ip
   * @param port
   *        port
   * @param message
   *        message
   * @param flgReceive
   *        flgReceive
   * @return return
   */
  public static String socket(String ip, int port, String message, boolean flgReceive) {
    return socket(ip, port, message, flgReceive, "utf8");
  }

  /**
   * socket
   *
   * @param ip
   *        ip
   * @param port
   *        port
   * @param message
   *        message
   * @return return
   */
  public static String socket(String ip, int port, String message) {
    return socket(ip, port, message, false, "utf8");
  }

  /**
   * socket
   *
   * @param ip
   *        ip
   * @param port
   *        port
   * @param message
   *        message
   * @param flgReceive
   *        flgReceive
   * @param encoding
   *        encoding
   * @return return
   */
  public static String socket(String ip, int port, String message, boolean flgReceive, String encoding) {
    try {
      String result = "";
      Socket handler = new Socket(ip, port);
      handler.setSendBufferSize(Integer.MAX_VALUE);
      handler.setSoTimeout(0);
      handler.setReceiveBufferSize(Integer.MAX_VALUE);
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(handler.getOutputStream(), encoding);
      outputStreamWriter.write(message);
      outputStreamWriter.flush();
      if (!flgReceive) {
        handler.shutdownInput();
        handler.shutdownOutput();
        handler.close();
        return result;
      }
      InputStreamReader inputStreamReader = new InputStreamReader(handler.getInputStream());
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      int current = -1;
      while ((current = inputStreamReader.read()) != -1) {
        byteArrayOutputStream.write(current);
        if (current == 0) {
          break;
        }
      }
      result = byteArrayOutputStream.toString(encoding);
      handler.shutdownInput();
      handler.shutdownOutput();
      handler.close();
      return result;
    } catch (Exception err) {
      throw new RuntimeException(err.getMessage());
    }
  }

  /*-----------------------------------------------------------------------------*/
  /**
   * get 下载文件
   *
   * @param url
   *        目的地址
   * @return 下载的文件（as a byte array）
   * @throws Exception
   *         Exception
   */
  public byte[] getFile(String url) throws Exception {
    CloseableHttpClient httpClient = getHttpClient();
    CloseableHttpResponse response = null;
    try {
      HttpGet httpget = new HttpGet(url);
      response = httpClient.execute(httpget);
      HttpEntity entity = response.getEntity();
      return EntityUtils.toByteArray(entity);
    } finally {
      response.close();
      httpClient.close();
    }
  }

  /**
   * post上传文件
   *
   * @param url
   *        目的地址
   * @param fileName
   *        文件名
   * @param file
   *        文件
   * @return 上传结果
   * @throws Exception
   *         Exception
   */
  public String postFile(String url, String fileName, File file) throws Exception {
    CloseableHttpClient httpClient = getHttpClient();
    HttpPost post = new HttpPost(url);
    FileBody bin = new FileBody(file);
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    multipartEntityBuilder.addPart(fileName, bin);
    HttpEntity multiEntity = multipartEntityBuilder.build();
    post.setEntity(multiEntity);
    return execute(url, CharsetsUtil.UTF_8, post, httpClient);
  }

  /**
   * set 连接超时时间
   *
   * @param connectTimeout
   *        set connectTimeout
   */
  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  /**
   * set 请求超时时间
   *
   * @param connectionRequestTimeout
   *        set connectionRequestTimeout
   */
  public void setConnectionRequestTimeout(int connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
  }

  /**
   * set socket超时时间
   *
   * @param socketTimeout
   *        set socketTimeout
   */
  public void setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  /**
   * set 头信息
   *
   * @param headers
   *        set headers
   */
  public void setHeaders(List<Header> headers) {
    this.headers = headers;
  }

  /**
   * 添加头信息
   *
   * @param name
   *        键
   * @param value
   *        值
   */
  public void addHeader(String name, String value) {
    Header header = new BasicHeader(name, value);
    if (headers == null) {
      headers = new ArrayList<Header>();
    }
    headers.add(header);
  }

}
