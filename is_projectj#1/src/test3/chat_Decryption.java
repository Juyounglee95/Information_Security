package test3;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class chat_Decryption {

	PublicKey pk;
	PrivateKey sk;
	String plaintxt;
	static Cipher cipher;
	public static String decrypt_txt(PrivateKey sk, byte[] encrypted)
			throws GeneralSecurityException, NoSuchPaddingException {

		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, sk);
			byte[] decodedData = Base64.getDecoder().decode(encrypted);
			byte[] decryptedBytes = cipher.doFinal(decodedData);
		//	plaintxt = new String(decryptedBytes);
			StringBuffer ctxt = new StringBuffer(); 
			for (int i = 0; i < decryptedBytes.length; ++i) {
				ctxt.append(Integer.toHexString(0x0100 + (decryptedBytes[i] & 0x00FF))
						.substring(1));
			}
			String plaintxt = ctxt.toString();
			return plaintxt;
		} catch (NoSuchAlgorithmException ex) {
			// 예외 처리
		}
		return null;
	}
}
