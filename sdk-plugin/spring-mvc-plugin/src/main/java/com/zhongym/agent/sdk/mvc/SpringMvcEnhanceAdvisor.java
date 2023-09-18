package com.zhongym.agent.sdk.mvc;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.enhance.MethodInterceptor;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author zhongym
 */
//@Slf4j
public class SpringMvcEnhanceAdvisor implements EnhanceAdvisor {

    public static final Set<String> ANNOTATIONS = Set.of(
            "org.springframework.web.bind.annotation.RequestMapping",
            "org.springframework.web.bind.annotation.GetMapping",
            "org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            "org.springframework.web.bind.annotation.PatchMapping"
    );

    public SpringMvcEnhanceAdvisor() {
    }

    @Override
    public ElementMatcher<? super TypeDescription> typeMatcher() {
        return ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.springframework.web.bind.annotation.RestController"))
                .or(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.springframework.web.bind.annotation.Controller")));
    }

    @Override
    public ElementMatcher<? super MethodDescription> methodMatcher() {
        return new ElementMatcher<MethodDescription>() {
            @Override
            public boolean matches(MethodDescription target) {
                for (AnnotationDescription description : target.getDeclaredAnnotations()) {
                    if (ANNOTATIONS.contains(description.getAnnotationType().getActualName())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    public Supplier<MethodInterceptor> interceptor() {
        return SpringMvcMethodInterceptor::new;
    }
}
