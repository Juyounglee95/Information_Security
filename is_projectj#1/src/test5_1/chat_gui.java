package test5_1;

import java.awt.EventQueue;

import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

import java.awt.Color;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

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
   private JButton Btn_SendSecK;
   private JButton Btn_SaveOppKey;
   private JButton Btn_LoadOppKey;

   private JRadioButton RBtn_Client;
   private JRadioButton RBtn_Server;
   JLabel Label_Status;
   JLabel Label_KeyPair;
   JLabel Label_KeyInfo;
   JLabel Label_Chat;
   JLabel Label_File;

   String ID, IP, chat, File;
   byte[] ciphertxt;
   int Port;

   Server server;
   Client client;
   chat_Encryption ctxt;
   chat_Decryption dtxt;
   private boolean ServerORClient = true;
   private JScrollPane scrollPane_keypair;
   private JScrollPane scrollPane_another;
   private JScrollPane scrollPane_chat;
   private JScrollPane scrollPane_file;
   private JScrollPane scrollPane_status;
   String publicKey, privKey;
   KeyPair pairkey;
   PublicKey pk;
   PrivateKey sk;
   Key sck, newsck;
   static Connect Cn;
   static chat_gui frame;

   Socket socket;
   ObjectInputStream Oin;
   ObjectOutputStream Oout;
   PublicKey Oppkey;

   String OppID = null;
   String OppkeyStr = null;
   
   byte[] msg;
   boolean seckey = true;
   file_coder fc;
   JTextArea Status_panel;
   String Path;
   
   private class TextFieldListener implements ActionListener {
      public void actionPerformed(ActionEvent evt) {

      }
   }
   /*******************Buttons of START & Send chat & Send File******************/
   private class btActionListener implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
    	/*Execute Start button*/
         if (e.getSource() == Btn_Start) {  

            if (ServerORClient) {

               client = new Client();
               try {/* Type ID */
                  TextFieldListener idListener = new TextFieldListener();
                  TF_ID.addActionListener(idListener);
                  ID = TF_ID.getText();
                  /* Type IP */
                  TextFieldListener ipListener = new TextFieldListener();
                  TF_IP.addActionListener(ipListener);
                  IP = TF_IP.getText();
                  /* Type Port */
                  TextFieldListener portListener = new TextFieldListener();
                  TF_Port.addActionListener(portListener);
                  Port = Integer.parseInt(TF_Port.getText());
               } catch (Exception forme) {
                  System.out.println("Input error");
               }
               client.ClientConn(IP, Port, Cn); // connect to server

               try {
                  Thread.sleep(1000);
               } catch (InterruptedException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               } /*show status*/
               if (Cn.getClientCon()) {
                  Label_Status.setText("Success to connect with server");
               } else {
                  Label_Status.setText("Fail to connect");
               }

            } else {

               TextFieldListener portListener = new TextFieldListener();
               TF_Port.addActionListener(portListener);
               Port = Integer.parseInt(TF_Port.getText());

               server = new Server();

               if (server.ServerConn(Port)) {
                  Label_Status.setText("Success to connect with client");
               } else {
                  Label_Status
                        .setText("Server fail to be executed : PORT ERROR.");
               }
            }

            try {
               if (ServerORClient) {
                  socket = client.Socketreturn();
               } else {
                  socket = server.Socketreturn();
                  ID = "SERVER";//if server, set ID as "SERVER"
               }
            } catch (Exception e4) {

            }

            try {
               Oout = new ObjectOutputStream(socket.getOutputStream());
               Oout.flush();
               Oin = new ObjectInputStream(socket.getInputStream());

            } catch (Exception ex) {
               ex.printStackTrace();
            }
            
            try {
				Oout.writeObject(ID); 
				Oout.flush();
				OppID = (String) Oin.readObject();
			    frame.Status_panel.append(OppID + "is connected! \n"); /*show status*/
                  
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            IO tr = new IO();
            tr.start(); //run IO thread 
            
            
            /*Send Chat*/
         } else if (e.getSource() == Btn_SendChat) {
            /* input plaintext */
            TextFieldListener chatListener = new TextFieldListener();
            TF_Chat.addActionListener(chatListener);
            chat = TF_Chat.getText();
            Panel_Chat.append("SEND: " + chat + "\n"); //show plain txt
            ctxt = new chat_Encryption();
            try {
               ciphertxt = ctxt.encrypt(chat, sck); // encrypt plaintext
               Oout.writeObject(ciphertxt); // send encrypted message
               Oout.flush();
            } catch (NoSuchPaddingException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            } catch (GeneralSecurityException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            frame.TF_Chat.setText("");
            
            /*Send File*/
         } else if (e.getSource() == Btn_SendFile) {
            TextFieldListener fileListener = new TextFieldListener();
            TF_File.addActionListener(fileListener);
   
            File = TF_File.getText(); // type filename
            Panel_File.append("SEND: " + File + "\n");
            File newfile = new File(Path+File); // find the file
            int fos = File.lastIndexOf("."); /*get File Type */
            String ext = File.substring(fos+1);
            String enfilename = Path+"encrypted."+ext; //set Encrypted file's name 

            Panel_File.append("전송 시 사용할 파일이름 " + enfilename + "\n");
         
            File encrypfile = new File(enfilename);

            try {

         
               fc = new file_coder();
               fc.encrypt(sck, newfile, encrypfile); //file encrypt then save it as a encryptfile
               Oout.writeObject(encrypfile);//send encrypted file
               Oout.flush();
            } catch (Exception e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            frame.TF_File.setText("");

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
      setBounds(100, 100, 504, 890);
      contentPane = new JPanel();

      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(null);

      JLabel lblCommunicationMode = new JLabel("Communication Mode"); // choose communication mode between client and server
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
            seckey = true;
            TF_ID.setVisible(true);
            TF_IP.setVisible(true);
            Btn_SendSecK.setVisible(false);
            Label_Status.setText("Haven't connected yet");
            Path = "C:/Client/"; //set client's file path
         }
      });

      RBtn_Client.setAlignmentY(Component.TOP_ALIGNMENT);
      RBtn_Client.setVerticalAlignment(SwingConstants.TOP);
      contentPane.add(RBtn_Client);

      RBtn_Server = new JRadioButton("Server");
      buttonGroup.add(RBtn_Server);
      RBtn_Server.setBounds(76, 31, 78, 34);
      RBtn_Server.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            ServerORClient = false; // available to type Port number only when Server mode
            seckey = false;  //server cannot get secretkey from client
            TF_ID.setVisible(false); 
            TF_IP.setVisible(false);
            Btn_SendSecK.setVisible(true); // available to send secret key only when server mode
            Label_Status.setText("Execute Server if you push Start btn. \n Waiting Client...");
            Path = "C:/Server/"; //set Server's file path
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

      Label_Status = new JLabel("Haven't connected yet "); // Default lebel for connection status
                                                
      Panel_Status.add(Label_Status);

      // Key Gen, Load, Save, Send Panel
      /* -------------------------------------------------------------- */

      JPanel Panel_Key = new JPanel();
      Panel_Key.setBorder(new LineBorder(new Color(0, 0, 0)));
      Panel_Key.setBounds(5, 150, 470, 199);
      contentPane.add(Panel_Key);
      Panel_Key.setLayout(null);

      JLabel lblKeyInformation = new JLabel("KeyPair Information");
      lblKeyInformation.setBounds(5, 126, 129, 18);
      contentPane.add(lblKeyInformation);
      scrollPane_keypair = new JScrollPane();
      scrollPane_keypair.setBounds(7, 5, 447, 41);
      Panel_Key.add(scrollPane_keypair);
      Panel_KeyPair = new JPanel();
      scrollPane_keypair.setViewportView(Panel_KeyPair);
      Panel_KeyPair.setBorder(new LineBorder(new Color(0, 0, 0)));

      Label_KeyPair = new JLabel("KEY PAIR INFORMATION");
      Panel_KeyPair.add(Label_KeyPair);
      
         /*********************Key generation part********************/
      Btn_KeyGen = new JButton("Key Generation");
      Btn_KeyGen.setBounds(7, 58, 135, 27);
      Panel_Key.add(Btn_KeyGen);
      Btn_KeyGen.addActionListener(new ActionListener() {
         /* Generate key */
         @Override
         public void actionPerformed(ActionEvent e) {

            RSA_keygen keypair = new RSA_keygen();
            try {

               pairkey = keypair.keygenerate(); // keypair for generating

               pk = keypair.getpublickey(pairkey); // get publickey 
               publicKey = keypair.publickey(pk); // print publckey
               sk = keypair.getprivatekey(pairkey); // get privatekey

               privKey = keypair.privatekey(sk); // print privatekey

               if (ID == null) {
                  ID = "SERVER";
               }
               Label_KeyPair.setText("<html>ID:" + ID + " public key: "
                     + publicKey + "<br>private key: " + privKey
                     + "</html>");
               // <html>=> for multiLabel

            } catch (GeneralSecurityException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }

         }
      });

      Btn_KeyLoad = new JButton("Load My key"); //Load my key info from saved file
      Btn_KeyLoad.setBounds(156, 58, 144, 27);
      Panel_Key.add(Btn_KeyLoad);
      Btn_KeyLoad.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            SaveNLoad Load = new SaveNLoad();
            try {
            	/*show status*/
  			   	frame.Status_panel.append("Load Key... \n");
                Load.Load(ID,Path); //load my key information
                frame.Label_KeyPair.setText(Load.Load(ID,Path)); //show loaded key information
                pk = Load.getPublicKey(ID,Path); //set publickey 
                sk = Load.getPrivateKey(ID,Path); //set privatekey
             } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
             }
         }

      });
      Btn_KeySave = new JButton("Save my key"); //save my keyinfo 
      Btn_KeySave.setBounds(310, 58, 144, 27);
      Panel_Key.add(Btn_KeySave);
      Btn_KeySave.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            SaveNLoad save = new SaveNLoad();
            try {
            	/*show status*/
  			   frame.Status_panel.append("Save Key... \n");
                save.Save(ID, publicKey, privKey, pk, sk,Path); //save my key information as a file
             } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
             }
         }

      });
      Btn_SendPK = new JButton("Send Publickey"); // send publickey
      Btn_SendPK.setBounds(310, 130, 144, 27);
      Btn_SendPK.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SendPK(); //send publickey
         }
      });
      

      Panel_Key.add(Btn_SendPK);
      

      Btn_SaveOppKey = new JButton("Save Other's"); //Save other's key information
      Btn_SaveOppKey.setBounds(310, 89, 144, 27);
      Panel_Key.add(Btn_SaveOppKey); 
      Btn_SaveOppKey.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SaveNLoad save = new SaveNLoad();
            	try {
            		/*show status*/
     			   frame.Status_panel.append("Save Other's Public Key... \n");
     			   save.Savekeyset(ID, OppID, OppkeyStr, Oppkey,Path); //save other's key information as a file
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	});
      Btn_LoadOppKey = new JButton("Load Other's"); //load Other's key information
      Btn_LoadOppKey.setBounds(156, 89, 144, 27);
      Panel_Key.add(Btn_LoadOppKey);
      Btn_LoadOppKey.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SaveNLoad Load = new SaveNLoad();
			try {

				if(Load.LoadkeyFromset(ID, OppID,Path)!=null){
					/*show status*/
					frame.Status_panel.append("Load Other's Public Key... \n");
					frame.Label_KeyInfo.setText("Other's PublicKey: "+Load.LoadkeyFromset(ID, OppID,Path)); //show loaded other's publickey
					Oppkey = Load.pubfromset(OppID,Path);  //set publickey 
					OppkeyStr = BAtoString(Oppkey.getEncoded()); //show publickey info
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeySpecException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	});

      // Show another Key info
      /* -------------------------------------------------------------- */

      JLabel lblAnotherKeyInfromation = new JLabel("Another Key Infromation");
      lblAnotherKeyInfromation.setBounds(14, 119, 194, 18);
      Panel_Key.add(lblAnotherKeyInfromation);

      Panel_KeyInfo = new JPanel();
      scrollPane_another = new JScrollPane();
      Panel_Key.add(Panel_KeyInfo);
      scrollPane_another.setBounds(7, 146, 293, 41);
      Panel_Key.add(scrollPane_another);

      scrollPane_another.setViewportView(Panel_KeyInfo);
      Panel_KeyInfo.setBorder(new LineBorder(new Color(0, 0, 0)));

      Label_KeyInfo = new JLabel("ANOTHER KEY INFO");
      Panel_KeyInfo.add(Label_KeyInfo);


      Btn_SendSecK = new JButton("Send SecretKey"); //send Secretkey
      Btn_SendSecK.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SendSecretKey(); //Send SecretKey 
            /*show status*/
			frame.Status_panel.append("Send Secret Key... \n");
         }
      });
      Btn_SendSecK.setBounds(310, 161, 144, 27);
      Btn_SendSecK.setVisible(false);
      Panel_Key.add(Btn_SendSecK);

      // chatting part
      /* -------------------------------------------------------------- */

      JLabel lblChatting = new JLabel("Chatting");
      lblChatting.setBounds(209, 464, 129, 18);
      contentPane.add(lblChatting);

      TF_Chat = new JTextField();
      TF_Chat.setBounds(5, 494, 383, 24);
      contentPane.add(TF_Chat);
      TF_Chat.setColumns(10);

      Panel_Chat = new JTextArea();
      scrollPane_chat = new JScrollPane();
      contentPane.add(Panel_Chat);
      contentPane.add(scrollPane_chat);
      scrollPane_chat.setBounds(8, 530, 467, 121);
      scrollPane_chat.setViewportView(Panel_Chat);
      Panel_Chat.setBorder(new LineBorder(new Color(0, 0, 0)));

      Label_Chat = new JLabel("Chatting");
      Panel_Chat.add(Label_Chat);

      Btn_SendChat = new JButton("Send"); // send encrypted txt
      Btn_SendChat.setBounds(394, 491, 78, 27);
      contentPane.add(Btn_SendChat);
      Btn_SendChat.addActionListener(new btActionListener());

      // file transfer part
      /* -------------------------------------------------------------- */

      JLabel lblFileTransfer = new JLabel("File Transfer");
      lblFileTransfer.setBounds(217, 653, 121, 18);
      contentPane.add(lblFileTransfer);

      TF_File = new JTextField();
      TF_File.setColumns(10);
      TF_File.setBounds(8, 683, 383, 24);
      contentPane.add(TF_File);

      Panel_File = new JTextArea(); 
      scrollPane_file = new JScrollPane();
      contentPane.add(Panel_File);
      contentPane.add(scrollPane_file);
      scrollPane_file.setBounds(5, 726, 467, 105);
      scrollPane_file.setViewportView(Panel_File);
      Panel_File.setBorder(new LineBorder(new Color(0, 0, 0)));
      

      Label_File = new JLabel("File Transfer");
      Panel_File.add(Label_File);

      Btn_SendFile = new JButton("Send");
      Btn_SendFile.setBounds(398, 682, 78, 27);
      contentPane.add(Btn_SendFile);
      Btn_SendFile.addActionListener(new btActionListener());

      Status_panel = new JTextArea();
      scrollPane_status = new JScrollPane();
      contentPane.add(Status_panel);
      contentPane.add(scrollPane_status);
      scrollPane_status.setBounds(8, 372, 464, 91);
      scrollPane_status.setViewportView(Status_panel);
      Status_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
      
      
      
      JLabel lblStatus = new JLabel("Status");
      lblStatus.setBounds(209, 351, 62, 18);
      contentPane.add(lblStatus);
      

   }
   
   /*send SecretKey*/
   protected void SendSecretKey() {
      // TODO Auto-generated method stub
      try {
         genSecretkey gs = new genSecretkey();
         sck = gs.genSecretkey(); //generate secretkey

         String sckStr = ""; 
         OppkeyStr = BAtoString(Oppkey.getEncoded());//return public key and secretkey as String form
         sckStr = BAtoString(sck.getEncoded());
         ctxt = new chat_Encryption();
         byte[] secret = ctxt.encrypt_seckey(sck, Oppkey); //encrypt secretkey with publickey
         
         setLabel(Label_KeyInfo, "Other's PublicKey: " + OppkeyStr
               + "SecretKey: " + sckStr); //show Secretkey to be sent
         Oout.writeObject(secret); //send encrypted secretkey
         Oout.flush();

      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public void SendPK() { // send publickey
      try {
         
         Oout.writeObject((PublicKey) pk); 
         Oout.flush();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public String BAtoString(byte[] BA) {
      StringBuffer StrB = new StringBuffer(); // using StringBuffer, change
                                    // byte array to hexstring
      for (int i = 0; i < BA.length; ++i) {
         StrB.append(Integer.toHexString(0x0100 + (BA[i] & 0x00FF))
               .substring(1));
      }
      String EncStr = StrB.toString();
      return EncStr;
   }
/************************SEND & GET DATA ************************************/
   class IO extends Thread { 
      public void run() {
         Object ob = null;
         dtxt = new chat_Decryption();
      
         String plaintxt = null, sckStr = "";
         File file;
      
         while (true) {
            try {
               ob = Oin.readObject(); //get data
            } catch (ClassNotFoundException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            try {
               Oppkey = (PublicKey) ob; // get publickey
               OppkeyStr = BAtoString(Oppkey.getEncoded());
               setLabel(Label_KeyInfo, "Other's PublicKey: " + OppkeyStr); //show publickey's info
            } catch (Exception e) { //if received Object wasn't publckey , then 
               try{
               msg = (byte[]) ob; // get encrypted message  or get secretkey 
               if (seckey) {
                  // get secretkey client only 
                  sck = dtxt.decrypt_seckey(sk, msg); // get secretkey
                  sckStr = BAtoString(sck.getEncoded());

                  setLabel(Label_KeyInfo, "<html> Other's PublicKey: "
                        + OppkeyStr + "<br> SecretKey: " + sckStr+" </html>"); //show publickey's info

                  seckey = false; 

               } 
               else { //if it was not secretkey then
            	   		// get encrypted message
                  frame.Panel_Chat.append("Enc : " + BAtoString(msg) + "\n"); // show the encrypted message
                                
                  try {
                     
                  plaintxt = dtxt.decrypt_txt(sck, msg); // decrypt the message
                     frame.Panel_Chat.append(plaintxt + "\n"); //show the decrypted message
                     
                  }
                  catch (Exception e2) {
                  }
               }
               }
            catch(Exception e3){
            	//if the received object wasn't key and message then 
                  File nfile = (File) ob; // get file 
               
                  frame.Panel_File.append("file is transfered." + "\n");/*show status*/
               
                  int fos = nfile.getName().lastIndexOf("."); /*get file type */
                  String ext =nfile.getName().substring(fos+1);
                  String decyptfilename = "decryptedfile."+ext; //set filename to be decrypted as a "decryptedfile"
                  fc = new file_coder();
                  File decryptfile= new File(Path+decyptfilename);
                  
                     try {
                        fc.decrypt(sck,nfile, decryptfile); //decrypt the file
                     } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                     }
                  
                  
                     frame.Panel_File.append("Transfered file successful");/*show status*/
                    
            
               
                  }
               }
      
   }}}   
   }
