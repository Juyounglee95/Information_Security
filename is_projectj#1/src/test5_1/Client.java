package test5_1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class Client {

	public Socket socket;

	Connect Cn;

	public void ClientConn(String IP, int Port, Connect Cn) {

		socket = null;
		try {
			socket = new Socket(IP, Port);
			Cn.setClientCon();

		} catch (Exception e) {
		} finally {
		}

	}
	public Socket Socketreturn(){
		return socket;
	}
}
