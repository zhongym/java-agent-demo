package com.zhongym.agent.main;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.enhance.EnhanceAdvisorLoader;
import com.zhongym.agent.main.impl.DefaultListener;
import com.zhongym.agent.main.impl.EnhanceAdvisorTransformer;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.matcher.BooleanMatcher;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author zhongym
 */
public class AgentMain {
//Cannot inject auxiliary class into bootstrap loader using reflection
    public static void premain(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default(new ByteBuddy(ClassFileVersion.JAVA_V11))
                // 忽略某些类
                .ignore(ignore())
                // 增强类
                .type(type())
                .transform(new EnhanceAdvisorTransformer())
                //监听器
                .with(new DefaultListener())
                .with(new AgentBuilder.InjectionStrategy.UsingUnsafe.OfFactory(ClassInjector.UsingUnsafe.Factory.resolve(instrumentation)))
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                //委托给agent
                .installOn(instrumentation);
    }

    private static ElementMatcher<? super TypeDescription> type() {
        ElementMatcher.Junction<TypeDescription> allMatcher = BooleanMatcher.of(false);

        for (EnhanceAdvisor advisor : EnhanceAdvisorLoader.getEnhanceAdvisor()) {
            ElementMatcher<? super TypeDescription> matcher = advisor.typeMatcher();
            allMatcher = allMatcher.or(matcher);
        }

        ElementMatcher.Junction<TypeDescription> finalAllMatcher = allMatcher;
        return target -> {
            try {
                return finalAllMatcher.matches(target);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };
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