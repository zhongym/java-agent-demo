package com.zhongym.agent.demo.loader;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.enhance.EnhanceAdvisorLoader;

/**
 * @author zhongym
 */
public class EnhanceAdvisorLoaderTest {

    public static void main(String[] args) {
//        PluginBootstrap.initBootstrap();
        Iterable<EnhanceAdvisor> advisors = EnhanceAdvisorLoader.getEnhanceAdvisor();
        System.out.println(advisors);
    }
}
