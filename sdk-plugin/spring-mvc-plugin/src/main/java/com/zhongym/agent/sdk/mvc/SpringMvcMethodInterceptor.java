package com.zhongym.agent.sdk.mvc;

import com.zhongym.agent.code.enhance.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author zhongym
 */
public class SpringMvcMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Object[] allArguments, Callable<?> callable, Method method) throws Throwable {
        // 方法执行前
        System.out.println("------------前置通知：" + allArguments);

        // 调用目标方法
        Object result;
        try {
            result = callable.call();
        } catch (Throwable e) {
            System.out.println("------------异常通知：" + e);
            throw e;
        }

        // 方法执行后
        System.out.println("------------后置通知：" + result);

        return result;
    }
}
