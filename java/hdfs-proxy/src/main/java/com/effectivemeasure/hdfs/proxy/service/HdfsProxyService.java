package com.effectivemeasure.hdfs.proxy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
@Path("/proxy")
public class HdfsProxyService {

    @Value("${hdfs.namenode.uri}")
    private String nameNodeUri;

    @GET
    @Produces("text/html")
    public String readConfigure() {
        return nameNodeUri;
    }

    @PostConstruct
    public void info(){
        System.out.println(nameNodeUri);
    }
}
