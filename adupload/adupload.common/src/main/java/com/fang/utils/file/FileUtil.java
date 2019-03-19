/**
 * File：FileUtil.java
 * Package：com.fang.utils.file
 * Author：yaokaibao
 * Date：2017年5月3日 下午6:12:54
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * FileUtil
 *
 * @author yaokaibao
 */
public class FileUtil {

  /**
   * readAllText
   *
   * @param path
   *        path
   * @return String
   */
  public static String readAllText(String path) {
    StringBuilder builder = new StringBuilder();
    String text;

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(FileHandler.class.getClassLoader().getResourceAsStream(path)));
    try {
      while ((text = bufferedReader.readLine()) != null) {
        builder.append(text);
        builder.append(System.getProperty("line.separator", "/n"));
      }
    } catch (IOException e) {
      builder = new StringBuilder();
    } finally {
      try {
        bufferedReader.close();
      } catch (Exception e2) {
        //
      }
    }

    return builder.toString();
  }

  /**
   * writeAllText
   *
   * @param path
   *        path
   * @param context
   *        context
   */
  public static void writeAllText(String path, String context) {
    FileWriter writer = null;
    File file = new File(path);
    try {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
        file = new File(file.getAbsolutePath());
      }
      file.createNewFile();
      if (file.isFile()) {
        writer = new FileWriter(file);
        writer.write(context);
      }
    } catch (IOException e) {
      //
    } finally {
      try {
        writer.close();
        writer = null;
        System.gc();
      } catch (IOException e) {
        //
      }
    }
  }

  /**
   * toByteArray
   *
   * @param filePath
   *        filePath
   * @return byte[]
   * @throws IOException
   *         IOException
   */
  @SuppressWarnings("resource")
  public static byte[] toByteArray(String filePath) throws IOException {
    FileChannel fc = null;
    try {
      fc = new RandomAccessFile(filePath, "r").getChannel();
      MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
      // System.out.println(byteBuffer.isLoaded());
      byte[] result = new byte[(int) fc.size()];
      if (byteBuffer.remaining() > 0) {
        // System.out.println("remain");
        byteBuffer.get(result, 0, byteBuffer.remaining());
      }
      return result;
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        fc.close();
        fc = null;
        System.gc();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 删除文件（夹）
   *
   * @param file
   *        file
   */
  public static void delete(File file) {
    if (file.exists()) {

      if (file.isDirectory()) {
        File[] files = file.listFiles();
        for (File f : files) {
          delete(f);
        }
      }
      file.delete();
    }

  }
}
