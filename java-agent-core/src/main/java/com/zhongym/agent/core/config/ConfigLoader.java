package com.zhongym.agent.core.config;

import com.zhongym.agent.core.PluginBootstrap;
import com.zhongym.agent.core.ex.AgentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author zhongym
 */
public class ConfigLoader {
    private static final String CONFIG = "config/agent.config";
    private static Properties config;

    public static void iniConfig() {
        if (config == null) {
            load();
        }
    }

    public static String getConfig(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    public static Integer getInt(String key, Integer defaultValue) {
        String value = getProperties().getProperty(key);
        return value == null ? defaultValue : Integer.valueOf(value);
    }


    public static Long getLong(String key, Long defaultValue) {
        String value = getProperties().getProperty(key);
        return value == null ? defaultValue : Long.valueOf(value);
    }

    public static boolean getBool(String key, Boolean defaultValue) {
        String value = getProperties().getProperty(key);
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    private static void load() {
        try {
            String agentPath = PluginBootstrap.getAgentPath();
            String configFilePath = agentPath.endsWith(File.separator) ? agentPath + CONFIG : agentPath + File.separator + CONFIG;
            Properties config = new Properties();
            config.load(new FileInputStream(configFilePath));
            ConfigLoader.config = config;
        } catch (IOException e) {
            throw new AgentException("配置初始化失败", e);
        }
    }

    private static Properties getProperties() {
        if (config == null) {
            throw new AgentException("agent配置未初始化");
        }
        return config;
    }

}
