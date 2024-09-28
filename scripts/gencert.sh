#!/usr/bin/env bash

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" > /dev/null 2>&1 && pwd -P)
pushd ${SCRIPT_DIR}/../ >/dev/null 2>&1 || exit 1

mkdir -p certs
pushd certs || exit 1

if [ ! -f ca.key ]; then
    openssl genrsa -out ca.key 4096
    openssl req -x509 -new -nodes -sha512 -days 3650 -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=CA" -key ca.key -out ca.crt
fi
#################################################################

openssl genrsa -out server.key 4096
openssl req -sha512 -new -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=server" -key server.key -out server.csr

cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=server
DNS.2=localhost
IP.1=127.0.0.1
IP.2=192.168.6.171
EOF

openssl x509 -req -sha512 -days 3650 -extfile v3.ext -CA ca.crt -CAkey ca.key -CAcreateserial -in server.csr -out server.crt

#################################################################
openssl genrsa -out client.key 4096
openssl req -sha512 -new -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=client" -key client.key -out client.csr

cat > v3_client.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = clientAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=client
DNS.2=localhost
IP.1=127.0.0.1
IP.2=192.168.6.171
EOF

openssl x509 -req -sha512 -days 3650 -extfile v3_client.ext -CA ca.crt -CAkey ca.key -CAcreateserial -in client.csr -out client.crt



# https://stackoverflow.com/questions/30928993/https-connection-is-not-working-spring-boot
# openssl pkcs12 -export -in <mycert.crt> -inkey <mykey.key> -out <mycert.p12> -name tomcat -CAfile <myca.ca> -caname root -chain

if [ -f ca.p12 ]; then rm -rf ca.p12; fi
if [ -f truststore.jks ]; then rm -rf truststore.jks; fi;



# https://github.com/Hakky54/sslcontext-kickstart/issues/549
# why ca.p12 can not use openssl ?
# openssl pkcs12 -export -inkey ca.key -in ca.crt -out ca-openssl.p12 -passout pass:changeit
# openssl version
# OpenSSL 3.0.13 30 Jan 2024 (Library: OpenSSL 3.0.13 30 Jan 2024)
# -jdktrust anyExtendedKeyUsage
# docker run -it --rm -v $(pwd):/certs alpine/openssl:3.3.2 version

# correct way
#

if command -v docker ; then
  docker run -it -u $(id -u):$(id -g) --rm -v $(pwd):/certs alpine/openssl:3.3.2 pkcs12 -export -jdktrust anyExtendedKeyUsage -inkey /certs/ca.key -in /certs/ca.crt -out /certs/ca.p12 -passout pass:changeit
else
  keytool -importcert -noprompt -trustcacerts -alias ca -file ca.crt -storetype PKCS12  -storepass changeit -keystore ca.p12
fi

keytool -importcert -noprompt -trustcacerts -alias ca -file ca.crt -storetype JKS     -storepass changeit -keystore truststore.jks
openssl pkcs12 -export -inkey server.key -in server.crt -out server.p12 -chain -CAfile ca.crt -name server -caname ca -passout pass:changeit
openssl pkcs12 -export -inkey client.key -in client.crt -out client.p12 -chain -CAfile ca.crt -name client -caname ca -passout pass:changeit

keytool -list -v -keystore truststore.jks -storetype JKS    -storepass changeit
keytool -list -v -keystore ca.p12         -storetype PKCS12 -storepass changeit
keytool -list -v -keystore server.p12     -storetype PKCS12 -storepass changeit
keytool -list -v -keystore client.p12     -storetype PKCS12 -storepass changeit




echo "######################open server.ssl.enabled PKCS12######################"
cat <<EOF
--server.ssl.enabled=true
--server.ssl.key-store=certs/server.p12
--server.ssl.key-store-type=PKCS12
--server.ssl.key-password=changeit
--server.ssl.key-store-password=changeit
--server.ssl.trust-store=certs/ca.p12
--server.ssl.trust-store-password=changeit
--server.ssl.trust-store-type=PKCS12
--server.ssl.client-auth=NONE
EOF

echo "######################open server.ssl.enabled PEM######################"
cat <<EOF
--server.ssl.enabled=true
--server.ssl.certificate=certs/server.crt
--server.ssl.certificate-private-key=certs/server.key
--server.ssl.trust-certificate=certs/ca.crt
--server.ssl.client-auth=NONE
EOF

echo "######################CURL TEST CMD BEGIN######################"

echo "curl --cacert certs/ca.crt --cert certs/client.crt --key certs/client.key https://127.0.0.1:8080/get"
echo "curl --cacert certs/ca.crt --cert certs/client.p12:changeit --cert-type P12 https://127.0.0.1:8080/get"
echo "######################CURL TEST CMD END######################"

openssl x509 -in server.crt -noout -text |grep -A2 Alternative
openssl x509 -in client.crt -noout -text |grep -A2 Alternative
openssl verify -CAfile ca.crt server.crt
openssl verify -CAfile ca.crt client.crt
# certs/client.crt: OK
popd || exit 1

popd || exit 1