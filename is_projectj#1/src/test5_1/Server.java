package test5_1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


	private ServerSocket serverSocket;
	private Socket socket;

	public boolean ServerConn(int PortNum) {

		serverSocket = null;
		socket = null;

		try {
			while (true) {

				try {
					serverSocket = new ServerSocket(PortNum);
					while (socket == null) {
						socket = serverSocket.accept();
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					
				} 
			}
		} catch (Exception ee) {
			return false;
		}
	}

	public Socket Socketreturn(){
		return socket;
	}
}
