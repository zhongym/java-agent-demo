package com.zhongym.examples;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhongym
 */
public class ThreadPoolExectorDemo {


    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,1,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100)
                );
        threadPoolExecutor.submit(()->{
            System.out.println("执行任务");
        });
    }
}
