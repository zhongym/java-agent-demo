package com.zhongym.agent.demo.loader;

import com.zhongym.agent.code.enhance.EnhanceAdvisor;

/**
 * @author zhongym
 */
public class EnhanceAdvisorLoaderTest {

    public static void main(String[] args) {
        EnhanceAdvisorLoader.AGENT_PATH = "/agent";
        Iterable<EnhanceAdvisor> advisors = EnhanceAdvisorLoader.getEnhanceAdvisor();
        System.out.println(advisors);
    }
}
