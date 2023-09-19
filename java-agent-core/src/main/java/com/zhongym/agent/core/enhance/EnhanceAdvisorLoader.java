package com.zhongym.agent.core.enhance;

import com.zhongym.agent.core.PluginBootstrap;
import com.zhongym.agent.core.loader.PluginClassLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author zhongym
 */
public class EnhanceAdvisorLoader {

    private static volatile List<EnhanceAdvisor> enhanceAdvisors = null;

    public static List<EnhanceAdvisor> getEnhanceAdvisor() {
        if (enhanceAdvisors == null) {
            synchronized (EnhanceAdvisorLoader.class) {
                if (enhanceAdvisors == null) {
                    // 初始化
                    PluginBootstrap.initBootstrap();

                    // 通过spl的方式加载
                    ServiceLoader<EnhanceAdvisor> loader = ServiceLoader.load(EnhanceAdvisor.class, PluginClassLoader.getDefault());

                    List<EnhanceAdvisor> advisors = new ArrayList<>();
                    for (EnhanceAdvisor enhanceAdvisor : loader) {
                        advisors.add(enhanceAdvisor);
                    }
                    enhanceAdvisors = advisors;
                }
            }
        }
        return enhanceAdvisors;
    }
}
