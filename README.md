
## 项目结构

```
java-agent-demo               
├    ├── java-agent                 -- agent主包 
├    ├── java-agent-code            -- 核心包：构建完成后，会将此包的所有class以共享的方式打包到java-agent.jar里面
├    ├── sdk-plugin                 -- 插件：构建完成后，会将jar复制到java-agent/target/plugins 目录下
├    ├── examples                   -- 用例

```
### 插件开发
```
 参考 sdk-plugin/spring-mvc-plugin
```

### 构建输出jar
```
java-agent
    -target
        -java-agent-1.0-SNAPSHOT.jar
        -plugins
            -spring-mvc-plugin-1.0-SNAPSHOT.jar
            
可以将target下面的java-agent-1.0-SNAPSHOT.jar和plugins目录复制到指定目录下
```

### 启动

```
启动jvm参数：-javaagent:/{java-agent所有目录}/java-agent-1.0-SNAPSHOT.jar

```
