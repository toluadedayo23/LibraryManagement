package com.test.librarymanagement.logging;

import com.test.librarymanagement.util.WebSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(com.test.librarymanagement.logging.LoggingProperties.class)
@ConditionalOnProperty(prefix = "librarymanagement.logging", name = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {

    static final String HTTP_REQUEST_HEADER_FILTER_LOG_REG_BEAN_NAME = "HTTP_REQUEST_HEADER_FILTER_LOG_REG_BEAN_NAME";
    static final String SERVLET_ACCESS_LOG_REG_BEAN_NAME = "SERVLET_ACCESS_LOG_REG_BEAN_NAME";

    @Bean(name = HTTP_REQUEST_HEADER_FILTER_LOG_REG_BEAN_NAME)
    public FilterRegistrationBean<com.test.librarymanagement.logging.HttpRequestHeaderLogFilter> registerHttpRequestHeaderLogFilter() {
        log.info("Initializing http header log filter");
        FilterRegistrationBean<com.test.librarymanagement.logging.HttpRequestHeaderLogFilter> registration = new FilterRegistrationBean<>();
        com.test.librarymanagement.logging.HttpRequestHeaderLogFilter filter = new HttpRequestHeaderLogFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean(name = SERVLET_ACCESS_LOG_REG_BEAN_NAME)
    @ConditionalOnProperty(prefix = "ivp.logging", name = "enabled", havingValue = "true")
    public FilterRegistrationBean<com.test.librarymanagement.logging.ServletAccessLogFilter> registerServletAccessLogFilter(LoggingProperties loggingProperties, @Autowired WebSecurityUtil webSecurityUtil) {
        log.info("Initializing servlet access log filter");
        FilterRegistrationBean<com.test.librarymanagement.logging.ServletAccessLogFilter> registration = new FilterRegistrationBean<>();
        com.test.librarymanagement.logging.ServletAccessLogFilter filter = new ServletAccessLogFilter(webSecurityUtil, loggingProperties);
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("Access Log Filter");
        return registration;
    }
}
