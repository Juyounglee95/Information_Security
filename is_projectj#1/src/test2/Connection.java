package test2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class Connection {

	ServerSocket serverSocket = null;
	Socket socket = null;
	boolean connectStatus = false;

	
		public void connect_server(String ip, int portnum) {
			try {
				Socket skt = new Socket(ip, portnum);
				connectStatus = true;
				// BufferedReader in = new BufferedReader(new
				// InputStreamReader(skt.getInputStream()));
				System.out.print("Received string: '");
			}

			/*
			 * while (!in.ready()) {} System.out.println(in.readLine()); // Read
			 * one line and output it
			 * 
			 * System.out.print("'\n"); in.close(); }
			 */
			catch (Exception e) {
				System.out.print("Whoops! It didn't work!\n");
			}

		}
	
	
		   public void setup_server(int portnum) {
		      String data = "Toobie ornaught toobie";
		      System.out.print("******************!\n");
		      connectStatus = false;
		      try { 
		    	  
		    	  ServerSocket srvr = new ServerSocket(portnum);
		    	  System.out.print("Server has connected!\n");
		         Socket skt = srvr.accept();
		         System.out.print("Server has connected!\n");
		       //  PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
		         System.out.print("Sending string: '" + data + "'\n");
		      /*   out.print(data);
		         out.close();
		         skt.close();
		         srvr.close();*/
		      }
		      catch(Exception e) {
		         System.out.print("Whoops! It didn't work!\n");
		      }
		  }
}