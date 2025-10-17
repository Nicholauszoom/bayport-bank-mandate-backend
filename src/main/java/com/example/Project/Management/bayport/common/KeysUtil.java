package com.example.Project.Management.bayport.common;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Component
@Log4j2
@Data
public class KeysUtil {
    private final String encAlgo = "RSA";
//    private final PublicKey publicKey;
//    private final PrivateKey privateKey;
//
//    @Autowired
//    public KeysUtil() throws NoSuchAlgorithmException {
////        log.info("Generating security keys.");
//        KeyPair keyPair = generateKeyPair();
//        // Get Public and Private Keys for users
//        publicKey  = keyPair.getPublic();
//        privateKey = keyPair.getPrivate();
////        log.info("Generated key pair.");
//    }

    // Helper method to convert bytes to hex string
    public  String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    public PrivateKey stringToPrivateKey(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Hex.decode(privateKeyString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(encAlgo); // Or the algorithm you're using
        return keyFactory.generatePrivate(spec);
    }

    public PublicKey stringToPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Hex.decode(publicKeyString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(encAlgo); // Or the algorithm you're using
        return keyFactory.generatePublic(spec);
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(encAlgo);
        keyPairGenerator.initialize(2048); // Key size (bits)
        return keyPairGenerator.generateKeyPair();
    }
}