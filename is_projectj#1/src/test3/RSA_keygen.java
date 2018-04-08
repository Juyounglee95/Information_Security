package test3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;



//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

public class RSA_keygen {
   KeyPair keypair;
   PublicKey pk;
   PrivateKey sk;
   static byte[] pubk;
   static byte[] prik;
   static KeyPairGenerator generator;
   static KeyFactory kf ;
  
   public KeyPair keygenerate () throws GeneralSecurityException{ //keypair »ý¼º
      generator= KeyPairGenerator.getInstance("RSA");
      generator.initialize(1024); 
      KeyPair keyPair = generator.generateKeyPair();
     
    
      return keyPair;
   }/*
   public static  PublicKey getpublickey(KeyPair keypair) throws InvalidKeySpecException{
	   PublicKey pk = keypair.getPublic();
	 
	  
	return pk;
	   
   }
   public static PrivateKey getprivatekey(KeyPair keypair) throws InvalidKeySpecException{
	   PrivateKey sk = keypair.getPrivate();

	  
	   return sk;
   }
   public static PublicKey back_pubk(byte[]pk) throws InvalidKeySpecException{
	   try {
		kf = kf.getInstance("RSA");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  // byte[]pk = Base64.Encoder(publickey.getBytes(), Base64.DEFAULT);
	   pubk=pk;
	   X509EncodedKeySpec pubspace = new X509EncodedKeySpec(pk);
	   PublicKey pubKey = kf.generatePublic(pubspace);
	   return pubKey;
   }
   public static PrivateKey back_privk(byte[] pk) throws InvalidKeySpecException{
	   try {
		kf = kf.getInstance("RSA");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   X509EncodedKeySpec privspace = new X509EncodedKeySpec(pk);
	   PrivateKey privKey =  kf.generatePrivate(privspace);
	   return privKey;
   }
   */
   
   public static String publickey(PublicKey pk){ //generate publickey
      StringBuffer PublicKeyString = new StringBuffer(); //using StringBuffer, change byte array to String
      pubk  = pk.getEncoded();
	   
        for (int i = 0; i < pubk.length; ++i) { 
            PublicKeyString.append(Integer.toHexString(0x0100 + (pubk[i] & 0x00FF)).substring(1));
        }
       String pblkey = PublicKeyString.toString();
      return pblkey;
   }   
   public static String privatekey(PrivateKey sk){//generate privatekey
		  prik = sk.getEncoded();
      StringBuffer PrivateKeyString = new StringBuffer();//using StringBuffer, change byte array to String
        for (int i = 0; i < prik.length; ++i) {
            PrivateKeyString.append(Integer.toHexString(0x0100 + (prik[i] & 0x00FF)).substring(1));
        }
        String prikey=PrivateKeyString.toString();
      return prikey;
   }
   
   
   public static void savepubkey(PublicKey publicKey) {
	   try {
	     X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
	     FileOutputStream fos = new FileOutputStream("public.key");
	     fos.write(x509EncodedKeySpec.getEncoded());
	     fos.flush();
	     fos.close();
	   } catch (FileNotFoundException ex) {
	   } catch (IOException ex) {
	   }
	 }
	
   public static void saveprivkey(PrivateKey privateKey) {
	   try {
	     PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
	     FileOutputStream fos = new FileOutputStream("private.key");
	     fos.write(pkcs8EncodedKeySpec.getEncoded());
	     fos.flush();
	     fos.close();
	   } catch (FileNotFoundException ex) {
	   } catch (IOException ex) {
	   }
	 }
	
   public static PrivateKey getPrivateKey() {
	   try {
	     File privateKeyFile = new File("private.key");
	     FileInputStream fis = new FileInputStream("private.key");
	     byte[] encodedPrivateKey = new byte[(int)privateKeyFile.length()];
	     fis.read(encodedPrivateKey);
	     fis.close();
	     PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
	     KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	     PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
	     return privateKey;
	   } catch (FileNotFoundException ex) {
	   } catch (IOException|NoSuchAlgorithmException|InvalidKeySpecException ex) {
	   } 
	   return null;
	 }
	
public static PublicKey getPublicKey() {
	   try {
	     File publicKeyFile = new File("public.key");
	     FileInputStream fis = new FileInputStream("public.key");
	     byte[] encodedPublicKey = new byte[(int)publicKeyFile.length()];
	     fis.read(encodedPublicKey);
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