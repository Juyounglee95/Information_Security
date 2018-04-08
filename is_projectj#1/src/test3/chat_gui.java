package test3;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Panel;

import javax.swing.JFormattedTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Label;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.JButton;

import java.awt.Color;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;

public class chat_gui extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private JTextField TF_ID;
	private JTextField TF_IP;
	private JTextField TF_Port;
	private JTextField TF_Chat;
	private JTextField TF_File;
	private JTextArea Panel_Chat;
	private JTextArea Panel_File;

	private JPanel Panel_Status;
	private JPanel Panel_KeyPair;
	private JPanel Panel_KeyInfo;

	private JButton Btn_Start;
	private JButton Btn_KeyGen;
	private JButton Btn_KeyLoad;
	private JButton Btn_KeySave;
	private JButton Btn_SendPK;
	private JButton Btn_SendChat;
	private JButton Btn_SendFile;

	private JRadioButton RBtn_Client;
	private JRadioButton RBtn_Server;
	// private JScrollPane scroll;
	JLabel Label_Status;
	JLabel Label_KeyPair;
	JLabel Label_KeyInfo;
	JLabel Label_Chat;
	JLabel Label_File;

	String ID, IP, chat, File, ciphertxt;
	int Port;

	Server server;
	Client client;
	chat_Encryption ctxt;
	private boolean ServerORClient = true;
	private JScrollPane scrollPane_keypair;
	private JScrollPane scrollPane_another;
	private JScrollPane scrollPane_chat;
	private JScrollPane scrollPane_file;

	String publicKey, privKey;

	String Server_PublicKey, Client_PublicKey;
	KeyPair pairkey;
	PublicKey pk;
	PrivateKey sk;
	static Connect Cn;
	static chat_gui frame;

	/**
	 * Launch the application.
	 */
	/*
	public PrivateKey returnprivkey(){
		return sk;
	}*/
	private class TextFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {

		}
	}

	private class btActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == Btn_Start) { // startbutton 실행

				if (ServerORClient) {

					client = new Client();
					try {
						TextFieldListener idListener = new TextFieldListener();
						TF_ID.addActionListener(idListener);
						ID = TF_ID.getText();

						TextFieldListener ipListener = new TextFieldListener();
						TF_IP.addActionListener(ipListener);
						IP = TF_IP.getText();

						TextFieldListener portListener = new TextFieldListener();
						TF_Port.addActionListener(portListener);
						Port = Integer.parseInt(TF_Port.getText());
					} catch (Exception forme) {
						System.out.println("입력값 오류");
					}
					ClientRun trC = new ClientRun(IP, Port, client);
					 ClientGetKeyInfo info = new ClientGetKeyInfo(client,
					 frame);
					trC.start();
					 info.start();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (Cn.getClientCon()) {
						Label_Status.setText("서버에 연결되었습니다.");
					} else {
						Label_Status.setText("서버에 연결이 실패했습니다.");
					}

				} else {

					TextFieldListener portListener = new TextFieldListener();
					TF_Port.addActionListener(portListener);
					Port = Integer.parseInt(TF_Port.getText());

					server = new Server();

					ServerGetKeyInfo sinfo = new ServerGetKeyInfo(server, frame);
					sinfo.start();
					

					if (server.ServerConn(Port)) {
						Label_Status.setText("클라이언트가 연결되었습니다.");
					} else {
						Label_Status.setText("서버가 실행되지 못했습니다 : PORT ERROR.");
					}
				}

			} else if (e.getSource() == Btn_SendChat) {
				TextFieldListener chatListener = new TextFieldListener();
				TF_Chat.addActionListener(chatListener);
				chat = TF_Chat.getText();
		
				if(ServerORClient){
				try {
					client.sendchat(chat);
				} catch (GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Panel_Chat.append("SEND: "+ chat+"\n");
			
				}else{
					try {
						server.sendchat(chat);
					} catch (GeneralSecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Panel_Chat.append("SEND: "+ chat+"\n");
				}
			} else if (e.getSource() == Btn_SendFile) {
				TextFieldListener fileListener = new TextFieldListener();
				TF_File.addActionListener(fileListener);
				File = TF_File.getText();
				if(ServerORClient){
					client.sendfile(File);
					Panel_File.append("SEND: "+ File+"\n");
				
					}else{
						server.sendfile(File);
						Panel_File.append("SEND: "+ File+"\n");
					}
			}

		}
	}

	public static void main(String[] args) {
		Cn = new Connect();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new chat_gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Create the frame.
	 */

	public void setLabel(JLabel label, String msg) {
		label.setText(msg);
	}

	public chat_gui() throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 504, 794);
		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblCommunicationMode = new JLabel("Communication Mode");
		lblCommunicationMode.setBounds(8, 5, 146, 18);
		contentPane.add(lblCommunicationMode);

		// client and server radio button
		/* -------------------------------------------------------------- */
		RBtn_Client = new JRadioButton("Client");
		buttonGroup.add(RBtn_Client);
		RBtn_Client.setBounds(5, 31, 65, 34);
		RBtn_Client.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ServerORClient = true;

				TF_ID.setVisible(true);
				TF_IP.setVisible(true);

				Label_Status.setText("서버에 연결되지 않았습니다.");

			}
		});

		RBtn_Client.setAlignmentY(Component.TOP_ALIGNMENT);
		RBtn_Client.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(RBtn_Client);

		RBtn_Server = new JRadioButton("Server"); // 라디오버튼을 제대로 눌러야함
		buttonGroup.add(RBtn_Server);
		RBtn_Server.setBounds(76, 31, 78, 34);
		RBtn_Server.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ServerORClient = false;

				TF_ID.setVisible(false);
				TF_IP.setVisible(false);

				Label_Status
						.setText("Start를 누르면 서버를 실행합니다. \n 클라이언트의 접속을 기다립니다.");

			}
		});

		RBtn_Server.setPreferredSize(new Dimension(10, 10));
		RBtn_Server.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(RBtn_Server);

		// ID, IP, PORT setting and make connection
		/* -------------------------------------------------------------- */

		JLabel lblInformation = new JLabel("ID");
		lblInformation.setBounds(164, 5, 23, 18);
		contentPane.add(lblInformation);

		TF_ID = new JTextField();
		TF_ID.setBounds(164, 24, 65, 23);
		contentPane.add(TF_ID);
		TF_ID.setColumns(10);

		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(243, 5, 62, 18);
		contentPane.add(lblIp);

		TF_IP = new JTextField();
		TF_IP.setColumns(10);
		TF_IP.setBounds(243, 24, 145, 23);
		contentPane.add(TF_IP);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(405, 5, 62, 18);
		contentPane.add(lblPort);

		TF_Port = new JTextField();
		TF_Port.setColumns(10);
		TF_Port.setBounds(402, 24, 65, 23);
		contentPane.add(TF_Port);

		Btn_Start = new JButton("START");
		Btn_Start.setBackground(Color.RED);
		Btn_Start.setBounds(367, 51, 105, 27);
		contentPane.add(Btn_Start);
		Btn_Start.addActionListener(new btActionListener());

		// Show connection
		/* -------------------------------------------------------------- */

		JLabel lblShowConnectionStatus = new JLabel("Show Connection status");
		lblShowConnectionStatus.setBounds(5, 65, 189, 23);
		contentPane.add(lblShowConnectionStatus);

		Panel_Status = new JPanel();
		Panel_Status.setBorder(new LineBorder(new Color(0, 0, 0)));
		Panel_Status.setBounds(5, 90, 471, 34);
		contentPane.add(Panel_Status);

		Label_Status = new JLabel("서버에 연결되지 않았습니다.");
		Panel_Status.add(Label_Status);

		// Key Gen, Load, Save, Send Panel
		/* -------------------------------------------------------------- */

		JPanel Panel_Key = new JPanel();
		Panel_Key.setBorder(new LineBorder(new Color(0, 0, 0)));
		Panel_Key.setBounds(5, 150, 470, 189);
		contentPane.add(Panel_Key);
		Panel_Key.setLayout(null);

		JLabel lblKeyInformation = new JLabel("KeyPair Information");
		lblKeyInformation.setBounds(5, 126, 129, 18);
		contentPane.add(lblKeyInformation);
		/* 추가 ****************************** */
		scrollPane_keypair = new JScrollPane();
		scrollPane_keypair.setBounds(10, 10, 447, 41);
		Panel_Key.add(scrollPane_keypair);
		/***************************************************/
		Panel_KeyPair = new JPanel();
		scrollPane_keypair.setViewportView(Panel_KeyPair);
		Panel_KeyPair.setBorder(new LineBorder(new Color(0, 0, 0)));

		Label_KeyPair = new JLabel("KEY PAIR INFORMATION");
		Panel_KeyPair.add(Label_KeyPair);

		Btn_KeyGen = new JButton("Key Generation");
		Btn_KeyGen.setBounds(10, 58, 144, 27);
		Panel_Key.add(Btn_KeyGen);
		Btn_KeyGen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				RSA_keygen keypair = new RSA_keygen();
				try {
					/* 수정 ****************************** */
					pairkey = keypair.keygenerate(); // keypair for generating
														// publickey to encrypt
														// plaintext
					pk = pairkey.getPublic(); //create publickey
					publicKey = keypair.publickey(pk); // print publickey to string
					sk = pairkey.getPrivate(); //create privkey
					privKey = keypair.privatekey(sk); // print privatekey to string
					Label_KeyPair.setText("<html> Publickey: " + publicKey
							+ "<br> PrivKey: " + privKey + "</html>");
					// <html>=> for multiLabel
				//	keypair.savepubkey(pk); //save publickey as a file
					//keypair.saveprivkey(sk); //save privatekey as a file

				} catch (GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		Btn_KeyLoad = new JButton("Load File");
		Btn_KeyLoad.setBounds(159, 58, 144, 27);
		Panel_Key.add(Btn_KeyLoad);

		Btn_KeySave = new JButton("Save File");
		Btn_KeySave.setBounds(310, 58, 144, 27);
		Panel_Key.add(Btn_KeySave);

		Btn_SendPK = new JButton("Send Publickey"); // send publickey
		Btn_SendPK.setBounds(313, 136, 144, 27);
		Btn_SendPK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				if (ServerORClient) {
					client.sendpk(pk); // send publickey to server
				} else {
					server.sendpk(pk); // send publickey to client
				}
			}
		});

		Panel_Key.add(Btn_SendPK);

		// Show another Key info
		/* -------------------------------------------------------------- */

		JLabel lblAnotherKeyInfromation = new JLabel("Another Key Infromation");
		lblAnotherKeyInfromation.setBounds(10, 93, 194, 18);
		Panel_Key.add(lblAnotherKeyInfromation);

		Panel_KeyInfo = new JPanel();
		scrollPane_another = new JScrollPane();
		Panel_Key.add(Panel_KeyInfo);
		scrollPane_another.setBounds(10, 120, 280, 57);
	//	scrollPane_another.setBounds(10, 120, 280, 29);
		Panel_Key.add(scrollPane_another);

		scrollPane_another.setViewportView(Panel_KeyInfo);
		Panel_KeyInfo.setBorder(new LineBorder(new Color(0, 0, 0)));

		Label_KeyInfo = new JLabel("ANOTHER KEY INFO");
		Panel_KeyInfo.add(Label_KeyInfo);

		// chatting part
		/* -------------------------------------------------------------- */

		JLabel lblChatting = new JLabel("Chatting");
		lblChatting.setBounds(215, 340, 129, 18);
		contentPane.add(lblChatting);

		TF_Chat = new JTextField();
		TF_Chat.setBounds(5, 370, 383, 24);
		contentPane.add(TF_Chat);
		TF_Chat.setColumns(10);

		Panel_Chat = new JTextArea(); // change panel to textarea
		scrollPane_chat= new JScrollPane();
		contentPane.add(Panel_Chat);
		contentPane.add(scrollPane_chat);
		scrollPane_chat.setBounds(5, 406, 467, 160);
		scrollPane_chat.setViewportView(Panel_Chat);
		Panel_Chat.setBorder(new LineBorder(new Color(0, 0, 0)));
		

		Label_Chat = new JLabel("Chatting");
		Panel_Chat.add(Label_Chat);

		Btn_SendChat = new JButton("Send"); // send encrypted txt
		Btn_SendChat.setBounds(389, 369, 78, 27);
		contentPane.add(Btn_SendChat);
		Btn_SendChat.addActionListener(new btActionListener() );

		// file transfer part
		/* -------------------------------------------------------------- */

		JLabel lblFileTransfer = new JLabel("File Transfer");
		lblFileTransfer.setBounds(215, 568, 121, 18);
		contentPane.add(lblFileTransfer);

		TF_File = new JTextField();
		TF_File.setColumns(10);
		TF_File.setBounds(5, 598, 383, 24);
		contentPane.add(TF_File);

		Panel_File = new JTextArea(); // change panel to textarea
		scrollPane_file= new JScrollPane();
		contentPane.add(Panel_File);
		contentPane.add(scrollPane_file);
		Panel_File.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_file.setBounds(5, 630, 467, 105);
		scrollPane_file.setViewportView(Panel_File);
		

		Label_File = new JLabel("File Transfer");
		Panel_File.add(Label_File);

		Btn_SendFile = new JButton("Send");
		Btn_SendFile.setBounds(394, 598, 78, 27);
		contentPane.add(Btn_SendFile);

	}

	class ClientRun extends Thread {
		Client client;
		String IP;
		int Port;

		ClientRun(String IP, int Port, Client client) {
			this.IP = IP;
			this.Port = Port;
			this.client = client;
		}

		public void run() {
			client.ClientConn(IP, Port, Cn);

		}
	}

	class ClientGetKeyInfo extends Thread {
		Client client;
		chat_gui UI;

		ClientGetKeyInfo(Client client, chat_gui UI) {
			this.client = client;
			this.UI = UI;
		}

		public void run() {
			while (true) {

				try {
					if (client.getPubKey().startsWith("PUBLICKEY")) {
						Server_PublicKey = client.getPubKey().substring(10,
								client.getPubKey().length());

						UI.Label_KeyInfo.setText(client.getPubKey());

						System.out.println(Server_PublicKey);
					}
					else{
						
						UI.Panel_Chat.append(client.getChat()+"\n");
						
					}

				} catch (Exception e) {

				}
			}
		}
	}

	class ServerGetKeyInfo extends Thread {
		Server server;
		chat_gui UI;

		ServerGetKeyInfo(Server server, chat_gui UI) {
			this.server = server;
			this.UI = UI;
		}

		public void run() {
			while (true) {

				try {

					if (server.getPubKey().startsWith("PUBLICKEY")) {
						Client_PublicKey = server.getPubKey().substring(10,
								server.getPubKey().length());
						UI.Label_KeyInfo.setText(server.getPubKey());
						System.out.println(Client_PublicKey);
					}else{
						
						UI.Panel_Chat.append(server.getChat()+"\n");
					}

				} catch (Exception e) {

				}
			}
		}
	}

	public PrivateKey returnprivkey() {
		// TODO Auto-generated method stub
		return sk;
	}
}