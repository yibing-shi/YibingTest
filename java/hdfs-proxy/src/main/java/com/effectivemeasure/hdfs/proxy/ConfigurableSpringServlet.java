package com.effectivemeasure.hdfs.proxy;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class ConfigurableSpringServlet extends SpringServlet {

    public static final String CONTEXT_CONFIG_CLASS = "contextConfigClass";

    @Override
    protected ConfigurableApplicationContext getContext() {
        final String contextConfigClass = getWebConfig().getInitParameter(CONTEXT_CONFIG_CLASS);
        if (contextConfigClass == null) {
            return super.getContext();
        } else {
            return getAnnotationConfigChildContext(contextConfigClass);
        }
    }

    protected ConfigurableApplicationContext getAnnotationConfigChildContext(final String contextConfigClass) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setParent(getDefaultContext());
        ctx.setServletContext(getServletContext());
        try {
            ctx.register(Class.forName(contextConfigClass));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        ctx.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
                BeanFactoryPostProcessor resolver = (BeanFactoryPostProcessor) beanFactory.getBean("propertySourcesPlaceholderConfigurer");
                resolver.postProcessBeanFactory(beanFactory);
            }
        });
        ctx.refresh();

        return ctx;
    }
}

