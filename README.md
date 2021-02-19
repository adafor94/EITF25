# Project in EITF25 - Computer Security.

Small simulation of a hospital client/server application. 

Clients (doctors, nurses, patients and government Agency) provide an accepted certificate + password 
to establish a TLS connection to the server containing patient records.

Access rules:
A patient can read his own records.
A nurse can read/write all records associated with him and read all records associated with the same division.
A doctor can read/write all records associated with him and read all records associated with the same division. And can create records. When creating a record he also associates a nurse with the record. 
A government agency can read/delete all records. 

The starting point of the simulation concist of the clients:
doc0, div0
doc1, div1
nurse0, div0
nurse1, div1
patientAlice
patientBob
govAge

and the records:
Patient: alice, Doctor: doc0, Divsion: div0, Nurse: nurse0, Comment: Broken foot
Patient: Bob, Doctor: doc1, Division: div1, Nurse: nurse1, Comment: Broken heart



