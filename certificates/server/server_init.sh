keytool -import -file ../ca/ca.crt -alias CA -keystore servertruststore -storepass password

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore serverkeystore -storepass password -dname "cn=myserver, ou=hospital, st=hospital, o=hospital, c=se, l=lund"

keytool -certreq -keystore serverkeystore -storepass password -file serverkey.csr 

openssl x509 -req -in serverkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out serverkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore serverkeystore -storepass password

keytool -import -trustcacerts -alias myKey -file serverkey.crt -keystore serverkeystore -storepass password

rm serverkey.csr
rm serverkey.crt