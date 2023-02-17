package com.zhongym.examples.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 启动jvm参数：-javaagent:/{工程所有目录}/java-agent-demo/java-agent/target/java-agent-1.0-SNAPSHOT.jar
 *
 * @author zhongym
 */
@RestController("/demo")
public class DemoController {

    @GetMapping("/say")
    public String say(Long id) {
        return "id" + id;
    }

    public static void main(String[] args) {
        DemoController demoController = new DemoController();
        demoController.say(1L);

        // 输出结果：
        // ------------------------------执行方法：xxx
        // ------------------------------执行参数：xxx
    }
}
