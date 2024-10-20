package com.example.mobilecomms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallback {

    private String localHost = "https://10.0.2.2";
    byte[] sharedSecret;
    ECPublicKey publicKey;
    private SecretKey secretKey;
    private IvParameterSpec ivParameterSpec;
    private EditText etMsg;
    private TextView resultTextView;
    PrivateKey privateKey;
    private PublicKey serverPublicKey; // To hold the server's public key

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        etMsg = findViewById(R.id.etMsg);

        // Key pair generation
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(new ECGenParameterSpec("secp384r1"));
            KeyPair kp = kpg.generateKeyPair();
            publicKey = (ECPublicKey) kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPublicKey(View view) {
        try {
            byte[] encoded = Base64.encode(publicKey.getEncoded(), Base64.DEFAULT);
            String strEncoded = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
            Log.d("MainActivity", "Client public key: " + strEncoded);
            HttpRequestTask httpRequestTask = new HttpRequestTask(this, encoded);
            httpRequestTask.execute(localHost, ApiEndPoints.POST_PUBLIC_KEY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Java method
    public static String byteToHexString(byte[] payload) {
        if (payload == null) return "<empty>";
        StringBuilder stringBuilder = new StringBuilder(payload.length);
        for (byte byteChar : payload)
            stringBuilder.append(String.format("%02X", byteChar));
        return stringBuilder.toString();
    }

    public void getPublicKey(View view) {
        Button button = findViewById(R.id.btnGetKey);
        button.setEnabled(false);

        String url = localHost+ "/";
//        + ApiEndPoints.GET_PUBLIC_KEY;
        new HttpRequestTask(this).execute(url, ApiEndPoints.GET_PUBLIC_KEY);

        new Handler().postDelayed(() -> button.setEnabled(true), 2000);
    }

    @Override
    public void receivePublicKey(String encodedKey) {
        try {
            JSONObject jsonResponse = new JSONObject(encodedKey);
            String encodedPublicKey = jsonResponse.getString("publicKey");
            Log.d("MainActivity", "Received server public key: " + encodedPublicKey);

            byte[] decodedKey = Base64.decode(encodedPublicKey, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            serverPublicKey = keyFactory.generatePublic(keySpec); // Store the server's public key

        } catch (JSONException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(View view) {

        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);

        String algorithm = "AES";
        String password = "password12345678";

        secretKey = new SecretKeySpec(password.getBytes(), algorithm);

        String message = etMsg.getText().toString();
        if (secretKey != null && ivParameterSpec != null) {
            try {
                // Encrypt the message using the secret key and IV
                byte[] encryptedMessage = EncryptionUtil.encrypt(message, secretKey, ivParameterSpec);
                if (encryptedMessage != null) {
                    // Send the encrypted message to the server
                    HttpRequestTask httpRequestTask = new HttpRequestTask(this, encryptedMessage);
                    httpRequestTask.execute(localHost, ApiEndPoints.SEND_MESSAGE);
                } else {
                    Toast.makeText(this, "Encryption failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Secret key or IV not available", Toast.LENGTH_SHORT).show();
        }
    }


    private byte[] encryptMessage(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    @Override
    public void decryptMessage(String toString) {
        // Handle decrypted messages if needed
    }
}