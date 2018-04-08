package test3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.NoSuchPaddingException;
import javax.swing.SwingUtilities;

public class Client {

	public Socket socket;
	private PrintWriter out;
	private static BufferedReader in;

	public String msg, msg_en;
	public static String msg_de;
	public String pubkey;
	public String ctxt;
	public String ec_pubkey = "";
	Connect Cn;
	PublicKey publickey_s,publickey_C;
	PrivateKey privatekey;
	RSA_keygen keygen;
	chat_Encryption ciphertxt = new chat_Encryption();
	static chat_Decryption decryptxt  = new chat_Decryption();
	Server S;
	String serverpk="";
	RSA_keygen rs = new RSA_keygen();
	static byte[] encrypted= null;
	public void ClientConn(String IP, int Port, Connect Cn) {

		socket = null;
		try {
			socket = new Socket(IP, Port);
			setIOStreams();
			Cn.setClientCon();
			// processCall();

		} catch (Exception e) {
		} finally {
		}

	}

	private void setIOStreams() throws IOException {
		out = new PrintWriter(socket.getOutputStream(), true);
		out.flush();
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // return
																					// inputstream
	}
	

	private void processCall() {

		do {
			try {
				msg = (String) in.readLine();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} while (!msg.equals("Server# bye"));

	}

	

	public String getPubKey() {
		try {
			pubkey = (String) in.readLine();
			publickey_s=rs.getPublicKey();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return pubkey;

	}
	public static String getChat() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
		try {
			String enchat = (String)in.readLine();
			chat_gui ui = new chat_gui();
			
			try {
				msg_de = decryptxt.decrypt_txt(ui.returnprivkey(), encrypted);
				return msg_de;
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
		
	}
	/*
	public PrivateKey getPrivateKey(){
		privatekey = rs.getPrivateKey();
		return privatekey;
	}*/
	public void sendchat(String msg) throws GeneralSecurityException{
	//	ciphertxt= new chat_Encryption();
		//byte[] encrypted = ciphertxt.encrypt(msg,publickey_s );
		encrypted= encryptedmsg(msg);
		msg_en = ciphertxt.encrypt_txt(encrypted);
		System.out.println(msg_en);
		
		out.println("CLINET SENT ecrypted: "+ msg_en +"\n");
		out.flush();
	}
	public byte[] encryptedmsg(String msg) throws NoSuchPaddingException, GeneralSecurityException{
	//	ciphertxt = new chat_Encryption();
		 encrypted = ciphertxt.encrypt(msg,publickey_s );
		 
		return encrypted;
	}

	public void sendpk(PublicKey pk) { // send publickey
		try {
			rs.savepubkey(pk); //save publickey as a file to send => 
			msg=keygen.publickey(pk);
			out.println("PUBLICKEY:"+msg); // get publickey through printwriter
			out.flush(); // send publickey to bufferreader
			
			
		} catch (Exception ex) {
		}
	}

	public String Message() {
		return msg;
	}

	private void close() {

		try {
			socket.close();
		} catch (Exception e) {

		}

	}

	public void sendfile(String file) {
		 File myFile = new File (file);
         byte [] mybytearray  = new byte [(int)myFile.length()];
		
		
	}
}
