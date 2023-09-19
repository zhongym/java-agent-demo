package com.example.jdkplugin;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.enhance.MethodInterceptor;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.function.Supplier;

/**
 * @author zhongym
 */
public class JdkThreadPoolEnhanceAdvisor implements EnhanceAdvisor {
    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return ElementMatchers.named("java.util.concurrent.ThreadPoolExecutor");
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
       return ElementMatchers.namedOneOf("submit","execute");
    }

    @Override
    public Supplier<MethodInterceptor> interceptor() {
        return JdkThreadPoolMethodInterceptor::new;
    }
}
