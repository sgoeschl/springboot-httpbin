#!/usr/bin/env bash
set -eo pipefail

_main() {
    JAR="${JAR:-tomcat}"
    FULL_JAR="${FULL_JAR:-/app/springboot-httpbin-${JAR}.jar}"
    exec java $JAVA_OPTS -jar "${FULL_JAR}" "$@"
}

_main "$@"