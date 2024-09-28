# springboot-httpbin

A java(springboot) port of the venerable [httpbin.org](httpbin.org) HTTP request & response testing service.

Inspired by [httpbin](https://httpbin.org/) [go-httpbin](https://github.com/mccutchen/go-httpbin) and [java-httpbin](https://github.com/gaul/java-httpbin).

Special thanks [fastapi-httpbin](https://github.com/dmuth/fastapi-httpbin) and [openapi-generator](https://github.com/OpenAPITools/openapi-generator).

## usage

```bash
docker run -p 8080:8080 dyrnq/springboot-httpbin
```

| env                                    | jvm options                            | application args                       |
|----------------------------------------|----------------------------------------|----------------------------------------|
| SERVER_SERVLET_CONTEXT_PATH            | -Dserver.servlet.context-path          | --server.servlet.context-path          |
| SPRING_MVC_SERVLET_PATH                | -Dspring.mvc.servlet.path              | --spring.mvc.servlet.path              |
| OPENAPI_SPRINGBOOT_HTTPBIN_BASE_PATH   | -Dopenapi.springboot-httpbin.base-path | --openapi.springboot-httpbin.base-path |
| JAVA_OPTS                              |                                        |                                        |
| JAR  tomcat, jetty, undertow, reactive |                                        |                                        |

e.g.

```bash
docker run -p 8080:8080 -e JAR=undertow -e JAVA_OPTS='-Xms1g -Xmx1g' dyrnq/springboot-httpbin
```

options use precompile jar

```bash
curl -O -fSL -# https://github.com/dyrnq/springboot-httpbin/releases/download/v0.0.1/springboot-httpbin-tomcat.jar
java -jar springboot-httpbin-tomcat.jar
```

also see [spring-boot/appendix/application-properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html)

## original httpbin openapi

- [https://httpbin.org/spec.json](https://httpbin.org/spec.json)
- [https://httpbin.dmuth.org/openapi.json](https://httpbin.dmuth.org/openapi.json)
- [httpbin.yaml on GitHub](https://github.com/Kong/swagger-ui-kong-theme/blob/main/demo/public/specs/httpbin.yaml)

See also

| website                                | source                                                              | docker                                   |
|----------------------------------------|---------------------------------------------------------------------|------------------------------------------|
| [httpbin.org](https://httpbin.org)     | [postmanlabs/httpbin](https://github.com/postmanlabs/httpbin)       | docker run -p 80:80 kennethreitz/httpbin |
| [httpbin.rs](https://httpbin.rs)       | [duskmoon314/httpbin-rs](https://github.com/duskmoon314/httpbin-rs) |                                          |
| [httpbingo.org](https://httpbingo.org) | [mccutchen/go-httpbin](https://github.com/mccutchen/go-httpbin)     | docker run -P mccutchen/go-httpbin       |
|                                        | [gaul/java-httpbin](https://github.com/gaul/java-httpbin)           |                                          |
|                                        | [SakigamiYang/httpbin4j](https://github.com/SakigamiYang/httpbin4j) |                                          |

## ref

- <https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/spring.md>
