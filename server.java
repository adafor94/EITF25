import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.net.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import util.*;

public class server implements Runnable {
    private ServerSocket serverSocket = null;
    private static int numConnectedClients = 0;
    private HashMap<String, Record> recordDB = new HashMap<String, Record>();
    private Log log = new Log();
    private HashMap<String, String> lastLogIn = new HashMap<String,String>();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private final String OPTIONS = "Options:\n 1. Create new record\n 2. Read record\n 3. Write to record\n 4. Delete record";

    public server(ServerSocket ss) throws IOException {
        serverSocket = ss;
        newListener();

        Record Alice = new Record("alice", "doc0", "div0", "nurse0", "Broken foot");
        Record Bob = new Record("bob", "doc1", "div1", "nurse0", "Broken heart");
        recordDB.put("alice", Alice);
        recordDB.put("bob", Bob);
    }

    public void run() {
        try {
            SSLSocket socket=(SSLSocket)serverSocket.accept();
            
            newListener();
            // String[] enabledCipherSuites = socket.getEnabledCipherSuites();
            // for (String suite : enabledCipherSuites) {
            //     System.out.println(s);
            // }

            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
            String subject = cert.getSubjectDN().getName();
            String issuer = cert.getIssuerDN().getName();
            String serialNumber = cert.getSerialNumber().toString();

            CurrentClient cc;
            cc = new CurrentClient(subject);


    	    numConnectedClients++;
            System.out.println("client connected");
            System.out.println("client name (cert subject DN field): " + subject);
            System.out.println("Issuer on certificate received from client:\n" + issuer + "\n");
            System.out.println("Serial number of certificate:\n" + serialNumber + "\n");

            System.out.println(numConnectedClients + " concurrent connection(s)\n");

            LocalDateTime time = LocalDateTime.now();
            String formattedTime = dtf.format(time);
            String userLastLogIn = "Last log in: " + lastLogIn.getOrDefault(serialNumber, "First log in") + "\n\n";
            lastLogIn.put(serialNumber, formattedTime);

            PrintWriter out = null;
            BufferedReader in = null;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientMsg = null;
            String record = null;
            
            out.println(userLastLogIn + OPTIONS);

            while ((clientMsg = in.readLine()) != null) {
                out.println("Please enter the name of the record:");
                record = in.readLine();
                int option; 
                try {
                    option = Integer.parseInt(clientMsg);
                } catch (Exception e) {
                    option = -1;
                }
                Boolean access = accessControl(cc, clientMsg, record);
                // if (clientMsg.equals("9")) {
                //     out.println(cc.getName());
                // }
                if (clientMsg.equals("10")){
                    out.println(this.log.getText());
                } else if (option < 1 || option > 4) {
                    out.println("\nSomething went wrong \n\n" + OPTIONS);
                } else if (!access) {
                    out.println("Access denied or no such record");
                } else {
                    if (clientMsg.equals("1")) {
                        createRecord(cc, record, out, in);
                        out.println("Done \n" + OPTIONS);

                    } else if (clientMsg.equals("2")) {
                        out.println(recordDB.get(record).printable());

                    } else if (clientMsg.equals("3")) {
                        appendToRecord(record, out, in);
                        out.println("Done \n" + OPTIONS);
                    } else if (clientMsg.equals("4")) {
                        recordDB.remove(record);
                        out.println("Done \n" + OPTIONS);
                    } else {
                        out.println("\nSomething went wrong \n\n" + OPTIONS);
                    }
                }
                this.log.newLogEntry(cc, clientMsg, record, access);
				out.flush();
			}
			in.close();
			out.close();
			socket.close();
    	    numConnectedClients--;
            System.out.println("client disconnected");
            System.out.println(numConnectedClients + " concurrent connection(s)\n");
		} catch (IOException e) {
            System.out.println("Client died: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    private void createRecord(CurrentClient cc, String record, PrintWriter out, BufferedReader in) throws IOException {
        out.println("Nurse: \n");
        String nurse = in.readLine();
        out.println("Comment: \n");
        String comment = in.readLine();
        Record newRecord = new Record(record, cc.getName(), cc.getDivision(), nurse, comment);
        recordDB.put(record, newRecord);
    }

    private void appendToRecord(String record, PrintWriter out, BufferedReader in) throws IOException {
        out.println("Write line to add to record: \n");
        String line = in.readLine();
        recordDB.get(record).appendComment(line);
    }

    private Boolean accessControl(CurrentClient cc, String option, String record) {
        String role = cc.getRole();
        String name = cc.getName();
        String div = cc.getDivision();
        Record rec = recordDB.get(record);
        switch(option) {
            case "1":
                return role.equals("doctor");
            case "2":
                if (rec == null) {
                    return false;
                }
                if (role.equals("doctor")) {
                    return rec.division.equals(div) || rec.doctor.equals(name);
                } else if (role.equals("nurse")) {
                    return rec.division.equals(div) || rec.nurse.equals(name);
                } else if (role.equals("patient")) {
                    return name.equals(record);
                } else if (role.equals("governmentAgency")) {
                    return true;
                } else {
                    return false;
                }

            case "3":
                if (rec == null) {
                    return false;
                }
                if (role.equals("doctor")) {
                    return rec.division.equals(div) || rec.doctor.equals(name);
                } else if (role.equals("nurse")) {
                    return rec.division.equals(div) || rec.nurse.equals(name);
                } else {
                    return false;
                }
            case "4":
                if (rec == null) {
                    return false;
                }
                return role.equals("governmentAgency");
            default:
                return false;
        }
    }

    private void newListener() { (new Thread(this)).start(); } // calls run()

    public static void main(String args[]) {
        System.out.println("\nServer Started\n");
        int port = -1;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        String type = "TLS";
        try {
            ServerSocketFactory ssf = getServerSocketFactory(type);
            ServerSocket ss = ssf.createServerSocket(port);
            ((SSLServerSocket)ss).setNeedClientAuth(true); // enables client authentication
            new server(ss);
        } catch (IOException e) {
            System.out.println("Unable to start Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ServerSocketFactory getServerSocketFactory(String type) {
        if (type.equals("TLS")) {
            SSLServerSocketFactory ssf = null;
            try { // set up key manager to perform server authentication
                SSLContext ctx = SSLContext.getInstance("TLS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
                char[] password = "password".toCharArray();

                ks.load(new FileInputStream("./certificates/server/serverkeystore"), password);  // keystore password (storepass)
                ts.load(new FileInputStream("./certificates/server/servertruststore"), password); // truststore password (storepass)
                kmf.init(ks, password); // certificate password (keypass)
                tmf.init(ts);  // possible to use keystore as truststore here
                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                ssf = ctx.getServerSocketFactory();
                return ssf;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return ServerSocketFactory.getDefault();
        }
        return null;
    }
}
