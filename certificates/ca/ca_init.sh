openssl genrsa -out ca.key 	

openssl req -new -x509 -key ca.key -out ca.crt
