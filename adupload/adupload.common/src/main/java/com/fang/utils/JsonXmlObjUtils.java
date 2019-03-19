/**
 * File：JsonXmlObjUtils.java
 * Package：com.fang.framework.utils
 * Author：wangzhiyuan
 * Date：2016年7月5日 上午10:35:31
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package com.fang.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.fang.utils.lang.StringUtil;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;

/**
 * Json,Xml,Obj 互相转换工具类
 *
 * @author wangzhiyuan
 */
public class JsonXmlObjUtils {

  /**
   * fastjson配置文件
   */
  private static SerializeConfig mapping = new SerializeConfig();

  static {
    mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss.SSS"));
    //mapping.put(DateTime.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss.SSS"));
  }

  // -----------------------------------------------------------------------
  /**
   * javaBean、list、map convert to json string
   *
   * @param obj
   *        javaBean、list、map
   * @return json string
   */
  public static String obj2json(Object obj) {
    // return JSON.toJSONString(obj,SerializerFeature.UseSingleQuotes);//使用单引号
    // return JSON.toJSONString(obj,true);//格式化数据，方便阅读
    return JSON.toJSONString(obj, mapping);
  }

  /**
   * json string convert to javaBean、map
   *
   * @param jsonStr
   *        json string
   * @param clazz
   *        Class
   * @param <T>
   *        类型
   * @return javaBean、map
   */
  public static <T> T json2obj(String jsonStr, Class<T> clazz) {
    return JSON.parseObject(jsonStr, clazz);
  }

  // -----------------------------------------------------------------------
  /**
   * json string convert to JSONObject
   *
   * @param jsonStr
   *        json string
   * @return JSONObject
   */
  public static JSONObject json2JsonObject(String jsonStr) {
    return JSON.parseObject(jsonStr);
  }

  /**
   * json array string convert to JSONArray
   *
   * @param jsonArrayStr
   *        json array string
   * @return JSONArray
   */
  public static JSONArray json2JSONArray(String jsonArrayStr) {
    return JSON.parseArray(jsonArrayStr);
  }

  // -----------------------------------------------------------------------
  /**
   * json array string convert to list with javaBean
   *
   * @param jsonArrayStr
   *        json array string
   * @param clazz
   *        Class
   * @param <T>
   *        类型
   * @return list with javaBean
   */
  public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
    return JSON.parseArray(jsonArrayStr, clazz);
  }

  /**
   * json string convert to map
   *
   * @param jsonStr
   *        json string
   * @param <T>
   *        类型
   * @return map
   */
  @SuppressWarnings("unchecked")
  public static <T> Map<String, Object> json2map(String jsonStr) {
    return json2obj(jsonStr, Map.class);
  }

  /**
   * json string convert to map with javaBean
   *
   * @param jsonStr
   *        json string
   * @param clazz
   *        Class
   * @param <T>
   *        类型
   * @return map with javaBean
   */
  public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
    Map<String, T> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, T>>() {
    });
    for (Entry<String, T> entry : map.entrySet()) {
      JSONObject obj = (JSONObject) entry.getValue();
      map.put(entry.getKey(), JSONObject.toJavaObject(obj, clazz));
    }
    return map;
  }

  // -----------------------------------------------------------------------
  /**
   * json string convert to xml string
   *
   * @param json
   *        json string
   * @param withHeader
   *        remove <code> &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt; </code>
   *        or not
   * @param oneLine
   *        remove <code>&quot;\n\t&quot;</code>,<code>&quot;\t&quot;</code> or not
   * @return xml string
   */
  public static String json2xml(String json, boolean withHeader, boolean oneLine) {
    StringReader input = new StringReader(json);
    StringWriter output = new StringWriter();
    JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).repairingNamespaces(false).build();
    try {
      XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(input);
      XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(output);
      writer = new PrettyXMLEventWriter(writer);
      writer.add(reader);
      reader.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        output.close();
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    String str = output.toString();
    if (output.toString().length() >= 38) {
      if (withHeader) {
        str = StringUtil.replaceEach(str, new String[] {"<?xml version='1.0' encoding='UTF-8'?>", "<?xml version='1.0'?>"}, new String[] {"", ""});
        // remove <?xml version="1.0" encoding="UTF-8"?>
        //str = str.substring(39);
      }
    }

    if (oneLine) {
      str = StringUtil.replaceEach(str, new String[] {"\n\t", "\t"}, new String[] {"", ""});
    }

    return str;
  }

  /**
   * json string convert to xml string
   *
   * @param json
   *        json json string
   * @return xml string
   */
  public static String json2xml(String json) {
    return json2xml(json, true, true);
  }

  /**
   * xml string convert to json string
   *
   * @param xml
   *        xml string
   * @return json string
   */
  public static String xml2json(String xml) {
    StringReader input = new StringReader(xml);
    StringWriter output = new StringWriter();
    JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true).autoPrimitive(true).prettyPrint(true).build();
    try {
      XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(input);
      XMLEventWriter writer = new JsonXMLOutputFactory(config).createXMLEventWriter(output);
      writer.add(reader);
      reader.close();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        output.close();
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return output.toString();
  }

  // -----------------------------------------------------------------------
  /**
   * xml string convert to javaBean、map
   *
   * @param xml
   *        xml string
   * @param clazz
   *        Class
   * @param <T>
   *        类型
   * @return javaBean、map
   */
  public static <T> T xml2obj(String xml, Class<T> clazz) {
    String jsonStr = xml2json(xml);
    return json2obj(jsonStr, clazz);
  }

  /**
   * javaBean、list、map convert to xml string
   *
   * @param obj
   *        javaBean、list、map
   * @return xml string
   */
  public static String obj2xml(Object obj) {
    String json = obj2json(obj);
    return json2xml(json);
  }

}
