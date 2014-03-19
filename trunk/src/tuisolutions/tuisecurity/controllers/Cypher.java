package tuisolutions.tuisecurity.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import android.content.Context;


public class Cypher {
    private int BIT_LENGTH; // key size
    private String PUBLIC_KEY_PATH;
    private String PRIVATE_KEY_PATH;
    private final int BUFFER_SIZE = 128;// Don't change
    private FileInputStream fis;
    private FileOutputStream fos;
    private FileInputStream fis2;
    private FileOutputStream fos2;
    
    public Cypher(String publicKeyPath, String privateKeyPath, int bitLength) {
        this.BIT_LENGTH = bitLength;
        this.PUBLIC_KEY_PATH = publicKeyPath;
        this.PRIVATE_KEY_PATH = privateKeyPath;
        
        // Preferences.setBitLength(bitLength);
        // Preferences.setPublicKeyPath(publicKeyPath);
        // Preferences.setPrivateKeyPath(privateKeyPath);
        
        if (!(new File(this.PUBLIC_KEY_PATH).exists())) {
            // create key pairs
            try {
                generalKeyPairs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public Cypher(Context context) {
        this.BIT_LENGTH = Parameters.BIT_LENGTH;
        this.PUBLIC_KEY_PATH = PreferencesUtils.getPathSaveFile(context);
        this.PRIVATE_KEY_PATH = PreferencesUtils.getPathSaveFile(context);
        
        if (!(new File(this.PUBLIC_KEY_PATH).exists())) {
            // create key pairs
            try {
                generalKeyPairs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void generalKeyPairs() throws Exception {
        
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(BIT_LENGTH);
        KeyPair kp = kpg.generateKeyPair();
        
        KeyFactory kfactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = (RSAPublicKeySpec) kfactory.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = (RSAPrivateKeySpec) kfactory.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
        
        FileManager.saveToFile(this.PUBLIC_KEY_PATH, pub.getModulus(), pub.getPublicExponent());
        FileManager.saveToFile(this.PRIVATE_KEY_PATH, priv.getModulus(), priv.getPrivateExponent());
    }
    
    private PublicKey readPublicKeyFromFile(String keyFileName) throws IOException {
        InputStream in = new FileInputStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }
    
    private PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
        InputStream in = new FileInputStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privKey = fact.generatePrivate(keySpec);
            return privKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }
    
    private void rsaEncrypt(String plainPath, String cypherPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        File plainFile = new File(plainPath);
        File cypherFile;
        if (plainFile.exists() && plainFile.length() > 0) {
            // Config file path if not exist
            if (!new File(cypherPath).exists()) {
                String[] path = cypherPath.split("/");
                String desPath = "";
                for (int i = 0; i < path.length - 1; i++) {
                    desPath += path[i] + "/";
                }
                // Create dir
                File desPathFile = new File(desPath);
                if (!desPathFile.mkdir()) {
                    desPathFile.mkdirs();
                }
                
            }
            // Create File
            cypherFile = new File(cypherPath);
            
            // Get kpublic key
            PublicKey pubKey = readPublicKeyFromFile(this.PUBLIC_KEY_PATH);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherData;
            fis = new FileInputStream(plainFile);
            fos = new FileOutputStream(cypherFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (fis.read(buffer) != -1) {
                cipherData = cipher.doFinal(buffer);
                // Write to file encrypted
                fos.write(cipherData);
            }
        }
    }
    
    private void rsaDecrypt(String cypherPath, String plainPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        File cypherFile = new File(cypherPath);
        File plainFile;
        if (cypherFile.exists() && cypherFile.length() > 0) {
            // Config file path if not exist
            if (!new File(plainPath).exists()) {
                String[] path = plainPath.split("/");
                String desPath = "";
                for (int i = 0; i < path.length - 1; i++) {
                    desPath += path[i] + "/";
                }
                // Create dir
                File desPathFile = new File(desPath);
                if (!desPathFile.mkdir()) {
                    desPathFile.mkdirs();
                }
                
            }
            // Create File
            plainFile = new File(plainPath);
            
            // Get private key
            PrivateKey privKey = readPrivateKeyFromFile(this.PRIVATE_KEY_PATH);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainData;
            
            fis2 = new FileInputStream(cypherFile);
            fos2 = new FileOutputStream(plainFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (fis2.read(buffer) != -1) {
                plainData = cipher.doFinal(buffer);
                // Write to file encrypted
                fos2.write(plainData);
            }
        }
    }
    
    /**
     * Function to encrypt or decrypt file use RSA algorithm. With plain text
     * file at plainFilePath and cypher text file at cypherFilePath
     * 
     * @param plainFilePath
     * @param cypherFilePath
     * @param reserve
     *            If reserve == false: encryption, reserve == true: decryption
     * 
     */
    public boolean crypt(String plainFilePath, String cypherFilePath, boolean reserve) {
        try {
            if (!reserve)
                rsaEncrypt(plainFilePath, cypherFilePath);
            else
                rsaDecrypt(cypherFilePath, plainFilePath);
            return true;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return false;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
