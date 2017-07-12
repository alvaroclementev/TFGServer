/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Alvaro
 */
public class RSAGenerator {
    
    private static final String PUBLIC_KEY_FILENAME = "C:\\Users\\Alvaro\\Desktop\\public.key";
    private static final String PRIVATE_KEY_FILENAME = "C:\\Users\\Alvaro\\Desktop\\private.key";
    
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        
        FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_FILENAME);
        fos.write(publicKeyBytes);
        fos.close();
        
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        
        
    }
}
