#fakeCA
openssl genrsa -out FAKEca.key 	

openssl req -new -x509 -key FAKEca.key -out FAKEca.crt

#fakeUser

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore fakeUserks -storepass password -dname "cn=fakeUser, ou=div0, st=doctor, o=hospital, c=se, l=lund"

keytool -certreq -keystore fakeUserks -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ./FAKEca.crt -CAkey ./FAKEca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ./FAKEca.crt -keystore fakeUserks -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore fakeUserks -storepass password

rm clientkey.csr
rm clientkey.crt