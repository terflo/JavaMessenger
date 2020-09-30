package com.company.app.network;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class CryptoManager {

    private static final String algorithm = "RSA";

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;
    private final KeyPair keyPair;

    public CryptoManager() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        encryptCipher = Cipher.getInstance(algorithm);
        decryptCipher = Cipher.getInstance(algorithm);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(2048);
        keyPair = keyGen.generateKeyPair();
        decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
    }

    public Key getPublicKey() {
        return keyPair.getPublic();
    }

    public String encryptString(String str, Key publicKey) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return new String(encryptCipher.doFinal(str.getBytes()));
    }

    public String decryptString(String str) throws BadPaddingException, IllegalBlockSizeException {
        return new String(decryptCipher.doFinal(str.getBytes()));
        /*StringBuilder decryptStr = new StringBuilder();
        int strIndex = 0;
        while(strIndex < str.length()) {
            if (strIndex + 117 < str.length()) {
                decryptStr.append(new String(decryptCipher.doFinal(str.substring(strIndex, strIndex + 117).getBytes())));
            } else {
                decryptStr.append(new String(decryptCipher.doFinal(str.substring(strIndex, str.length()).getBytes())));
            }
            strIndex += 117;
        }
        return decryptStr.toString();*/
    }



    public byte[] encryptString2(byte[] str, Key publicKey) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encryptCipher.doFinal(str);
    }

    public byte[] decryptString2(byte[] str) throws BadPaddingException, IllegalBlockSizeException {
        return decryptCipher.doFinal(str);
    }
}
