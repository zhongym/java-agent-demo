package com.zhongym.agent.demo.impl;

import com.zhongym.agent.code.enhance.EnhanceAdvisor;
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
    private final EnhanceAdvisor enhanceAdvisor;
    public EnhanceAdvisorTransformer(EnhanceAdvisor enhanceAdvisor) {
        this.enhanceAdvisor = enhanceAdvisor;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        return builder.method(enhanceAdvisor.methodMatcher())
                .intercept(MethodDelegation.to(new MethodInterceptorAdapter(enhanceAdvisor.interceptor().get())));
    }
}