# import the ca certificate and give alias CA to a new keystore 'clienttruststore'. set password to 'password'
keytool -import -file ../ca/ca.crt -alias CA -keystore servertruststore -storepass password

# generate a keypair with RSA of keysize 2048 bits to a new keystore  'serverkeystore'
keytool -genkeypair -keyalg RSA -keysize 2048 -keystore serverkeystore -storepass password -dname "cn=server, o=hospital, c=se, l=lund"

# make a Certificate Signing Request (csr) for the keystore 'serverkeystore' with password 'password'. Save request as 'serverkey.csr'
keytool -certreq -keystore serverkeystore -storepass password -file serverkey.csr 

#Signs the request 'serverkey.csr' and signs with CA. Saves to 'serverkey.crt'.
openssl x509 -req -in serverkey.csr -CA ../ca/ca.crt -CAkey ../ca/ca.key -CAcreateserial -out serverkey.crt

#First imports ca.crt and then serverkey.crt into 'serverkeystore' 
keytool -import -trustcacerts -alias root -file ca.crt -keystore serverkeystore -storepass password
keytool -import -trustcacerts -alias serverKey -file serverkey.crt -keystore serverkeystore -storepass password

rm serverkey.csr
rm serverkey.crt