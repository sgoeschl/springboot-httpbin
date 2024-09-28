#!/usr/bin/env bash

## https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/spring.md

#curl -O --retry 100 -fSL -# https://httpbin.dmuth.org/openapi.json
set -Eeo pipefail



if ! /tmp/yq --version ; then
yq_version=v4.44.3
curl -fSL -# --retry 30 https://github.com/mikefarah/yq/releases/download/${yq_version}/yq_linux_amd64.tar.gz | \
tar -xzv --strip-components=1 -C /tmp --transform "s/yq_linux_amd64/yq/";
/tmp/yq --version
fi

#curl --retry 10 -fSL -# "https://converter.swagger.io/api/convert?url=https://httpbin.org/spec.json" -H "Accept: application/yaml" -o openapi_httpbin.yaml
#curl --retry 10 -fSL -# "https://converter.swagger.io/api/convert?url=https://httpbin.dmuth.org/openapi.json" -H "Accept: application/yaml" -o openapi_fastapi_httpbin.yaml

cp -f -v openapi_httpbin_original.yaml openapi_httpbin.yaml

# .paths.[].[].responses."200" = { "description": "description", "content" : { "application/json" : { "schema" : {} } } } |
# schema: type: string format: binary
# "image/jpeg":{},"image/webp":{},"image/png":{}, "image/svg+xml":{},
/tmp/yq -i '
.paths."/image".get.responses."200".content = { "image/*" : {} } |
.paths."/bearer".get.parameters["0"] = {"name": "Authorization","in": "header","required": false, "schema": {"type": "string" } } |
.paths."/bearer".get.parameters += [{"name": "token","in": "query","required": false,"style": "simple","schema": {"type": "string","default": "3bsvhis2g6re31r0occao8i7ph" } } ] |
.paths."/etag/{etag}".get.parameters["0"].schema = { "type": "string" } |
.paths."/etag/{etag}".get.parameters["1"].schema = { "type": "string" } |
.paths."/bytes/{n}".[].parameters[0] = {"name": "n", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/links/{n}/{offset}".[].parameters[0] = {"name": "n", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/links/{n}/{offset}".[].parameters[1] = {"name": "offset", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/range/{numbytes}".[].parameters[0] = {"name": "numbytes", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/stream-bytes/{n}".[].parameters[0] = {"name": "n", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/stream/{n}".[].parameters[0] = {"name": "n", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/redirect-to".get.parameters[1] = {"name": "status_code", "in": "query" , "required": "false" ,"schema": { "type": "integer" } } |
.paths."/redirect-to".post.requestBody.content."multipart/form-data".schema.properties.status_code = { "type": "integer" } |
.paths."/redirect-to".put.requestBody.content."multipart/form-data".schema.properties.status_code = { "type": "integer" } |
.paths."/cache".get.parameters[0].schema = { "type": "string" } |
.paths."/cache".get.parameters[1].schema = { "type": "string" } |
.paths."/response-headers".[].parameters[0].schema = { "type": "string" } |
.paths."/cookies/delete".[].parameters[0].schema = { "type": "string" } |
.paths."/cookies/set".[].parameters[0].schema = { "type": "string" } |
.paths."/absolute-redirect/{n}".[].parameters[0] = {"name": "n", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/relative-redirect/{n}".[].parameters[0] = {"name": "n", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/redirect/{n}".[].parameters[0] = {"name": "n", "in": "path" , "required": "true" ,"schema": { "type": "integer" } } |
.paths."/delay/{delay}".[].parameters[0] = {"name": "delay", "in": "path" , "required": "true" ,"style": "simple" , "schema": { "type": "integer" } } |
.paths."/etag/{etag}".get.parameters += [ {"name": "etag", "in": "path" , "required": "true" ,"style": "simple" , "schema": { "type": "string" } } ] |
.paths."/anything/{anything}".[].parameters += [{"name": "anything","in": "path","required": true,"style": "simple","schema": {"type": "string"} } ] |
.paths."/status/{codes}".[].parameters = [{"name": "codes","in": "path","required": true,"style": "simple","schema": {"type": "string"} } ] |
.info.version = "0.0.1" | 
.info.contact.email = "dyrnq@outlook.com" | 
.info.contact.url = "https://github.com/dyrnq" | 
.info.description = "springboot-httpbin" | 
.info.title = "springboot-httpbin"
' openapi_httpbin.yaml || exit 1;


#mkdir -p backup/springboot-httpbin/src/main/java/com/dyrnq/httpbin/api
#mkdir -p backup/springboot-httpbin/src/main/java/org/gaul/httpbin
#cp -R springboot-httpbin/src/main/java/com/dyrnq/httpbin/api  backup/springboot-httpbin/src/main/java/com/dyrnq/httpbin
#cp -R springboot-httpbin/src/main/java/org/gaul/httpbin       backup/springboot-httpbin/src/main/java/org/gaul
#rm -rf backup/springboot-httpbin/src/main/java/com/dyrnq/httpbin/api/*Api.java
#
#rm -rf springboot-httpbin-reactive
#rm -rf springboot-httpbin



for type in "springboot-httpbin-reactive" "springboot-httpbin"; do

reactive_var="reactive=false"
artifactId="${type}"
if [ "springboot-httpbin-reactive" = "${type}" ]; then
  reactive_var="reactive=true"
fi

docker run \
--rm \
--user 1000 \
-v $PWD:/local openapitools/openapi-generator-cli generate \
--input-spec /local/openapi_httpbin.yaml \
-g spring \
--group-id com.dyrnq.httpbin \
--artifact-id ${artifactId} \
--artifact-version 0.0.1 \
--invoker-package com.dyrnq.httpbin \
--api-package com.dyrnq.httpbin.api \
--model-package com.dyrnq.httpbin.model \
--git-repo-id dyrnq \
--additional-properties \
generateBuilders=true,\
developerEmail=dyrnq@qq.com,\
developerName=dyrnq,\
developerOrganization=https://github.com/dyrnq,\
developerOrganizationUrl=https://github.com/dyrnq,\
delegatePattern=false,\
title=springboot-httpbin,\
useTags=true,\
${reactive_var},\
hideGenerationTimestamp=true,\
useSpringBoot3=true,\
artifactDescription=springboot-httpbin,\
artifactUrl=https://github.com/dyrnq/springboot-httpbin,\
configPackage=com.dyrnq.httpbin.configuration,\
parentArtifactId=springboot-httpbin-root,\
parentGroupId=com.dyrnq.httpbin,\
parentVersion=0.0.1,\
unhandledException=true,\
useOneOfInterfaces=true,\
bigDecimalAsString=true,\
useOptional=false \
\
-o /local/${type}

cp -v -f scripts/SpringDocConfiguration.java ${type}/src/main/java/com/dyrnq/httpbin/configuration/SpringDocConfiguration.java
rm -rf ${type}/README.md
cat < <(cat <<EOF
springdoc.swagger-ui.doc-expansion=none
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.filter=true
springdoc.swagger-ui.operationsSorter=alpha
springdoc.swagger-ui.showExtensions=false
springdoc.swagger-ui.tagsSorter=alpha
EOF
) | tee -a ${type}/src/main/resources/application.properties

done


CONTENT="
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-osx-aarch64</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-osx-aarch64-1.17.0.jar</systemPath>
</dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-osx-x86_64</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-osx-x86_64-1.17.0.jar</systemPath>
</dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-windows-x86_64</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-windows-x86_64-1.17.0.jar</systemPath>
</dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-windows-aarch64</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-windows-aarch64-1.17.0.jar</systemPath>
</dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-linux-x86_64</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-linux-x86_64-1.17.0.jar</systemPath>
</dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-linux-aarch64</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-linux-aarch64-1.17.0.jar</systemPath>
</dependency>
<dependency>
<groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-linux-armv7</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-linux-armv7-1.17.0.jar</systemPath>
  </dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-linux-s390x</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-linux-s390x-1.17.0.jar</systemPath>
</dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-linux-riscv64</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-linux-riscv64-1.17.0.jar</systemPath>
</dependency>
<dependency>
  <groupId>com.aayushatharva.brotli4j</groupId>
  <artifactId>native-linux-ppc64le</artifactId>
  <version>1.17.0</version>
  <scope>system</scope>
  <systemPath>\${project.basedir}/../common/lib/native-linux-ppc64le-1.17.0.jar</systemPath>
</dependency>
"

CONTENT="${CONTENT//$'\n'/}" ## remove LF


sed -i "/.*<\/dependencies>/i $CONTENT" springboot-httpbin/pom.xml
sed -i "/.*<\/configuration>/i <includeSystemScope>true</includeSystemScope>" springboot-httpbin/pom.xml

rm -rf springboot-httpbin/src/main/java/com/dyrnq/httpbin/api/*ApiController.java

sed -i "s@multipart/form-data@*/*@g" springboot-httpbin/src/main/java/com/dyrnq/httpbin/api/RedirectsApi.java

#cp -r -v backup/springboot-httpbin ./

# rm -rf /tmp/httpbin
# rm -rf /tmp/java-httpbin
# git clone git@github.com:postmanlabs/httpbin.git /tmp/httpbin
# git clone git@github.com:gaul/java-httpbin.git /tmp/java-httpbin

cp -v -f -R /tmp/httpbin/httpbin/templates/images springboot-httpbin/src/main/resources
cp -v -f -R /tmp/httpbin/httpbin/templates/moby.html springboot-httpbin/src/main/resources
cp -v -f -R /tmp/httpbin/httpbin/templates/sample.xml springboot-httpbin/src/main/resources
cp -v -f -R /tmp/httpbin/httpbin/templates/UTF-8-demo.txt springboot-httpbin/src/main/resources

