
## 项目结构

```
java-agent-demo               
├    ├── java-agent                 -- agent主包 
├    ├── java-agent-core            -- 核心包：构建完成后，会将此包的所有class以共享的方式打包到java-agent.jar里面
├    ├── sdk-plugin                 -- 插件：构建时会将依赖的jar打包到同一个jar，构建完成后，会将jar复制到/out/plugins 目录下
├    ├── examples                   -- 用例

```
### 插件开发
```
 参考 sdk-plugin/spring-mvc-plugin
```

### 构建输出jar
```
java-agent-demo
    -out
        -java-agent-1.0-SNAPSHOT.jar
        -config
        -plugins
            -spring-mvc-plugin-1.0-SNAPSHOT.jar
            
将out目录复制到指定目录下
```

### 启动

```
启动jvm参数：-javaagent:/{java-agent所有目录}/java-agent-1.0-SNAPSHOT.jar

```
