package com.effectivemeasure.hdfs.proxy;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("httpServer")
public class HttpServer {

    private transient Server httpServer;

    @Autowired
    private transient SpringContextLoaderListener contextLoaderListener;

    @PostConstruct
    @Required
    public void start() throws Exception {
        httpServer = new Server(53333);
        final ServletContextHandler contextHandler
                = new ServletContextHandler(ServletContextHandler.NO_SECURITY + ServletContextHandler.NO_SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.setDisplayName("Root Servlet Context");

        contextHandler.addEventListener(new SpringContextLoaderListener());

        // Setup Default Servlet
        final ServletHolder defaultServlet = new ServletHolder("Default Handler", DefaultServlet.class);
        defaultServlet.setInitOrder(1);
        contextHandler.addServlet(defaultServlet, "/*");

        final ServletHolder springServlet = new ServletHolder("HdfsProxy", ConfigurableSpringServlet.class);
        springServlet.setInitOrder(2);
        // Set up the package to scan for web services
        springServlet.setInitParameter(ConfigurableSpringServlet.CONTEXT_CONFIG_CLASS,
                ServiceScanConfiguration.class.getName());
        springServlet.setInitParameter("com.sun.jersey.config.property.packages",
                "com.effectivemeasure.hdfs.proxy");
        springServlet.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters",
                "com.sun.jersey.api.container.filter.LoggingFilter");
        springServlet.setInitParameter("com.sun.jersey.config.feature.Trace", "false");
        contextHandler.addServlet(springServlet, "/hdfs/*");

        httpServer.setHandler(contextHandler);

        httpServer.start();
    }

    @PreDestroy
    @Required
    public void shutdown() throws Exception {
        if (this.httpServer != null) {
            if (!this.httpServer.isStopping() && !this.httpServer.isStopping()) {
                this.httpServer.stop();
            }
            if (this.httpServer.isStopped()) {
                this.httpServer.destroy();
            }
        }
    }

    public Map<String, String> getServiceEndpoints() {

        final HandlerWrapper handler = this.httpServer;

        final ServletContextHandler servletContextHandler = (ServletContextHandler)httpServer.getHandler();
        final ServletHandler servletHandler = servletContextHandler.getServletHandler();
        final ServletMapping[] mappings = servletHandler.getServletMappings();

        final Map<String, String> endpointMap = new LinkedHashMap<String, String>();
        for (final ServletMapping servletMapping : mappings) {
            final StringBuilder paths = new StringBuilder();
            for (final String pathSpec : servletMapping.getPathSpecs()) {
                paths.append(',');
                paths.append(pathSpec);
            }
            if (paths.charAt(0) == ',') {
                paths.deleteCharAt(0);
            }
            endpointMap.put(paths.toString(), servletMapping.getServletName());
        }

        return endpointMap;
    }
}
