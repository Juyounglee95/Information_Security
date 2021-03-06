package assignment1;

import java.security.*;
import javax.crypto.*;

public class SymEncryption {
	public static void main(String[] args) throws Exception {
		
		String text = "This is a plaintext for symmetric encryption test";
		System.out.println("Plaintext: " + text);

		System.out.println("\n\nAES Key Generation ");
		KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
		keyGen2.init(128);
		Key key2 = keyGen2.generateKey();
		byte[] printKey2 = key2.getEncoded();
		System.out.print("Secret key generation complete: ");
		for (byte b : printKey2)
			System.out.printf("%02X ", b);
		System.out.print("\nLength of secret key: " + printKey2.length + " byte");

		System.out.println("\n\nAES Encryption ");
		Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher2.init(Cipher.ENCRYPT_MODE, key2);

		System.out.print("Plaintext : ");
		byte[] plaintext = text.getBytes();
		/*System.out.print(plaintext);
		*/
		for (byte b : plaintext)
			System.out.printf("%02X ", b);
		System.out.print("\nPlaintext Length: " + plaintext.length + " byte");

		byte[] ciphertext2 = cipher2.doFinal(plaintext);
		System.out.print("\nCiphertext :");
		for (byte b : ciphertext2)
			System.out.printf("%02X ", b);
		System.out.print("\nCiphertext Length: " + ciphertext2.length + " byte");
		cipher2.init(Cipher.DECRYPT_MODE, key2);
		byte[] decrypttext2 = cipher2.doFinal(ciphertext2);
		String output2 = new String(decrypttext2, "UTF8");
		System.out.print("\nDecrypted Text:" + output2);
	}
}
