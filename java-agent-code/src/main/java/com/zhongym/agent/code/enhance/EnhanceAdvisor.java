package com.zhongym.agent.code.enhance;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.function.Supplier;

/**
 * @author zhongym
 */
public interface EnhanceAdvisor {

    /**
     * 需要增强的目标类匹配器
     */
    ElementMatcher<? super TypeDescription> typeMatcher();

    /**
     * 需要增强的目标方法匹配器
     */
    ElementMatcher<? super MethodDescription> methodMatcher();

    /**
     * 拦截器
     *
     * @return 拦截器
     */
    Supplier<MethodInterceptor> interceptor();
}
