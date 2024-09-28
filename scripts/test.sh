#!/usr/bin/env bash
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" > /dev/null 2>&1 && pwd -P)

if [ ! -f ./h.sh ]; then
  pushd "${SCRIPT_DIR}" >/dev/null 2>&1 || exit 1
fi

./h.sh --arg "-j" -m GET     -l "get"
./h.sh --arg "-j" -m GET     -l "get?query=bb"
./h.sh --arg "-j" -m GET     -l "get?query=bb&key=中文"
./h.sh --arg "-f" -m POST    -l "post"   -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m POST    -l "post"   -d "value=hello test=你好 公司=微软"
./h.sh --arg "-f" -m PUT     -l "put"    -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m PUT     -l "put"    -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m DELETE  -l "delete" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m GET     -l "basic-auth/user/pass" -d "value=hello test=你好 公司=微软" -a "user:pass" -A "basic"
./h.sh --arg "-j" -m GET     -l "bearer" -d "value=hello test=你好 公司=微软" -a "dddddddddddddddddddd" -A "bearer"
./h.sh --arg "-j" -m GET     -l "digest-auth/auth/tomcat/passwd/MD5" -d "value=hello test=你好 公司=微软" -a "tomcat:passwd" -A "digest"

./h.sh --arg "-j" -m DELETE  -l "status/103" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m POST    -l "status/301" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m GET     -l "status/400" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m PATCH   -l "status/500" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m PUT     -l "status/307" -d "value=hello test=你好 公司=微软"

./h.sh --arg "-j" -m GET     -l "headers"
./h.sh --arg "-j" -m GET     -l "ip"
./h.sh --arg "-j" -m GET     -l "user-agent"

./h.sh --arg "-j" -m GET     -l "cache" -d "If-Modified-Since:-"
./h.sh --arg "-j" -m GET     -l "cache"
./h.sh --arg "-j" -m GET     -l "cache/3"
./h.sh --arg "-j" -m GET     -l "etag/etagetagetagetagetag"
./h.sh --arg "-j" -m GET     -l "etag/etagetagetagetagetag" -d "If-Match:etagetagetagetagetag"
./h.sh --arg "-j" -m GET     -l "etag/etagetagetagetagetag" -d "If-None-Match:-"
./h.sh --arg "-j" -m GET     -l "response-headers?freeform="
./h.sh --arg "-j" -m POST    -l "response-headers?freeform=hello"


./h.sh --arg "-j" -m GET     -l "deny"
./h.sh --arg "-j" -m GET     -l "encoding/utf8"
./h.sh --arg "-j" -m GET     -l "html"
./h.sh --arg "-j" -m GET     -l "xml"
./h.sh --arg "-j" -m GET     -l "json"
./h.sh --arg "-j" -m GET     -l "robots.txt"


./h.sh --arg "-j" -m GET     -l "base64/SFRUUEJJTiBpcyBhd2Vzb21l"
./h.sh --arg "-j" -m GET     -l "bytes/10"
./h.sh --arg "-j" -m GET     -l "delay/2"
./h.sh --arg "-j" -m GET     -l "drip?duration=2&numbytes=10&code=200&delay=2"
./h.sh --arg "-j" -m GET     -l "links/10/8"
./h.sh --arg "-j" -m GET     -l "range/10"
./h.sh --arg "-j" -m GET     -l "stream-bytes/10"
./h.sh --arg "-j" -m GET     -l "stream/10"
./h.sh --arg "-j" -m GET     -l "uuid"



./h.sh --arg "-j" -m GET     -l "cookies" --session /tmp/mysession
./h.sh --arg "-j" -m GET     -l "cookies/set?freeform=base64" --session /tmp/mysession
./h.sh --arg "-j" -m GET     -l "cookies/set/jenkins/v1999" --session /tmp/mysession




./h.sh --arg "-j" -m DELETE       -l "redirect-to"    --data "url==https://httpbin.org/anything status_code==301"
./h.sh --arg "-j" -m PATCH        -l "redirect-to"    --data "url==https://httpbin.org/anything status_code==301"

./h.sh --arg "-j"           -m GET  -l "redirect-to" --data "url==https://httpbin.org/anything status_code==301"
./h.sh --arg "--multipart"  -m POST -l "redirect-to" --data "url==https://httpbin.org/anything status_code==301"
./h.sh --arg "-j"           -m PUT  -l "redirect-to" --data "url==https://httpbin.org/anything status_code==301"

./h.sh --arg "-j" -m GET -l "redirect-to/2"
# ./h.sh --arg "-j" -m GET -l "relative-redirect/1"
# ./h.sh --arg "-j" -m GET -l "absolute-redirect/1"





./h.sh --arg "-j" -m GET      -l "anything" -d "value==hello test=你好 公司=微软"
./h.sh --arg "-j" -m POST     -l "anything" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m DELETE   -l "anything" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m PATCH    -l "anything" -d "value=hello test=你好 公司=微软"
./h.sh --arg "-j" -m PUT      -l "anything" -d "value=hello test=你好 公司=微软"




for img in "relative-redirect/1" "absolute-redirect/1" ;do
(
curl -fsiL -X GET "http://127.0.0.1:8080/$img" > /tmp/springboot-httpbin.txt
curl -fsiL -X GET "https://httpbin.org/$img" > /tmp/httpbin.txt
diff -y /tmp/springboot-httpbin.txt /tmp/httpbin.txt
echo ""
)
done


for img in "jpeg" "png" "webp" "svg+xml";do
(
curl -fsL -X GET "http://127.0.0.1:8080/image" -H "accept: image/${img}"  | base64 --wrap=0 > /tmp/springboot-httpbin.txt
curl -fsL -X GET "https://httpbin.org/image" -H "accept: image/${img}"    | base64 --wrap=0 > /tmp/httpbin.txt
diff -y /tmp/springboot-httpbin.txt /tmp/httpbin.txt
echo ""
)
done

for img in "jpeg" "png" "webp" "svg";do
(
curl -fsL -X GET "http://127.0.0.1:8080/image/$img" -H "accept: image/*"  | base64 --wrap=0 > /tmp/springboot-httpbin.txt
curl -fsL -X GET "https://httpbin.org/image/$img" -H "accept: image/*"    | base64 --wrap=0 > /tmp/httpbin.txt
diff -y /tmp/springboot-httpbin.txt /tmp/httpbin.txt
echo ""
)
done




(
curl -fsL -X GET "http://127.0.0.1:8080/brotli" -H "accept: application/json" | brotli -d > /tmp/springboot-httpbin.txt
curl -fsL -X GET "https://httpbin.org/brotli" -H "accept: application/json" | brotli -d > /tmp/httpbin.txt
diff -y /tmp/springboot-httpbin.txt /tmp/httpbin.txt
)

(
curl -fsL -X GET "http://127.0.0.1:8080/gzip" -H "accept: application/json" | gzip -d > /tmp/springboot-httpbin.txt
curl -fsL -X GET "https://httpbin.org/gzip" -H "accept: application/json" | gzip -d > /tmp/httpbin.txt
diff -y /tmp/springboot-httpbin.txt /tmp/httpbin.txt
)