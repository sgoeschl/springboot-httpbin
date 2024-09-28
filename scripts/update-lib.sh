#!/usr/bin/env bash
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" > /dev/null 2>&1 && pwd -P)
pushd ${SCRIPT_DIR}/../common/lib >/dev/null 2>&1 || exit 1

version='1.17.0'

for platform in 'native-osx-aarch64' \
                'native-osx-x86_64' \
                'native-windows-x86_64' \
                'native-windows-aarch64' \
                'native-linux-x86_64' \
                'native-linux-aarch64' \
                'native-linux-armv7' \
                'native-linux-s390x' \
                'native-linux-riscv64' \
                'native-linux-ppc64le'
do
  curl  -OJ -# "https://repo1.maven.org/maven2/com/aayushatharva/brotli4j/${platform}/${version}/${platform}-${version}.jar"

#cat <<EOF
#<dependency>
#    <groupId>com.aayushatharva.brotli4j</groupId>
#    <artifactId>${platform}</artifactId>
#    <version>${version}</version>
#    <scope>system</scope>
#    <systemPath>\${project.basedir}/../common/lib/${platform}-${version}.jar</systemPath>
#</dependency>
#EOF
done
popd >/dev/null 2>&1 || exit 1
