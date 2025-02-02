#!/usr/bin/env bash

docker run \
--rm \
--user 1000 \
-v $PWD:/local openapitools/openapi-generator-cli generate \
--input-spec /local/openapi_httpbin.yaml \
-g spring \
--group-id com.dyrnq.httpbin \
--artifact-id springboot-httpbin \
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
reactive=false,\
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
-o /local/springboot-httpbin