package com.zhongym.agent.demo.loader;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;
import com.zhongym.agent.core.loader.EnhanceAdvisorLoader;

/**
 * @author zhongym
 */
public class EnhanceAdvisorLoaderTest {

    public static void main(String[] args) {
        EnhanceAdvisorLoader.AGENT_PATH = "/Users/zhongym/IdeaProjects/sources/java-agent-demo/java-agent/target/";
        Iterable<EnhanceAdvisor> advisors = EnhanceAdvisorLoader.getEnhanceAdvisor();
        System.out.println(advisors);
    }
}
