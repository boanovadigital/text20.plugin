#!/bin/bash

# Create your own key like this (change -validity from 60 to something higher, and the -dname as well)   
keytool  -genkey -v  -alias text20 -keystore text20keystore.p12 -validity 60 -keypass replacethis -new replacethis -keyalg rsa -keysize 512 -storetype PKCS12 -dname "cn=MALICIOUS TEST CERTIFICATE, ou=Radioactive Labs, o=Danger Inc., c=Moon"
