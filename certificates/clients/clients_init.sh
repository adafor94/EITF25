# import the ca certificate and give alias CA to a new keystore 'clienttruststore'. set password to 'password'. This is the same for all clients. 
keytool -import -file ../ca/ca.crt -alias CA -keystore clienttruststore -storepass password 

#GOVERNANCE AGENCY

# generate a keypair with RSA of keysize 2048 bits to a new keystore
keytool -genkeypair -keyalg RSA -keysize 2048 -keystore governmentAgencykeystore -storepass password -dname "cn=governmentAgency, ou=gov, st=gov, o=gov, c=se, l=lund"

# make a Certificate Signing Request (csr) for the keystore. Save request as 'clientkey.csr
keytool -certreq -keystore governmentAgencykeystore -storepass password -file clientkey.csr 

#Signs the request with CA. Saves to 'clientkey.crt'.
openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

#imports ca.crt into clientkeystore 
keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore governmentAgencykeystore -storepass password

#import clientkey.crt into clientkeystore. 
keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore governmentAgencykeystore -storepass password

rm clientkey.csr
rm clientkey.crt

#DOCTOR0, div0

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore doc0keystore -storepass password -dname "cn=doc0, ou=div0, st=doctor, o=hospital, c=se, l=lund"

keytool -certreq -keystore doc0keystore -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore doc0keystore -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore doc0keystore -storepass password

rm clientkey.csr
rm clientkey.crt

#DOCTOR1, div1

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore doc1keystore -storepass password -dname "cn=doc1, ou=div1, st=doctor, o=hospital, c=se, l=lund"

keytool -certreq -keystore doc1keystore -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore doc1keystore -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore doc1keystore -storepass password

rm clientkey.csr
rm clientkey.crt

#NURSE0, div0

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore nurse0keystore -storepass password -dname "cn=nurse0, ou=div0, st=nurse, o=hospital, c=se, l=lund"

keytool -certreq -keystore nurse0keystore -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore nurse0keystore -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore nurse0keystore -storepass password

rm clientkey.csr
rm clientkey.crt