package com.zhongym.agent.code.enhance;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author zhongym
 */
public interface MethodInterceptor {

    /**
     * 拦截方法
     *
     * @param obj          目标对象
     * @param allArguments 目标方法参数
     * @param callable     目标方法执行器
     * @param method       目标方法
     * @return 目标方法执行结果
     * @throws Throwable
     */
    default Object intercept(final Object obj,
                             final Object[] allArguments,
                             final Callable<?> callable,
                             final Method method) throws Throwable {
        return callable.call();
    }
}
