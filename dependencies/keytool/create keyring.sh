#!/bin/bash


#keytool -genkey -keystore augmentedtext.keystore -alias augmentedtext -keypass replacethis -new replacethis
#keytool  -keyalg rsa -keysize 1024 -storetype PKCS12 -selfcert -keystore augmentedtext.keystore  -alias augmentedtext -validity 1000 -keypass replacethis -new replacethis -dname "cn=Ralf Biedert, ou=KM Department, o=DFKI, c=de"

keytool  -genkey -v  -alias text20 -keystore text20keystore.p12 -validity 60 -keypass replacethis -new replacethis -keyalg rsa -keysize 512 -storetype PKCS12 -dname "cn=MALICIOUS TEST CERTIFICATE, ou=Radioactive Labs, o=Danger Inc., c=Moon"

#keytool -selfcert -keystore augmentedtextkeystore.p12  -alias augmentedtext -validity 1000 -keypass replacethis -new replacethis -keyalg rsa -keysize 512 -storetype PKCS12 -dname "cn=Ralf Biedert, ou=KM Department, o=DFKI, c=de"
# keytool -export -keystore augmentedtextkeystore.p12 -alias augmentedtext -keypass replacethis -new replacethis -file augmentedtext.cert