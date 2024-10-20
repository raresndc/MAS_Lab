package com.example.mobilecomms;

public interface AsyncTaskCallback {
    void receivePublicKey(String encodedKey);
    void decryptMessage(String toString);
}
