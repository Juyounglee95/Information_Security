package test2;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
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
import java.security.PublicKey;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;

public class chat_gui extends JFrame implements Runnable {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private JTextField TF_ID;
	private JTextField TF_IP;
	private JTextField TF_Port;
	private JTextField TF_Chat;
	private JTextField TF_File;

	private JPanel Panel_Status;
	private JPanel Panel_KeyPair;
	private JPanel Panel_KeyInfo;
	private JPanel Panel_Chat;
	private JPanel Panel_File;

	private JButton Btn_Start;
	private JButton Btn_KeyGen;
	private JButton Btn_KeyLoad;
	private JButton Btn_KeySave;
	private JButton Btn_SendPK;
	private JButton Btn_SendChat;
	private JButton Btn_SendFile;

	private JRadioButton RBtn_Client;
	private JRadioButton RBtn_Server;
	private JScrollPane scroll;
	JLabel Label_Status;
	JLabel Label_KeyPair;
	JLabel Label_KeyInfo;
	JLabel Label_Chat;
	JLabel Label_File;

	String ID, IP, chat, File;
	int Port;
	Connection c;
	private boolean ServerORClient = true;
	
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */

	private class TextFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {

		}
	}

	private class btActionListener implements ActionListener {

		@Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
         if (e.getSource() == Btn_Start) { // startbutton 실행
        	 c = new Connection();
            if (ServerORClient) {
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
                  System.out.println("input error");
               }
           // c = new Connection();
           
            c.connect_server(IP, Port);
               if (c.connectStatus) { //
                  Label_Status.setText("서버에 연결되었습니다.");//
               } else {
                  Label_Status.setText("서버에 연결이 실패했습니다.");//
               }
               
            } else {
            	
            	 c.setup_server(Port);
               if (c.connectStatus) {//
                  Label_Status.setText("클라이언트가 연결되었습니다.");
               } else {
                  Label_Status.setText("서버가 실행되지 못했습니다 : PORT ERROR.");
               }

            }

         } else if (e.getSource() == Btn_SendChat) {
            TextFieldListener chatListener = new TextFieldListener();
            TF_Chat.addActionListener(chatListener);
            chat = TF_Chat.getText();

         } else if (e.getSource() == Btn_SendFile) {
            TextFieldListener fileListener = new TextFieldListener();
            TF_File.addActionListener(fileListener);
            File = TF_File.getText();
         }

      }
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					chat_gui frame = new chat_gui();
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
	public chat_gui() {

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
		RBtn_Client.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ServerORClient = true;

				TF_ID.setVisible(true);
				TF_IP.setVisible(true);
				TF_Port.setVisible(true);

				Label_Status.setText("서버에 연결되지 않았습니다.");

			}
		});

		RBtn_Client.setAlignmentY(Component.TOP_ALIGNMENT);
		RBtn_Client.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(RBtn_Client);

		RBtn_Server = new JRadioButton("Server"); // 라디오버튼을 제대로 눌러야함
		buttonGroup.add(RBtn_Server);
		RBtn_Server.setBounds(76, 31, 78, 34);
		RBtn_Server.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ServerORClient = false;

				TF_ID.setVisible(false);
				TF_IP.setVisible(false);
				TF_Port.setVisible(true);

				Label_Status.setText("Start를 누르면 서버를 실행합니다. \n 클라이언트의 접속을 기다립니다.");
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
		Btn_Start.setBounds(253, 51, 105, 27);
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
		Panel_Key.setBounds(5, 150, 470, 160);
		contentPane.add(Panel_Key);
		Panel_Key.setLayout(null);

		JLabel lblKeyInformation = new JLabel("KeyPair Information");
		lblKeyInformation.setBounds(5, 126, 129, 18);
		contentPane.add(lblKeyInformation);
		/* 추가****************************** */
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 447, 41);
		Panel_Key.add(scrollPane);
		/***************************************************/
		Panel_KeyPair = new JPanel();
		scrollPane.setViewportView(Panel_KeyPair);
		Panel_KeyPair.setBorder(new LineBorder(new Color(0, 0, 0)));

		Label_KeyPair = new JLabel("KEY PAIR INFORMATION"); // 키 페어 라벨 재설정 필요
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
					/* 수정****************************** */
					String publicKey = RSA_keygen.publickey(keypair
							.keygenerate()); // print public key to String
					String privKey = RSA_keygen.privatekey(keypair
							.keygenerate()); // print public key to String
					Label_KeyPair.setText("<html> public key: " + publicKey
							+ "<br>private key: " + privKey + "</html>");
					// <html>=> for multiLabel

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

		Btn_SendPK = new JButton("Send Publickey");
		Btn_SendPK.setBounds(310, 120, 144, 27);
		Btn_SendPK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		Panel_Key.add(Btn_SendPK);

		// Show another Key info
		/* -------------------------------------------------------------- */

		JLabel lblAnotherKeyInfromation = new JLabel("Another Key Infromation");
		lblAnotherKeyInfromation.setBounds(10, 93, 194, 18);
		Panel_Key.add(lblAnotherKeyInfromation);

		Panel_KeyInfo = new JPanel();
		Panel_KeyInfo.setBounds(10, 120, 280, 29);
		Panel_KeyInfo.setBorder(new LineBorder(new Color(0, 0, 0)));
		Panel_Key.add(Panel_KeyInfo);

		Label_KeyInfo = new JLabel("ANOTHER KEY INFO");
		Panel_KeyInfo.add(Label_KeyInfo);

		// chatting part
		/* -------------------------------------------------------------- */

		JLabel lblChatting = new JLabel("Chatting");
		lblChatting.setBounds(215, 314, 129, 18);
		contentPane.add(lblChatting);

		TF_Chat = new JTextField();
		TF_Chat.setBounds(5, 344, 383, 24);
		contentPane.add(TF_Chat);
		TF_Chat.setColumns(10);

		Panel_Chat = new JPanel();
		Panel_Chat.setBorder(new LineBorder(new Color(0, 0, 0)));
		Panel_Chat.setBounds(5, 375, 467, 191);
		contentPane.add(Panel_Chat);

		Label_Chat = new JLabel("Chatting");
		Panel_Chat.add(Label_Chat);

		Btn_SendChat = new JButton("Send");
		Btn_SendChat.setBounds(394, 344, 78, 27);
		contentPane.add(Btn_SendChat);

		// file transfer part
		/* -------------------------------------------------------------- */

		JLabel lblFileTransfer = new JLabel("File Transfer");
		lblFileTransfer.setBounds(215, 568, 121, 18);
		contentPane.add(lblFileTransfer);

		TF_File = new JTextField();
		TF_File.setColumns(10);
		TF_File.setBounds(5, 598, 383, 24);
		contentPane.add(TF_File);

		Panel_File = new JPanel();
		Panel_File.setBorder(new LineBorder(new Color(0, 0, 0)));
		Panel_File.setBounds(5, 630, 467, 105);
		contentPane.add(Panel_File);

		Label_File = new JLabel("File Transfer");
		Panel_File.add(Label_File);

		Btn_SendFile = new JButton("Send");
		Btn_SendFile.setBounds(394, 598, 78, 27);
		contentPane.add(Btn_SendFile);
		
		JButton Btn_Stop = new JButton("STOP");
		Btn_Stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//server close 
				
			}
		});
		Btn_Stop.setBackground(Color.RED);
		Btn_Stop.setBounds(362, 51, 105, 27);
		contentPane.add(Btn_Stop);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}