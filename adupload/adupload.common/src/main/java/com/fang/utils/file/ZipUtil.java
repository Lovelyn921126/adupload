/**
 * File：ZipUtil.java
 * Package：com.fang.utils.file
 * Author：yaokaibao
 * Date：2017年5月3日 下午6:28:01
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * ZipUtil
 *
 * @author yaokaibao
 */
public class ZipUtil {

  /**
   * 获取zip输出流
   *
   * @param zipFileName
   *        压缩文件完整路径
   * @return ZipOutputStream
   */
  private static ZipOutputStream getZipOutputStream(String zipFileName) {
    File file = new File(zipFileName);

    ZipOutputStream zos = null;
    try {
      if (file.exists()) {
        file.delete();
      }
      file.createNewFile();
      zos = new ZipOutputStream(new FileOutputStream(file));
    } catch (IOException ex) {
      zos = null;
    }
    return zos;
  }

  /**
   * 创建zip节点目录
   *
   * @param zos
   *        zip输出流
   * @param path
   *        zip内部相对路径
   * @throws IOException
   *         IOException
   */
  private static void createZipNode(ZipOutputStream zos, String path) throws IOException {
    ZipEntry zipEntry = new ZipEntry(path);
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
  }

  /**
   * 压缩文件
   *
   * @param zos
   *        zip输出流
   * @param file
   *        文件
   * @param path
   *        zip内部相对路径
   * @return boolean
   * @throws IOException
   *         IOException
   */
  private static boolean zipFile(ZipOutputStream zos, File file, String path) throws IOException {
    boolean result = true;
    ZipEntry zipEntry = new ZipEntry(path + file.getName());
    zos.putNextEntry(zipEntry);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      int max = 1024;
      int length = 0;
      byte[] buffer = new byte[max];
      while ((length = stream.read(buffer, 0, max)) >= 0) {
        zos.write(buffer, 0, length);
        length = 0;
      }
      zos.flush();
      zos.closeEntry();
    } catch (IOException ex) {
      result = false;
    } finally {
      if (stream != null) {
        stream.close();
      }
    }
    return result;
  }

  /**
   * 压缩文件夹
   *
   * @param zos
   *        zip输出流
   * @param file
   *        文件夹
   * @param path
   *        zip内部相对路径
   * @return boolean
   * @throws IOException
   *         IOException
   */
  private static boolean zipDirectory(ZipOutputStream zos, File file, String path) throws IOException {
    String newPath = path + file.getName() + File.separator;
    createZipNode(zos, newPath);

    File[] files = file.listFiles();
    for (File f : files) {
      boolean result = true;
      if (f.isDirectory()) {
        result = zipDirectory(zos, file, newPath);
      } else {
        result = zipFile(zos, f, newPath);
      }
      if (!result) {
        return false;
      }
    }
    return true;
  }

  /**
   * 压缩
   *
   * @param zipFileName
   *        压缩文件完整路径
   * @param path
   *        zip内部相对路径
   * @param filePath
   *        要压缩的文件（夹）路径
   * @return 是否压缩成功
   * @throws IOException
   *         IOException
   */
  public static boolean zip(String zipFileName, String path, String filePath) throws IOException {
    boolean result = false;
    File file = new File(filePath);
    if (!file.exists()) {
      return result;
    }

    ZipOutputStream zos = getZipOutputStream(zipFileName);
    zos.setLevel(6);

    try {
      if (file.isDirectory()) {
        result = zipDirectory(zos, file, path);
      } else {
        result = zipFile(zos, file, path);
      }

      return result;
    } catch (IOException ex) {
      return false;
    } finally {
      zos.flush();
      zos.close();
      zos = null;
      System.gc();
    }
  }

  /**
   * 压缩
   *
   * @param zipFileName
   *        压缩文件完整路径
   * @param filePath
   *        要压缩的文件（夹）路径
   * @return 是否压缩成功
   * @throws IOException
   *         IOException
   */
  public static boolean zip(String zipFileName, String filePath) throws IOException {
    return zip(zipFileName, "", filePath);
  }
}
