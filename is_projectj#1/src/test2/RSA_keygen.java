package test2;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
//import java.util.Base64;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSA_keygen {
	
	public KeyPair keygenerate () throws GeneralSecurityException{ //keypair »ý¼º
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024); 
		KeyPair keyPair = generator.generateKeyPair();
	
		return keyPair;
	}
	public static String publickey(KeyPair keypair){ //generate publickey
		PublicKey pk =keypair.getPublic();
		byte[] pubk  = pk.getEncoded();
		StringBuffer PublicKeyString = new StringBuffer(); //using StringBuffer, change byte array to String
        for (int i = 0; i < pubk.length; ++i) { 
            PublicKeyString.append(Integer.toHexString(0x0100 + (pubk[i] & 0x00FF)).substring(1));
        }
       String pblkey = PublicKeyString.toString();
		return pblkey;
	}	
	public static String privatekey(KeyPair keypair){//generate privatekey
		PrivateKey sk = keypair.getPrivate();
		byte[] prik = sk.getEncoded();
		StringBuffer PrivateKeyString = new StringBuffer();//using StringBuffer, change byte array to String
        for (int i = 0; i < prik.length; ++i) {
            PrivateKeyString.append(Integer.toHexString(0x0100 + (prik[i] & 0x00FF)).substring(1));
        }
        String prikey=PrivateKeyString.toString();
		return prikey;
	}

}