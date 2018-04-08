package test3;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class chat_Encryption {

	PublicKey pk;
	PrivateKey sk;
	static String plaintxt;
	static byte[] encodedData = null;
	static byte[] b0;
	byte[] b1;
	static Cipher cipher;

	public static byte[] encrypt(String plaintxt, PublicKey pk)
			throws GeneralSecurityException, NoSuchPaddingException {
		plaintxt = plaintxt;
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		byte[] encryptedBytes = cipher.doFinal(plaintxt.getBytes());
		encodedData = Base64.getEncoder().withoutPadding()
				.encode(encryptedBytes);

		return encodedData;

	}

	public static String encrypt_txt(byte[] encrypted) {

		StringBuffer ctxt = new StringBuffer(); // using StringBuffer, change
												// byte array to String
		for (int i = 0; i < encrypted.length; ++i) {
			ctxt.append(Integer.toHexString(0x0100 + (encrypted[i] & 0x00FF))
					.substring(1));
		}
		String ciphertxt = ctxt.toString();
		return ciphertxt;
	}


}
