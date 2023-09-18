package com.zhongym.agent.core.enhance;

import com.zhongym.agent.core.loader.PluginClassLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author zhongym
 */
public class EnhanceAdvisorLoader {

    public static Iterable<EnhanceAdvisor> getEnhanceAdvisor() {
        // 通过spl的方式加载
        ServiceLoader<EnhanceAdvisor> loader = ServiceLoader.load(EnhanceAdvisor.class, PluginClassLoader.getDefault());

        List<EnhanceAdvisor> advisors = new ArrayList<>();
        for (EnhanceAdvisor enhanceAdvisor : loader) {
            advisors.add(enhanceAdvisor);
        }
        return advisors;
    }
}
