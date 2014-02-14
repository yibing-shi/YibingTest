package com.effectivemeasure.hdfs.proxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.servlet.ServletContext;

@Component
public class SpringContextLoaderListener extends ContextLoaderListener implements ApplicationContextAware {

    private transient ApplicationContext applicationContext;

    /**
     * This gets called by the Spring Web Application Loader
     */
    @Override
    protected WebApplicationContext createWebApplicationContext(final ServletContext servletContext) {

        final GenericWebApplicationContext webApplicationContext = new GenericWebApplicationContext(servletContext);

        webApplicationContext
                .setDisplayName("Web Application Context [" + servletContext.getServletContextName() + "]");
        webApplicationContext.setParent(this.applicationContext);

        return webApplicationContext;
    }

    /**
     * This gets invoked by the Spring Application Context loader
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
