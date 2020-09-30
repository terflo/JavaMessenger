package com.company.app.network;

import javax.crypto.*;
import java.security.*;
import java.util.Base64;

public class AesCryptoManager {

    private static final String algorithm = "AES";

    private final KeyGenerator keyGen;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private SecretKey key;

    public AesCryptoManager() throws NoSuchAlgorithmException {
        keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(192);
    }

    public String convertToString(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }

    public byte[] convertToByteCode(String str) {
        return Base64.getDecoder().decode(str);
    }

    public SecretKey getKey() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        key = keyGen.generateKey();
        decryptCipher = Cipher.getInstance(algorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        encryptCipher = Cipher.getInstance(algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return this.key;
    }

    public void setKey(SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.key = key;
        decryptCipher = Cipher.getInstance(algorithm);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        encryptCipher = Cipher.getInstance(algorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public byte[] encryptString(String str) throws BadPaddingException, IllegalBlockSizeException {
        return encryptCipher.doFinal(str.getBytes());
    }

    public byte[] decryptString(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        return decryptCipher.doFinal(bytes);
    }
}

