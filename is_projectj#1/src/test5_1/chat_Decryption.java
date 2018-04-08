package test5_1;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class chat_Decryption {

	static Cipher cipher;
	public static String decrypt_txt(Key sec, byte[] encrypted) //decrypt the encrypted message to plaintext  
			throws GeneralSecurityException, NoSuchPaddingException {

		try {
			cipher = Cipher.getInstance("AES"); //using AES algorithm 
			cipher.init(Cipher.DECRYPT_MODE,sec ); // decrypt mode using Secretkey
			byte[] decryptedBytes = cipher.doFinal(encrypted);
			
			String plaintxt = new String(decryptedBytes); //transform byte array to text
			return "Dec : "+plaintxt;
		} catch (NoSuchAlgorithmException ex) {
			// exception
		}
		return null;
	}
	public Key decrypt_seckey(PrivateKey sk, byte[] ensec){ //decrypt encrypted secretkey using privatekey
		Key sec=null;
		try {
			cipher = Cipher.getInstance("RSA"); //use RSA algorithm because it is encrypted with RSA algorithm
			cipher.init(Cipher.UNWRAP_MODE,sk);  // unwrap encrypted key as a secretkey 
			sec = cipher.unwrap(ensec, "AES", Cipher.SECRET_KEY); 
			return sec;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
}
