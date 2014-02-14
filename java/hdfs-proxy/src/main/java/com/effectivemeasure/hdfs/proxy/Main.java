package com.effectivemeasure.hdfs.proxy;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Main {
    public static void main(String[] args) throws Exception {
        final AbstractApplicationContext context = new AnnotationConfigApplicationContext(ServerContextConfiguration.class);
        context.registerShutdownHook();
        context.start();

        HttpServer httpServer = (HttpServer) context.getBean("httpServer");
        System.out.println(httpServer.getServiceEndpoints());

        try {
            Thread.currentThread().join(); // Wait for a HUP or similar
        } catch (Exception e) {
            // Ignore
        }
    }
}
