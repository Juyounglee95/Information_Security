package test5_1;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class genSecretkey {
	
	public SecretKey genSecretkey(){ //generate secretkey with AES algorithm
		KeyGenerator keyGen2;
		SecretKey key2=null;
		try {
			keyGen2 = KeyGenerator.getInstance("AES");
			keyGen2.init(128);
			key2 = keyGen2.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return key2;
	}
}
