const https = require('https');
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const options = {
  key: fs.readFileSync(path.join(__dirname, 'android_key.pem')),
  cert: fs.readFileSync(path.join(__dirname, 'cert.pem'))
};
const { publicKey, privateKey } = crypto.generateKeyPairSync('ec', {namedCurve: 'secp384r1' });

app.use(express.json());
app.use(bodyParser.text({ type: 'text/*' }));
app.use((req, res, next) => {
    console.log(`Incoming request: ${req.method} ${req.url}`);
    next();
  });
  
console.log('Server private key with EC:', privateKey);
console.log('Server public key with EC:', publicKey);

let derivedKey = null;

app.get('/', (req, res) => {
    res.send('Hello, this is an HTTPS server!');
  });

app.get('/api/getPublicKey', (req, res) => {
  
  const serverPublicKey = publicKey.export({type: 'spki', format: 'der'});
  const encodedPublicKey = serverPublicKey.toString('base64');
  console.log('Server public key:', encodedPublicKey);
  res.json({ publicKey: encodedPublicKey });
})

app.post('/api/postPublicKey', (req, res) => {
    const clientPublicKey = req.body;
    // Derive the shared secret using the recipient's public key
    console.log('Received Base64 client public key:', clientPublicKey);
    const decodedPublicKey = Buffer.from(clientPublicKey,'base64');
    console.log('Received decoded client public key:', decodedPublicKey);
    
    const publicKeyObject = crypto.createPublicKey({
      key: decodedPublicKey,
      format: 'der', 
      type: 'spki',
    });
    
    const sharedSecret = crypto.diffieHellman({ privateKey: privateKey, publicKey: publicKeyObject});
    // Your shared secret from Diffie-Hellman (should be Buffer)
    //const sharedSecret = getSharedSecretFromDiffieHelman(); // Replace with your actual shared secret

    // Define the desired key length (e.g., 32 bytes for AES-256)
    const keyLength = 32;
    // Use a Key Derivation Function (KDF) to derive the AES encryption key
    console.log('Server computed shared secret:', sharedSecret.toString('hex'));
    console.log('Server computed shared secret:', sharedSecret.toString('base64'));

    derivedKey = crypto.pbkdf2Sync(sharedSecret.toString('base64'), 'mysalt', 100000, keyLength, 'sha256');
    
    console.log('Server computed derived key:', derivedKey.toString('hex'));
    console.log('Server computed derived key:', derivedKey.toString('base64'));

    res.send('Public key for the client was received successfully');
});

app.post('/api/postMessage', (req, res) =>{

  const message = req.body;
  console.log('Received (encrypted message):', message);

  const messageEncryptedBuffer = Buffer.from(message, 'base64');

  // 2. Choose an encryption algorithm
  const algorithm = 'aes-256-cbc';

  // 3. Get the Initialization Vector (IV) from the message
  const iv = messageEncryptedBuffer.slice(0, 16);
  // const iv = Array.from(buffer);

  // 4. Create a cipher object for encryption with the IV
  console.log("Derived key: " + derivedKey.toString('hex'));
  console.log("IV: " + iv.toString('hex'));
  const decipher = crypto.createDecipheriv(algorithm, derivedKey, iv);
  
  // 5. Decrypt the message
  const encryptedData = messageEncryptedBuffer.slice(16, messageEncryptedBuffer.length);
  // Update and finalize the decipher object with the ciphertext  
  let decrypted = decipher.update(encryptedData, 'binary', 'binary');
  decipher.setAutoPadding(true);
  decrypted += decipher.final('binary');

  console.log('Received (plain message): ', decrypted);

  // Create a cipher object with the AES algorithm and mode
  const cipher = crypto.createCipheriv('aes-256-cbc', derivedKey, iv);

  // 6. Encrypt the message response message
  const responeMsg = "Client message: " + decrypted + ", Server says: Thank you!";
  let encrypted = cipher.update(responeMsg, 'utf8', 'base64');
  encrypted += cipher.final('base64');
  
  res.send(encrypted);  
});

const port = 443; // Replace with the desired port number for HTTPS (typically 443)

https.createServer(options, app).listen(port, () => {
  console.log(`Server running on https://localhost:${port}/`);
});

