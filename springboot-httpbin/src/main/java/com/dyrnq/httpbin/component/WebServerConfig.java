package com.dyrnq.httpbin.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebServerConfig implements ApplicationListener<ServletWebServerInitializedEvent> {
    Logger logger = LoggerFactory.getLogger(WebServerConfig.class);

    private boolean ssl;

    @Bean
    public String schema() {
        return ssl ? "https" : "http";
    }

    @Bean
    public WebServerFactoryCustomizer containerCustomizer() {
        return container -> {
            if (container instanceof AbstractConfigurableWebServerFactory factory) {
                ssl = factory.getSsl() != null;
            }
        };
    }

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        Object webServer = event.getWebServer();
        logger.info("Currently used ServletWebServerFactory implementation class: {}", webServer.getClass().getName());
    }

    @Bean
    public FilterRegistrationBean accessControlAllowFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        AccessControlAllowFilter accessControlAllowFilter = new AccessControlAllowFilter();
        filterRegistrationBean.setFilter(accessControlAllowFilter);
        filterRegistrationBean.setUrlPatterns(List.of("/*"));
        return filterRegistrationBean;
    }
}
