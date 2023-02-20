package com.zhongym.agent.demo.loader;

import com.zhongym.agent.code.enhance.EnhanceAdvisor;

import java.io.File;
import java.io.InvalidClassException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
    public static List<String> PLUGIN_DIRECTORY = List.of("plugins");


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

    private static String findAgentPath() throws Exception {
        String classResourcePath = EnhanceAdvisorLoader.class.getName().replaceAll("\\.", "/") + ".class";

        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String urlString = resource.toString();

            int insidePathIndex = urlString.indexOf('!');
            boolean isInJar = insidePathIndex > -1;

            if (isInJar) {
                urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
                File agentJarFile = null;
                try {
                    agentJarFile = new File(new URL(urlString).toURI());
                } catch (MalformedURLException | URISyntaxException e) {
                    throw new RuntimeException(urlString, e);
                }
                if (agentJarFile.exists()) {
                    return agentJarFile.getParentFile().getPath();
                }
            } else {
                int prefixLength = "file:".length();
                String classLocation = urlString.substring(
                        prefixLength, urlString.length() - classResourcePath.length());
                return classLocation;
            }
        }
        throw new InvalidClassException("Can not locate agent jar file.");
    }
}
