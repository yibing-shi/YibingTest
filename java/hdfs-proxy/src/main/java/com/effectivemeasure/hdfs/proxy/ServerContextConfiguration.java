package com.effectivemeasure.hdfs.proxy;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import javax.ws.rs.Path;

@Configuration
@ComponentScan(
        basePackages = {"com.effectivemeasure.hdfs.proxy"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Path.class)}
)
public class ServerContextConfiguration {

}
