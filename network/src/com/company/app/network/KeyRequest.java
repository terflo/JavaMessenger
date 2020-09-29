package com.company.app.network;

import java.security.Key;

public class KeyRequest implements Request {

    private Key publicKey;

    public KeyRequest(Key publicKey) {
        this.publicKey = publicKey;
    }

    public Key getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Key publicKey) {
        this.publicKey = publicKey;
    }
}
