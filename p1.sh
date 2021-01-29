#1.
#Generate a rsa key and save it as ca.key
openssl genrsa -out ca.key 	

# Generate a self signed x509-certificate. save as ca.crt
openssl req -new -x509 -key ca.key -out ca.crt

#2.
# import the specified certificate and give alias CA to a new keystore 'clienttruststore'
keytool -import -file ./ca.crt -alias CA -keystore clienttruststore 

#3.
# generate a keypair with RSA of keysize 2048 bits to a new keystore  'clientkeystore'
keytool -genkeypair -keyalg RSA -keysize 2048 -keystore clientkeystore 

#4.
# make a Certificate Signing Request (csr) for the keystore 'clientkeystore' with password 'password'. Save request as 'clientkey.csr
keytool -certreq -keystore clientkeystore -storepass password -file clientkey.csr 

#5.
#Signs the request 'clientkey.csr' and signs with CA. Saves to 'clientkey.crt'. (A file called ca.srl is created which i think contains all issued serial numbers)
openssl x509 -req -in clientkey.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out clientkey.crt

#6. 



