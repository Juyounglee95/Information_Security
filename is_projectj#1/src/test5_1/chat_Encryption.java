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
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class chat_Encryption {

	static Cipher cipher;
	public static byte[] encrypt(String plaintxt, Key sec) //encrypt plaintext using Secretkey
			throws GeneralSecurityException, NoSuchPaddingException {
		cipher = Cipher.getInstance("AES"); //using AES algorithm
		cipher.init(Cipher.ENCRYPT_MODE, sec); 
		byte[] encryptedBytes = cipher.doFinal(plaintxt.getBytes()); //return encrypted message in byte array form
	
		return encryptedBytes;

	}
	public byte[] encrypt_seckey(Key sk, PublicKey pk){ //encrypt secretkey with other's publickey
		byte[] wrap=null;
		try {
			cipher = Cipher.getInstance("RSA"); //use RSA algorithm to send secretKey safely 
			cipher.init(Cipher.WRAP_MODE, pk);  //encrypt Secretkey with publickey
			 wrap = cipher.wrap(sk); 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return wrap;
	}
	public static String encrypt_txt(byte[] encrypted) { //print encrypted data

		StringBuffer ctxt = new StringBuffer();     // using StringBuffer, change byte array to String
		for (int i = 0; i < encrypted.length; ++i) {
			ctxt.append(Integer.toHexString(0x0100 + (encrypted[i] & 0x00FF))
					.substring(1));
		}
		String ciphertxt = ctxt.toString();
		return ciphertxt;
	}

}
