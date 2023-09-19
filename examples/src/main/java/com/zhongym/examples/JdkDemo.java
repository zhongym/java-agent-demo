package com.zhongym.examples;

import java.util.concurrent.*;

/**
 * @author zhongym
 */
public class JdkDemo {

    public static void main(String[] args) throws InterruptedException {
        new Test().say();
        new Test().say1();
    }
}
