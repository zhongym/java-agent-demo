package com.example.jdkplugin;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.enhance.MethodInterceptor;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ThreadPoolExecutorEnhanceAdvisor implements EnhanceAdvisor {
    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return ElementMatchers.namedOneOf(
                "java.util.concurrent.ThreadPoolExecutor");
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return ElementMatchers.namedOneOf("submit");
    }

    @Override
    public Supplier<MethodInterceptor> interceptor() {
        return ()->new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Object[] allArguments, Callable<?> callable, Method method) throws Throwable {
                if (method.getParameters()[0].getType().equals(Callable.class)){
                    // 包装
                }
                return MethodInterceptor.super.intercept(obj, allArguments, callable, method);
            }
        };
    }
}
