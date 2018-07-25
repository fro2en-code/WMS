package com.ymt.velocity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;

public class Test {

    public static void main(String[] args) throws IOException {
        VelocityContext context = new VelocityContext();
        context.put("comment", "12345");
        context.put("author", "zhouxianlgh");
        context.put("serviceName", "MyService");
        context.put("superServiceClass", "MySuperService");
        context.put("entity", "MyEntitys");
        context.put("date", "nowTime");
        context.put("test2", 5);
        Map<String, String> map = new HashMap<>();
        map.put("Entity", "abc");
        map.put("Service", "abcService");
        context.put("package", map);
        context.put("list", map.values());
        Generator generator = new Generator();
        System.out.println(generator.vmToString(context, "template/test.java.vm"));
    }
}
