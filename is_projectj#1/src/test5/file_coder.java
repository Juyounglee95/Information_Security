package test5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class file_coder {
	
	static String transformation = "AES/ECB/PKCS5Padding";
	static FileInputStream fis; 
    static InputStream  input;
    static FileOutputStream fos;
    static OutputStream output;
    
    public void encrypt(Key key,File source, File dest) throws Exception { //encrypted source file will be stored in dest file
        crypt(key, Cipher.ENCRYPT_MODE, source, dest);
    }
    
    public void decrypt(Key key,File source, File dest) throws Exception { //decrypted source file will be stored in dest file
        crypt(key, Cipher.DECRYPT_MODE, source, dest);
    }
    private static void crypt(Key key, int mode, File source, File dest) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation); 
        cipher.init(mode, key); 
      
        try {
            input = new BufferedInputStream(new FileInputStream(source)); //read data ofsource file
            output = new BufferedOutputStream(new FileOutputStream(dest)); // write data into dest file
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = input.read(buffer)) != -1) {
                output.write(cipher.update(buffer, 0, read));
            }
            output.write(cipher.doFinal());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ie) {
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ie) {
                }
            }
        }
    }
	

}
