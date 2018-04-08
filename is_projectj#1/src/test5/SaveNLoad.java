package test5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class SaveNLoad {

	FileWriter writer;
	FileReader reader;
	File file;

/*	HashMap<String, Key> KeySave = new HashMap<String, Key>();

	StringBuffer str = new StringBuffer();
	String keyset;
	String set;
*//*	public void Save(String ID, String PublicKey, String PrivateKey) throws IOException{
		if(ID == null){
			ID = "SERVER";
		}
		boolean contain = false;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str.toString());
		while(true){
			String in = reader.readLine();
			if(in==null){
				break;
			}
			str.append(in);
			System.out.println(str.toString());
		}
		
		String[] keysplit = new String[10];
		keysplit = str.toString().split("\n");
		
		for(int a = 0 ; a< keysplit.length; a++){
			System.out.println(keysplit[a]);
			if(keysplit[a].equals(ID)){
				keysplit[a+1]=PublicKey+"\t"+PrivateKey;
				contain = true;
				break;
			}
		}
		
		if(!contain){
			
			set = ID+"\r"+PublicKey+"\t"+PrivateKey+"\r";
			System.out.print(ID);
			System.out.print(PublicKey);
			System.out.print(PrivateKey);
			
			str.append(set);	
		}
		
		try{
		writer = new FileWriter(file);
		writer.write(str.toString());
		writer.flush();
		writer.close();
		
		}catch(Exception e2){
			
		}
	}
	*/
	public void Save(String ID, String PubKey, String PriKey){
		try {
			if(ID=="SERVER"){
				file = new File("ServerKey.txt");
			System.out.println("********");
			FileWriter fw = new FileWriter(file);
			fw.write("ID:"+ID+"\n public key: "+PubKey+"\n private key: "+PriKey+"\n");
			fw.flush();
			fw.close();
			}else{
			file= new File("ClientKey.txt");
			System.out.println("%%%%%%%%%");
			writer = new FileWriter(file);
			writer.write("ID:"+ID+"\n public key: "+PubKey+"\n private key: "+PriKey+"\n");
			writer.flush();
			writer.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public String Load(String ID){
		String Key = "";
		File lof ;
		try {
			if(ID=="SERVER"){
			lof=  new File("ServerKey.txt");
			}else{
			lof = new File("ClientKey.txt");
			}
			reader = new FileReader(lof);
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
	class Key{
		String PuK;
		String PriK;
		Key(String PuK, String PriK){
			this.PuK = PuK;
			this.PriK = PriK;
		}
	}
}
