package com.zhongym.agent.core.loader;

import com.zhongym.agent.core.enhance.EnhanceAdvisor;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author zhongym
 */
public class EnhanceAdvisorLoader {
    /**
     * agent.jar所有目录
     */
    public static String AGENT_PATH = null;

    /**
     * 插件目录，默认从agent.jar所有目录下加载
     */
    public static List<String> PLUGIN_DIRECTORY = List.of("plugins","config");


    public static Iterable<EnhanceAdvisor> getEnhanceAdvisor() {
        //初始化插件类加载器
        PluginClassLoader.initDefaultLoader(EnhanceAdvisorLoader.class.getClassLoader(), getPluginPaths());

        // 通过spl的方式加载
        ServiceLoader<EnhanceAdvisor> loader = ServiceLoader.load(EnhanceAdvisor.class, PluginClassLoader.getDefault());

        List<EnhanceAdvisor> advisors = new ArrayList<>();
        for (EnhanceAdvisor enhanceAdvisor : loader) {
            advisors.add(enhanceAdvisor);
        }
        return advisors;
    }

    private static List<String> getPluginPaths() {
        try {
            String agentPath = AGENT_PATH == null ? findAgentPath() : AGENT_PATH;
            List<String> pluginPaths = new ArrayList<>();
            for (String path : PLUGIN_DIRECTORY) {
                pluginPaths.add(agentPath.endsWith(File.separator) ? agentPath + path : agentPath + File.separator + path);
            }
            return pluginPaths;
        } catch (Exception e) {
            throw new RuntimeException("加载插件位置失败", e);
        }
    }

    /**
     * 获取java-agent jar所有在目录
     */
    private static String findAgentPath() {
        try {
            URL location = EnhanceAdvisorLoader.class.getProtectionDomain().getCodeSource().getLocation();
            String path = location.toURI().getSchemeSpecificPart();
            return path.substring(0, path.lastIndexOf(File.separator) + 1);
        } catch (Exception e) {
            throw new RuntimeException("Can not locate agent jar file.", e);
        }
    }
}
