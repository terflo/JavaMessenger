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
    }
}
