package com.zhongym.agent.core;

import com.zhongym.agent.core.config.ConfigLoader;
import com.zhongym.agent.core.loader.PluginClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongym
 */
public class PluginBootstrap {
    /**
     * agent.jar所有目录
     */
    private static String agentPath = null;

    /**
     * 插件目录，默认从agent.jar所有目录下加载
     */
    public static final List<String> PLUGIN_DIRECTORY = List.of("plugins");


    public static String getAgentPath() {
        if (agentPath == null) {
            synchronized (PluginBootstrap.class) {
                agentPath = findAgentPath();
            }
        }
        return agentPath;
    }

    public static void initBootstrap() {
        // 初始化agent目录
        String agentPath = getAgentPath();
        System.out.println("=====================agentPath:" + agentPath);

        // 初始化配置文件
        ConfigLoader.iniConfig();

        // 初始化类载器
        PluginClassLoader.initDefaultLoader(PluginBootstrap.class.getClassLoader(), getPluginPaths());
    }

    private static List<String> getPluginPaths() {
        try {
            String agentPath = getAgentPath();
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
        String classResourcePath = PluginBootstrap.class.getName().replaceAll("\\.", "/") + ".class";

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
                    e.printStackTrace();
                    System.out.println("Can not locate agent jar file by url:" + urlString);
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

        throw new RuntimeException("Can not locate agent jar file.");

    }
}
