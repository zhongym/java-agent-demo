/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zhongym.agent.core.loader;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author zhongym
 */
public class PluginClassLoader extends URLClassLoader {

    static {
        registerAsParallelCapable();
    }

    /**
     * The default class loader for the agent.
     */
    private static PluginClassLoader DEFAULT_LOADER;


    public static PluginClassLoader getDefault() {
        return DEFAULT_LOADER;
    }

    /**
     * Init the default class loader.
     */
    public static void initDefaultLoader(ClassLoader parent, List<String> pluginPaths) {
        if (DEFAULT_LOADER == null) {
            synchronized (PluginClassLoader.class) {
                if (DEFAULT_LOADER == null) {
                    DEFAULT_LOADER = new PluginClassLoader(parent, pluginPaths);
                }
            }
        }
    }

    public PluginClassLoader(ClassLoader parent, List<String> pluginPaths) {
        super(new URL[]{}, parent);
        this.loadJars(pluginPaths);
    }

    private void loadJars(List<String> pluginPaths) {
        pluginPaths.forEach(directory -> {
            File[] jarFiles = new File(directory).listFiles();
            if (jarFiles == null) {
                return;
            }
            for (File file : jarFiles) {
                if (ifJar(file)) {
                    try {
                        addURL(file.toURI().toURL());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(file.getPath(), e);
                    }
                }
            }
        });
    }

    private boolean ifJar(File file) {
        return file.getPath().toLowerCase().endsWith(".jar");
    }

}