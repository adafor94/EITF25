#PART 1

#1.
#Generate a rsa key and save it as ca.key
openssl genrsa -out ca.key 	
#A:

# Generate a self signed x509-certificate. save as ca.crt
openssl req -new -x509 -key ca.key -out ca.crt

#2.
# import the specified certificate and give alias CA to a new keystore 'clienttruststore'. set password to 'password'
keytool -import -file ca.crt -alias CA -keystore clienttruststore -storepass password 

#3.
# generate a keypair with RSA of keysize 2048 bits to a new keystore  'clientkeystore'
keytool -genkeypair -keyalg RSA -keysize 2048 -keystore clientkeystore -storepass password

#4.
# make a Certificate Signing Request (csr) for the keystore 'clientkeystore' with password 'password'. Save request as 'clientkey.csr
keytool -certreq -keystore clientkeystore -storepass password -file clientkey.csr 

#5.
#Signs the request 'clientkey.csr' and signs with CA. Saves to 'clientkey.crt'. (A file called ca.srl is created which i think contains all issued serial numbers)
openssl x509 -req -in clientkey.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out clientkey.crt

#6.
#First imports ca.crt and then clientkey.crt into clientkeystore 
keytool -import -trustcacerts -alias root -file ca.crt -keystore clientkeystore -storepass password
keytool -import -trustcacerts -alias myKey -file clientkey.crt -keystore clientkeystore -storepass password

#7.
#Lists all certificates in clientkeystore. I think it looks alright.
keytool -list -v -keystore clientkeystore -storepass password

#8.
#B: 
#C: 

#9.
# generate a keypair with RSA of keysize 2048 bits to a new keystore  'serverkeystore'
keytool -genkeypair -keyalg RSA -keysize 2048 -keystore serverkeystore -storepass password

# make a Certificate Signing Request (csr) for the keystore 'serverkeystore' with password 'password'. Save request as 'serverkey.csr'
keytool -certreq -keystore serverkeystore -storepass password -file serverkey.csr 

#Signs the request 'serverkey.csr' and signs with CA. Saves to 'serverkey.crt'.
openssl x509 -req -in serverkey.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out serverkey.crt

#First imports ca.crt and then serverkey.crt into 'serverkey' 
keytool -import -trustcacerts -alias root -file ca.crt -keystore serverkeystore -storepass password
keytool -import -trustcacerts -file serverkey.crt -keystore serverkeystore -storepass password

#Lists all certificates in clientkeystore. I think it looks alright.
#keytool -list -v -keystore clientkeystore -storepass password

#10.
# import the specified certificate and give alias CA to a new keystore 'clienttruststore'. set password to 'password'
keytool -import -file ./ca.crt -alias CA -keystore servertruststore -storepass password
#D: 

#11.
#E: 

#PART 2

#1
javac server.java client.java

#2
terminal -e java server 9875
terminal -e java client localhost 9875

#3 
#F: The message reversed

#7
#G: Sets the SSL-socket to require client authentication. 


