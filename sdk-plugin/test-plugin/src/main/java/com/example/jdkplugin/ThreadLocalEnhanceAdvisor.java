package com.example.jdkplugin;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.enhance.MethodInterceptor;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author zhongym
 */
public class ThreadLocalEnhanceAdvisor implements EnhanceAdvisor {
    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return ElementMatchers.namedOneOf(
                "java.util.concurrent.ThreadPoolExecutor",
                "java.lang.ThreadLocal",
                "java.lang.InheritableThreadLocal");
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return ElementMatchers.namedOneOf("get", "set");
    }

    @Override
    public Supplier<MethodInterceptor> interceptor() {
        return () -> new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Object[] allArguments, Callable<?> callable, Method method) throws Throwable {
                Object call = callable.call();

                if ("get".equals(method.getName())) {
                    System.out.println("get:" + call);
                }
                if ("set".equals(method.getName())) {
                    System.out.println("set:" + allArguments[0]);
                }
                if ("submit".equals(method.getName())){
                    System.out.println("submit======================");
                }
                return call;
            }
        };
    }
}
