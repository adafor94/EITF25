# import the ca certificate and give alias CA to a new keystore 'clienttruststore'. set password to 'password'. This is the same for all clients. 
keytool -import -file ../ca/ca.crt -alias CA -keystore clienttruststore -storepass password 

#GOVERNANCE AGENCY

# generate a keypair with RSA of keysize 2048 bits to a new keystore
keytool -genkeypair -keyalg RSA -keysize 2048 -keystore govAgeks -storepass password -dname "cn=govAge, ou=gov, st=govAge, o=gov, c=se, l=lund"

# make a Certificate Signing Request (csr) for the keystore. Save request as 'clientkey.csr
keytool -certreq -keystore govAgeks -storepass password -file clientkey.csr 

#Signs the request with CA. Saves to 'clientkey.crt'.
openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

#imports ca.crt into clientkeystore 
keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore govAgeks -storepass password

#import clientkey.crt into clientkeystore. 
keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore govAgeks -storepass password

rm clientkey.csr
rm clientkey.crt

#DOCTOR0, div0

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore doc0ks -storepass password -dname "cn=doc0, ou=div0, st=doctor, o=hospital, c=se, l=lund"

keytool -certreq -keystore doc0ks -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore doc0ks -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore doc0ks -storepass password

rm clientkey.csr
rm clientkey.crt

#DOCTOR1, div1

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore doc1ks -storepass password -dname "cn=doc1, ou=div1, st=doctor, o=hospital, c=se, l=lund"

keytool -certreq -keystore doc1ks -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore doc1ks -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore doc1ks -storepass password

rm clientkey.csr
rm clientkey.crt

#NURSE0, div0

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore nurse0ks -storepass password -dname "cn=nurse0, ou=div0, st=nurse, o=hospital, c=se, l=lund"

keytool -certreq -keystore nurse0ks -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore nurse0ks -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore nurse0ks -storepass password

rm clientkey.csr
rm clientkey.crt

#NURSE1, div1

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore nurse1ks -storepass password -dname "cn=nurse1, ou=div1, st=nurse, o=hospital, c=se, l=lund"

keytool -certreq -keystore nurse1ks -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore nurse1ks -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore nurse1ks -storepass password

rm clientkey.csr
rm clientkey.crt

#Patient Alice

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore patientAliceks -storepass password -dname "cn=alice, st=patient, o=hospital, c=se, l=lund"

keytool -certreq -keystore patientAliceks -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore patientAliceks -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore patientAliceks -storepass password

rm clientkey.csr
rm clientkey.crt

#Patient Bob

keytool -genkeypair -keyalg RSA -keysize 2048 -keystore patientBobks -storepass password -dname "cn=bob, st=patient, o=hospital, c=se, l=lund"

keytool -certreq -keystore patientBobks -storepass password -file clientkey.csr 

openssl x509 -req -in clientkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out clientkey.crt

keytool -import -trustcacerts -alias root -file ../ca/ca.crt -keystore patientBobks -storepass password

keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore patientBobks -storepass password

rm clientkey.csr
rm clientkey.crt