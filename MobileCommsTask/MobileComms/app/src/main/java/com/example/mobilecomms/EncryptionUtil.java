package com.example.mobilecomms;

import android.util.Log;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EncryptionUtil {

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    public static byte[] encrypt(String message, SecretKey secretKey, IvParameterSpec ivParameterSpec) {
        try {

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            Log.d("MainActivity", "Secret key encoded: " + MainActivity.byteToHexString(secretKey.getEncoded()));
            Log.d("MainActivity", "IV: " + MainActivity.byteToHexString(ivParameterSpec.getIV()));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(byte[] encryptedMessage, SecretKey secretKey, IvParameterSpec ivParameterSpec) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
