import java.io.*;
import java.net.*;
import java.security.KeyStore;
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
    private CurrentClient currentClient;

    public server(ServerSocket ss) throws IOException {
        serverSocket = ss;
        newListener();

        Record Alice = new Record("Alice", "doc0", "div0", "nurse0", "Broken foot");
        Record Bob = new Record("Bob", "doc1", "div1", "nurse0", "Broken heart");
        recordDB.put("Alice", Alice);
        recordDB.put("Bob", Bob);
    }

    public void run() {
        try {
            SSLSocket socket=(SSLSocket)serverSocket.accept();
            newListener();
            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
            String subject = cert.getSubjectDN().getName();
            String issuer = cert.getIssuerDN().getName();
            String serialNumber = cert.getSerialNumber().toString();

            currentClient = new CurrentClient(subject);
            currentClient.print();

    	    numConnectedClients++;
            System.out.println("client connected");
            System.out.println("client name (cert subject DN field): " + subject);
            System.out.println("Issuer on certificate received from client:\n" + issuer + "\n");
            System.out.println("Serial number of certificate:\n" + serialNumber + "\n");

            System.out.println(numConnectedClients + " concurrent connection(s)\n");

            PrintWriter out = null;
            BufferedReader in = null;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientMsg = null;
            String record = null;
            out.println("Options:\n 1. Create new record\n 2. Read record\n 3. Write to record\n 4. Delete record");

            while ((clientMsg = in.readLine()) != null) {
                System.out.println(clientMsg);
                out.println("Please enter the name of the record:");
                record = in.readLine();

                switch(clientMsg) {
                    case "1":
                        if (!accessControl("create", record)) {
                            out.println("Denied!");
                        } else {
                            createRecord(record);
                            out.println("TODO");
                        }
                        break;

                    case "2":
                        if (!accessControl("read", record)) {
                            out.println("Denied!");
                        } else {
                            out.println(recordDB.get(record).printable());
                        } 
                        break;

                    case "3":
                        if (!accessControl("write", record)) {
                            out.println("Denied!");
                        } else {
                            writeRecord(record);
                            out.println("TODO");
                        }
                        break;

                    case "4":
                        if (!accessControl("delete", record)) {
                            out.println("Denied!");
                        } else {
                            recordDB.remove(record);
                            out.println("Done");
                        }   
                        break;
                    default:
                        out.println("\nSomething went wrong \n\n" + "Options:\n 1. Create new record\n 2. Read record\n 3. Write to record\n 4. Delete record");
                }
				out.flush();
                System.out.println("done\n");
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

    private void writeRecord(String record) {
        System.out.println("TODO");
    }

    private void createRecord(String record) {
        System.out.println("TODO");
    }

    private Boolean accessControl(String option, String record) {
      //  currentClient.print();
        String st = currentClient.getAttribute("ST");
        String cn = currentClient.getAttribute("CN");
        Record rec = recordDB.get(record);
        switch(option) {
            case "create":
                return st.equals("doctor");
            case "read":
                if (rec == null) {
                    return false;
                }
                if (st.equals("doctor")) {
                    return rec.division.equals(currentClient.getAttribute("OU")) || rec.doctor.equals(cn);
                } else if (st.equals("nurse")) {
                    return rec.division.equals(currentClient.getAttribute("OU")) || rec.nurse.equals(cn);
                } else if (st.equals("patient")) {
                    return cn.equals(record);
                } else if (cn.equals("governmentAgency")) {
                    return true;
                } else {
                    return false;
                }

            case "write":
                if (rec == null) {
                    return false;
                }
                if (st.equals("doctor")) {
                    return rec.division.equals(currentClient.getAttribute("OU")) || rec.doctor.equals(cn);
                } else if (st.equals("nurse")) {
                    return rec.division.equals(currentClient.getAttribute("OU")) || rec.nurse.equals(cn);
                } else if (st.equals("patient")) {
                    return cn.equals(record);
                } else if (st.equals("governmentAgency")) {
                    return false;
                } else {
                    return false;
                }
            case "delete":
                if (rec == null) {
                    return false;
                }
                return cn.equals("governmentAgency");
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
