import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;
import java.security.cert.*;

public class client {
    public static void main(String[] args) throws Exception {
        String host = null;
        int port = -1;
        // for (int i = 0; i < args.length; i++) {
        //     System.out.println("args[" + i + "] = " + args[i]);
        // }
        if (args.length < 2) {
            System.out.println("USAGE: java client host port");
            System.exit(-1);
        }
        try { /* get input parameters */
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("USAGE: java client host port");
            System.exit(-1);
        }
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        try { /* set up a key manager for client authentication */
            SSLSocketFactory factory = null;
            try {
                System.out.println("\n Please enter the path of your certificate:\n");
                String certificatePath = read.readLine();
                System.out.println("\n Please enter your password: \n");
                char[] password = System.console().readPassword();

                KeyStore ks = KeyStore.getInstance("JKS");
                KeyStore ts = KeyStore.getInstance("JKS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                SSLContext ctx = SSLContext.getInstance("TLS");
                ks.load(new FileInputStream("./certificates/clients/" + certificatePath), password);  // keystore password (storepass)
				ts.load(new FileInputStream("./certificates/clients/clienttruststore"), password); // truststore password (storepass);
				kmf.init(ks, password); // user password (keypass)
				tmf.init(ts); // keystore can be used as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                factory = ctx.getSocketFactory();
            } catch (Exception e) {
                System.out.println("Something went wrong!");
                throw new IOException(e.getMessage());
            }
            SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
            System.out.println("\nsocket before handshake:\n" + socket + "\n");

            /*
             * send http request
             *
             * See SSLSocketClient.java for more information about why
             * there is a forced handshake here when using PrintWriters.
             */
            socket.startHandshake();

            SSLSession session = socket.getSession();
            X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
            String subject = cert.getSubjectDN().getName();
            String issuer = cert.getIssuerDN().getName();
            String serialNumber = cert.getSerialNumber().toString();

            System.out.println("certificate name (subject DN field) on certificate received from server:\n" + subject + "\n");
            System.out.println("Issuer on certificate received from server:\n" + issuer + "\n");
            System.out.println("Serial number of certificate:\n" + serialNumber + "\n");
            System.out.println("socket after handshake:\n" + socket + "\n");
            System.out.println("secure connection established\n\n");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            char[] arr = new char[1000];
			for (;;) {
                in.read(arr);
                System.out.print(arr);
                arr = new char[1000];
                System.out.print(">");
                msg = read.readLine();
                if (msg.equalsIgnoreCase("quit")) {
				    break;
				}
                out.println(msg);
                out.flush();
            }
            in.close();
			out.close();
			read.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
