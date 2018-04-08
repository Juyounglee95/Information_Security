package test5_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

public class SaveNLoad {

	BufferedWriter bw;
	BufferedReader br;
	FileWriter writer;
	FileReader reader;
	File file, pubkeyfile, prikeyfile;
	File keyset;
	String Key;
	FileOutputStream fos;
	FileInputStream fis;
	byte[] publicKey;
	/*Save my key information*/
	public void Save(String ID, String PubKey, String PriKey, PublicKey PK, PrivateKey PrK, String Path){
		try {	
			file = new File(Path+ID+"KeyInfo.txt"); //text file to show my key informations
			writer = new FileWriter(file); //create file
			bw = new BufferedWriter(writer); 
			bw.write("ID:"+ID+" public key: "+PubKey); //write my key information
			bw.newLine(); // " \n"
			bw.write("private key: "+PriKey);
			bw.flush();
			
			pubkeyfile = new File(Path+ID+"PublicKeyInfo"); //save my publickey as a file
			fos = new FileOutputStream(pubkeyfile);
			byte[] puK = PK.getEncoded();
			fos.write(puK);
			fos.flush();
			
			prikeyfile = new File(Path+ID+"PrivateKeyInfo"); //save my privatekey as a file
			fos = new FileOutputStream(prikeyfile);
			byte[] prK = PrK.getEncoded();
			fos.write(prK);
			fos.flush();
			
			/*close streams*/
			writer.close();
			bw.close();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public String Load(String ID, String Path){/*to show keyinformation of saved saved key information file*/
		Key = "";
		File lof ;
		try {
			lof=  new File(Path+ID+"KeyInfo.txt");
			reader = new FileReader(lof); //read data of textfile
			char[] in = new char[(int)lof.length()];
			reader.read(in);
			
			for(char c : in){
				Key+=c;
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Loaded: "+ Key;
	}

	public PrivateKey getPrivateKey(String ID, String Path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	      try { //load privatekey by reading privatekey file
	        File privateKeyFile = new File(Path+ID+"PrivateKeyInfo");
	        FileInputStream fis = new FileInputStream(privateKeyFile); //load privatekey file
	        byte[] encodedPrivateKey = new byte[(int)privateKeyFile.length()];
	        fis.read(encodedPrivateKey); //read the file
	        fis.close();
	        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey); //set to PrivateKey form from the read data
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
	        return privateKey; //return privatkey 
	      } catch (FileNotFoundException ex) {
	      } 
	      return null;
	    }
	public PublicKey getPublicKey(String ID, String Path) {
	      try {//load Publickey by reading Publickey file
	        File publicKeyFile = new File(Path+ID+"PublicKeyInfo");
	        FileInputStream fis = new FileInputStream(publicKeyFile);//load publickey file
	        byte[] encodedPublicKey = new byte[(int)publicKeyFile.length()];
	        fis.read(encodedPublicKey);//read the file
	        fis.close();
	        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);//set to publickey form from the read data
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
	        return publicKey;
	      } catch (FileNotFoundException ex) {
	      } catch (IOException|NoSuchAlgorithmException|InvalidKeySpecException ex) {
	      } 
	      return null;
	    }
	
	public void Savekeyset(String OwnerID, String ID, String PubKey, PublicKey OppPk, String Path) throws IOException{
		String line; //save other's key information
		int c = 0, length = 0;
		boolean exist = false;
		String[] IDKey = new String[500];
		pubkeyfile = new File(Path+ID+"PKInfo"); //create other's publickey file
		fos = new FileOutputStream(pubkeyfile);
		byte[] puK = OppPk.getEncoded(); //change form key to byte array to save it as a file
		fos.write(puK); //write data
		fos.flush();
		fos.close();
		keyset = new File(Path+OwnerID+"keyset.txt");
		writer = new FileWriter(keyset, true);
		br = new BufferedReader(new FileReader(keyset));
		do{
			line = br.readLine();
			IDKey[c] = line;
			c++;
		}while(line!=null);
		/* Check the ID exist or not*/
		for(int x = 0; x<IDKey.length; x+=2){
			if(ID.equals(IDKey[x])){
				IDKey[x+1]=PubKey;
				exist = true;
				break;
			}
			else if(IDKey[x]==null){
				IDKey[x] = ID;
				IDKey[x+1] = PubKey;
				length = x+2;
				break;
			}
		}
		
		writer = new FileWriter(keyset);
		bw = new BufferedWriter(writer);
		if(exist){ // if the ID exist in file, just change publickey's information
			for (int x = 0; x < c-1; x++) {
				bw.write(IDKey[x]);
				bw.newLine();
			}			
		}
		else{ // if the ID does not exist in file, save the publickey information 
			for (int x = 0; x < length; x++) {
				bw.write(IDKey[x]);
				bw.newLine();
			}
		}
		bw.flush(); //save
		bw.close(); /*close streams*/
		writer.close();
	}
	public String LoadkeyFromset(String OwnerID, String ID, String Path) throws IOException{
		keyset = new File(Path+OwnerID+"keyset.txt"); /*load other's key information if it is saved */
		br = new BufferedReader(new FileReader(keyset));
		int c = 0;
		String[] IDKey = new String[500];
		String line = null;
		
		do{
			line = br.readLine();
			IDKey[c] = line;
			c++;
		}while(line!=null);
		for(int x = 0; x<IDKey.length; x+=2){
			
			if(ID.equals(IDKey[x])){
				publicKey = IDKey[x].getBytes();
				return IDKey[x+1];
			}
		}
		return null;
	}
	public PublicKey pubfromset(String ID, String Path) throws NoSuchAlgorithmException, InvalidKeySpecException{
		try {	//change form to PublicKey type
	        File publicKeyFile = new File(Path+ID+"PKInfo");
	        FileInputStream fis = new FileInputStream(publicKeyFile);
	        byte[] encodedPublicKey = new byte[(int)publicKeyFile.length()];
	        fis.read(encodedPublicKey); //read file's data
	        fis.close();
	        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
	        return publicKey;
	      } catch (FileNotFoundException ex) {
	      } catch (IOException|NoSuchAlgorithmException|InvalidKeySpecException ex) {
	      } 
	      return null;
	    }
	
}
