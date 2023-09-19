package com.zhongym.agent.main;

import com.zhongym.agent.core.PluginBootstrap;
import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.main.impl.DefaultListener;
import com.zhongym.agent.main.impl.EnhanceAdvisorTransformer;
import com.zhongym.agent.core.enhance.EnhanceAdvisorLoader;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.BooleanMatcher;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author zhongym
 */
public class AgentMain {

    public static void premain(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                // 忽略某些类
                .ignore(ignore())
                // 增强类
                .type(BooleanMatcher.of(true))
                .transform(new EnhanceAdvisorTransformer())
                //监听器
                .with(new DefaultListener())
                //委托给agent
                .installOn(instrumentation);
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