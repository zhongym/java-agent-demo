package com.zhongym.agent.demo.impl;

import com.zhongym.agent.code.enhance.MethodInterceptor;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author zhongym
 */
public class MethodInterceptorAdapter {
    private final MethodInterceptor methodInterceptor;

    public MethodInterceptorAdapter(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @RuntimeType
    public Object intercept(@This final Object obj,
                            @AllArguments final Object[] allArguments,
                            @SuperCall final Callable<?> callable,
                            @Origin final Method method) throws Throwable {
        return methodInterceptor.intercept(obj, allArguments, callable, method);
    }
}