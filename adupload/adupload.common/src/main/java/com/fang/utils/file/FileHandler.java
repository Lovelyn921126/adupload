/**
 * File：FileHandler.java
 * Package：com.fang.utils
 * Author：yaokaibao
 * Date：2017年5月3日 上午10:13:12
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.file;

import java.io.File;
import java.net.URLDecoder;

import com.fang.entity.vo.AdvertFileVo;
import com.fang.utils.CodecUtil;
import com.fang.utils.lang.CharsetsUtil;
import com.fang.utils.web.HttpClientUtil;

/**
 * FileHandler
 *
 * @author yaokaibao
 */
public class FileHandler {

  /**
   * handle
   *
   * @param file
   *        file
   * @param httpClientUtil
   *        httpClientUtil
   * @throws Exception
   *         Exception
   */
  public static void handle(AdvertFileVo file, HttpClientUtil httpClientUtil) throws Exception {
    String id = file.getSourceId();

    String moduleFilePath = "module/720yun-template-11.html"; // "module/index.html"; //
    String moduleFolderPath = URLDecoder.decode(FileHandler.class.getClassLoader().getResource(moduleFilePath).getPath().replace("720yun-template-11.html", "").substring(1),
        CharsetsUtil.UTF_8);

    // 删除临时文件
    FileUtil.delete(new File(moduleFolderPath + id));

    String replaceFilePath = moduleFolderPath + id + "/html/index.html";
    String zipFilePath = moduleFolderPath + id + "/index.zip";

    String text = FileUtil.readAllText(moduleFilePath);
    String newText = text.replace("{TitleString}", file.getProjectName()).replace("{DescriptionString}", file.getDescription()).replace("{ImageUrl}", file.getImageURL().replace("http:", "https:"))
        .replace("{panoid}", file.getSourceId()).replace("{pv}", file.getPv() ? "true" : "").replace("{up}", file.getUp() ? "true" : "")
        .replace("{message}", file.getSm() ? "true" : "").replace("{PanoUrl}", file.getSourceUrl().replace("http:", "https:"));
    FileUtil.writeAllText(replaceFilePath, newText);

    // 压缩
    ZipUtil.zip(zipFilePath, moduleFolderPath + id + "/html/");

    // 上传
    String requestUrl = file.getNetRoad() + "?i=" + file.getSourceId() + "&city=" + CodecUtil.urlEncode(file.getCityName()) + "&channel=" + CodecUtil.urlEncode(file.getChannelName());

    FileUpload.upload(requestUrl, "file", "file", "application/octet-stream", zipFilePath);
    // httpClientUtil.postFile(requestUrl, "file", new File(zipFilePath));

  }

}
