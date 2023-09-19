package com.example.jdkplugin;

import com.zhongym.agent.core.enhance.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhongym
 */
public class JdkThreadPoolMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Object[] allArguments, Callable<?> callable, Method method) throws Throwable {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) obj;
        System.out.println("调用方法:" + method.getName());
        System.out.println("当前线程池状态:" + threadPoolExecutor.toString());
        return callable.call();
    }
}
