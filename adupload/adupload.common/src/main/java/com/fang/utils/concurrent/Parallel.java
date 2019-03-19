/**
 * File：Parallel.java
 * Package：com.fang.framework.utils.concurrent
 * Author：wangzhiyuan
 * Date：2017年2月16日 下午1:56:37
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package com.fang.utils.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 并行处理线程
 *
 * @author wangzhiyuan
 */
public class Parallel {

  /**
   * 可用的处理器个数
   */
  private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

  /**
   * 并行处理
   *
   * @param items
   *        ，元素
   * @param func
   *        ，行为
   * @param <T>
   *        类型
   */
  public static <T> void forEach(Iterable<T> items, final LoopBody<T> func) {
    ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES * 2);
    List<Future<?>> futures = new LinkedList<Future<?>>();

    for (final T item : items) {
      Future<?> future = executorService.submit(new Runnable() {

        @Override
        public void run() {
          func.invoke(item);
        }
      });

      futures.add(future);
    }

    for (Future<?> future : futures) {
      try {
        future.get();
      } catch (Exception err) {
        future.cancel(true);
        throw new RuntimeException(err);
      }
    }
    executorService.shutdown();
  }

  /**
   * 并行处理
   *
   * @param start
   *        ，开始
   * @param stop
   *        ，结束
   * @param func
   *        ，行为
   */
  public static void forLoop(int start, int stop, final LoopBody<Integer> func) {
    ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES * 2);
    List<Future<?>> futures = new LinkedList<Future<?>>();

    for (int i = start; i < stop; i++) {

      final int temp = i;
      Future<?> future = executorService.submit(new Runnable() {

        @Override
        public void run() {
          func.invoke(temp);
        }
      });

      futures.add(future);
    }

    for (Future<?> future : futures) {
      try {
        future.get();
      } catch (Exception err) {
        throw new RuntimeException(err);
      }
    }

    executorService.shutdown();
  }

  /**
   * 并行处理
   *
   * @param start
   *        ，开始
   * @param stop
   *        ，结束
   * @param func
   *        ，行为
   */
  public static void forSync(int start, int stop, final LoopBody<Integer> func) {
    ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES * 6);
    List<Callable<Void>> callables = new LinkedList<Callable<Void>>();

    for (int i = start; i < stop; i++) {
      final int temp = i;
      callables.add(new Callable<Void>() {

        @Override
        public Void call() throws Exception {
          func.invoke(temp);
          return null;
        }
      });
    }

    try {
      executorService.invokeAll(callables);
    } catch (InterruptedException e) {
      executorService.shutdown();
      throw new RuntimeException(e);
    }

    executorService.shutdown();
  }

  /**
   * LoopBody
   *
   * @author wangzhiyuan
   * @param <T>
   *        类型
   */
  public interface LoopBody<T> {

    /**
     * 运行
     *
     * @param i
     *        i
     */
    void invoke(T i);
  }
}
