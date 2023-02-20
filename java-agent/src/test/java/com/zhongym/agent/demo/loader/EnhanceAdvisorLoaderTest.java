package com.zhongym.agent.demo.loader;

import com.zhongym.agent.code.enhance.EnhanceAdvisor;

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
