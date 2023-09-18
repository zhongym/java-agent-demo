package com.zhongym.agent.main;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.main.impl.DefaultListener;
import com.zhongym.agent.main.impl.EnhanceAdvisorTransformer;
import com.zhongym.agent.core.loader.EnhanceAdvisorLoader;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author zhongym
 */
public class AgentMain {

    public static void premain(String arguments, Instrumentation instrumentation) {
        AgentBuilder agent = new AgentBuilder.Default();
        // 忽略某些类
        agent.ignore(ignore());
        // 增强类
        agent = enhance(agent);
        //监听器
        agent.with(new DefaultListener());
        //委托给agent
        agent.installOn(instrumentation);
    }

    private static AgentBuilder enhance(AgentBuilder agentBuilder) {
        // 加载类增强定义
        Iterable<EnhanceAdvisor> enhanceAdvisors = EnhanceAdvisorLoader.getEnhanceAdvisor();
        // 添加拦截器
        for (EnhanceAdvisor enhanceAdvisor : enhanceAdvisors) {
            agentBuilder = agentBuilder
                    .type(enhanceAdvisor.typeMatcher())
                    .transform(new EnhanceAdvisorTransformer(enhanceAdvisor));
        }
        return agentBuilder;
    }

    private static ElementMatcher<? super TypeDescription> ignore() {
        return nameStartsWith("net.bytebuddy.")
                .or(nameStartsWith("org.slf4j."))
                .or(nameStartsWith("org.groovy."))
                .or(nameContains("javassist"))
                .or(nameContains(".asm."))
                .or(nameContains(".reflectasm."))
                .or(nameStartsWith("sun.reflect"))
                .or(isSynthetic());
    }

}