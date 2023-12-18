package com.zhongym.examples;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhongym
 */
public class ThreadLocalDemo {


    public static void main(String[] args) {
        ThreadLocal<String> objectThreadLocal = new ThreadLocal<>();
        objectThreadLocal.set("a");
        objectThreadLocal.get();

        new ArrayList<>();
        new ReentrantLock(true);
    }
}
