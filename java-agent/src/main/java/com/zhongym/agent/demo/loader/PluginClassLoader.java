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

package com.zhongym.agent.demo.loader;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * copy from skywalking AgentClassLoader
 *
 * @author zhongym
 */
public class PluginClassLoader extends ClassLoader {

    static {
        registerAsParallelCapable();
    }

    /**
     * The default class loader for the agent.
     */
    private static PluginClassLoader DEFAULT_LOADER;

    private final List<File> pluginDirectories = new LinkedList<>();
    private List<Jar> allJars;
    private final ReentrantLock jarScanLock = new ReentrantLock();

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
        super(parent);
        // 插件目录
        pluginPaths.forEach(directory -> {
            pluginDirectories.add(new File(directory));
        });
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        List<Jar> allJars = getAllJars();
        String path = name.replace('.', '/').concat(".class");
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(path);
            if (entry == null) {
                continue;
            }
            try {
                URL classFileUrl = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                byte[] data;
                try (final BufferedInputStream inputStream = new BufferedInputStream(classFileUrl.openStream());
                     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    int ch;
                    while ((ch = inputStream.read()) != -1) {
                        outputStream.write(ch);
                    }
                    data = outputStream.toByteArray();
                }
                return defineClass(name, data, 0, data.length);
            } catch (IOException e) {
            }
        }
        throw new ClassNotFoundException("Can't find " + name);
    }

    @Override
    protected URL findResource(String name) {
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                try {
                    return new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name);
                } catch (MalformedURLException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        List<URL> allResources = new LinkedList<>();
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                allResources.add(new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name));
            }
        }

        final Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }

    private List<Jar> getAllJars() {
        if (allJars == null) {
            jarScanLock.lock();
            try {
                if (allJars == null) {
                    allJars = doGetJars();
                }
            } finally {
                jarScanLock.unlock();
            }
        }

        return allJars;
    }

    private LinkedList<Jar> doGetJars() {
        LinkedList<Jar> jars = new LinkedList<>();
        for (File path : pluginDirectories) {
            if (path.exists() && path.isDirectory()) {
                String[] jarFileNames = path.list((dir, name) -> name.endsWith(".jar"));
                for (String fileName : jarFileNames) {
                    try {
                        File file = new File(path, fileName);
                        Jar jar = new Jar(new JarFile(file), file);
                        jars.add(jar);
                    } catch (IOException e) {
                    }
                }
            }
        }
        return jars;
    }

    private static class Jar {
        private final JarFile jarFile;
        private final File sourceFile;

        public Jar(JarFile jarFile, File sourceFile) {
            this.jarFile = jarFile;
            this.sourceFile = sourceFile;
        }
    }
}