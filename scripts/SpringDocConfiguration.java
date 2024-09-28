package com.dyrnq.httpbin.configuration;

import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfiguration {

    //EXTERNAL_URL="https://domain.com,https://httpbin.domain.com"
    @Value("${external.url:}")
    private String[] externalUrl;

    @Bean(name = "com.dyrnq.httpbin.configuration.SpringDocConfiguration.apiInfo")
    OpenAPI apiInfo() {
        OpenAPI openAPI = new OpenAPI()
                .info(
                        new Info()
                                .title("springboot-httpbin")
                                .description("""
                                        \nA simple HTTP Request & Response Service.\n
                                        <p>\n\n<b>Run locally:</b> <code>$ docker run -p 8080:8080 dyrnq/springboot-httpbin</code>\n\n
                                        <a href="https://github.com/dyrnq/springboot-httpbin" target="_blank">the developer - Website</a>\n\n
                                        <a href="https://hub.docker.com/r/dyrnq/springboot-httpbin/tags" target="_blank">the docker images</a>
                                        """)
                                .version("0.0.1")
                );
        if (externalUrl != null && externalUrl.length > 0) {
            for (String s : externalUrl) {
                if (StringUtils.isNotBlank(s)) {
                    openAPI.addServersItem(new Server().url(s));
                }
            }

        }
        return openAPI;
    }
}