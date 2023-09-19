package com.example.jdkplugin;

import com.zhongym.agent.core.enhance.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author zhongym
 */
public class TestMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Object[] allArguments, Callable<?> callable, Method method) throws Throwable {
        System.out.println("调用方法:" + method.getName());
        return callable.call();
    }
}
