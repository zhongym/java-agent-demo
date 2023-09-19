package com.zhongym.agent.main.impl;

import com.zhongym.agent.core.PluginBootstrap;
import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.enhance.EnhanceAdvisorLoader;
import com.zhongym.agent.core.enhance.MethodInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

/**
 * @author zhongym
 */
public class EnhanceAdvisorTransformer implements AgentBuilder.Transformer {
    private final Iterable<EnhanceAdvisor> enhanceAdvisors;

    public EnhanceAdvisorTransformer() {
        // 加载
        enhanceAdvisors = EnhanceAdvisorLoader.getEnhanceAdvisor();
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        for (EnhanceAdvisor advisor : enhanceAdvisors) {
            if (advisor.typeMatcher().matches(typeDescription)) {
                MethodInterceptor methodInterceptor = advisor.interceptor().get();
                builder = builder
                        .method(advisor.methodMatcher())
                        .intercept(MethodDelegation
                                .to(new MethodInterceptorAdapter(methodInterceptor)));
            }
        }
        return builder;
    }
}