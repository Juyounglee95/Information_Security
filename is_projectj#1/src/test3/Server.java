package test3;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.NoSuchPaddingException;

public class Server {

	private PrintWriter out;
	private static BufferedReader in;

	private ServerSocket serverSocket;
	private Socket socket;

	public String msg,msg_en;
	public static String msg_de;
	public String pubkey;
	public String ctxt;
	public String ec_pubkey;
	public String clientpk = "";
	PublicKey publickey_s, publickey_C;
	PrivateKey privatekey;
	chat_Encryption ciphertxt;
	Client C;
	 RSA_keygen rs = new RSA_keygen();
	 static chat_Decryption decryptxt  = new chat_Decryption();
	static byte[] encrypted;
	public boolean ServerConn(int PortNum) {

		serverSocket = null;
		socket = null;

		// set the server's port
		try {
			while (true) {

				try {
					serverSocket = new ServerSocket(PortNum);
					while (socket == null) {
						socket = serverSocket.accept();
						setIOStreams();
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					
				} finally {
					//close();
				}
			}
		} catch (Exception ee) {
			return false;
		}
	}

	

	private void setIOStreams() throws IOException {
		out = new PrintWriter(socket.getOutputStream(),true);
		out.flush();
		try {
			in = new BufferedReader(new InputStreamReader(
	                  socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processCall() {
		// send(msg);
		do {
			try {
				msg = (String) in.readLine();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} while (!msg.equals("Client# bye"));
	}

	public void sendpk(PublicKey pk) {
		try {
		publickey_s = pk;
		msg=rs.publickey(publickey_s);
			out.println("PUBLICKEY:"+msg);
			out.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
/*	public PublicKey ec_pubkey(String key){
		publickey_C = 
		return 
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
		/*
	public PrivateKey getPrivateKey(){
		privatekey = rs.getPrivateKey();
		return privatekey;
	}*/
	public String getPubKey(){
		try {
            pubkey =(String) in.readLine();
            
           publickey_C= rs.getPublicKey();
        } 
         catch (IOException ex) {
        	 ex.printStackTrace();
         }
	    return pubkey;
		
	}
	
	
	private void close() {

		try {
			socket.close();
			serverSocket.close();
		} catch (Exception e) {

		}

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



	public void sendfile(String file) {
		// TODO Auto-generated method stub
		
	}



}
