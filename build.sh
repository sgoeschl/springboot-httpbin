#!/usr/bin/env bash
set -Eeo pipefail
rm -rf target
mkdir -p target

if ! /tmp/xq --version ; then
  curl -fSL# --retry 10 https://github.com/sibprogrammer/xq/releases/download/v1.2.5/xq_1.2.5_linux_amd64.tar.gz | tar -xvz -C /tmp
fi

ver=$(cat pom.xml | /tmp/xq -x /project/version)

pushd springboot-httpbin || exit 1

for ct in "tomcat" "undertow" "jetty" ; do

pom="pom.xml"

if [ "${ct}" = "tomcat" ]; then
  :
else
  pom="pom-${ct}".xml




sed \
-e "s@<artifactId>spring-boot-starter-web</artifactId>@<artifactId>spring-boot-starter-web</artifactId><exclusions><exclusion><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-tomcat</artifactId></exclusion></exclusions>@g" \
-e "s@<dependencies>@<dependencies><dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-${ct}</artifactId></dependency>@" \
pom.xml > ${pom}


fi
mvn clean package -Dmaven.test.skip=true --file "${pom}"



cp -v -f target/springboot-httpbin-${ver}.jar ../target/springboot-httpbin-${ct}.jar
done

popd || ext 1


pushd springboot-httpbin-reactive || exit 1
mvn clean package -Dmaven.test.skip=true
cp -v -f target/springboot-httpbin-reactive-${ver}.jar ../target/springboot-httpbin-reactive.jar
popd || ext 1






