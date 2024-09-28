package com.dyrnq.httpbin.configuration;

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

    @Bean(name = "com.dyrnq.httpbin.configuration.SpringDocConfiguration.apiInfo")
    OpenAPI apiInfo() {
        return new OpenAPI()
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
                )
                ;
    }
}