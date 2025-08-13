package com.dflex.emkopo.commons.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Component
public class DigitalSignature {
    @Value("${dflex.key.password}")
    private String privateKeyPass;

    @Value("${dflex.key.private}")
    private String privateKeyFilePath;

    @Value("${dflex.key.alias}")
    private String privateKeyAlias;



    private static final Logger logger = LogManager.getLogger(DigitalSignature.class);


    String BASE_PATH = "";
    boolean verifySig = true;

    public DigitalSignature() {
    }
    private PrivateKey getPrivateKey(String keyPass, String keyAlias, String keyFilePath) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream is = new FileInputStream(keyFilePath);
        keyStore.load(is, keyPass.toCharArray());
        return (PrivateKey) keyStore.getKey(keyAlias, keyPass.toCharArray());
    }

    public String createSignature(String content) throws Exception {
        byte[] data = content.getBytes(StandardCharsets.UTF_8); // Ensure UTF-8 encoding
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(this.getPrivateKey(privateKeyPass, privateKeyAlias, privateKeyFilePath));
        sig.update(data);
        byte[] signatureBytes = sig.sign();
        String base64Signature = Base64.encodeBase64String(signatureBytes);

        System.out.println("Signature Length Expected: " + signatureBytes.length);
        System.out.println("Base64 Encoded Signature: " + base64Signature);
        System.out.println("Base64 Encoded Signature Length: " + base64Signature.length());

        return base64Signature;
    }

    private PublicKey getPublicKey(String keyFilePath) throws Exception {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        FileInputStream fis = new FileInputStream(keyFilePath);
        Certificate cert = factory.generateCertificate(fis);
        return cert.getPublicKey();
    }

    public boolean verifySignature(String signature, String content, String publicKeyFilePath) throws Exception {
        if (!this.verifySig) {
            return true;
        } else {
            boolean isValid = false;
            try {
                // Decode the base64 signature
                byte[] signatureBytes = Base64.decodeBase64(signature);

                // Create Signature instance
                Signature sig = Signature.getInstance("SHA256withRSA");

                // Get bytes of the content
                byte[] data = content.getBytes(StandardCharsets.UTF_8);

                // Initialize Signature with public key
                sig.initVerify(this.getPublicKey(publicKeyFilePath));

                // Update signature instance with data
                sig.update(data);

                // Print decoded signature length and base64 representation
                System.out.println("*************Signature Length result******************: " + signatureBytes.length);
                System.out.println("*******Base64 Decoded Signature*******: " + Base64.encodeBase64String(signatureBytes));

                // Verify the signature
                isValid = sig.verify(signatureBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isValid;
        }
    }
}

//    private PublicKey getPublicKey(String keyPass, String keyAlias, String keyFilePath) throws Exception {
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        FileInputStream is = new FileInputStream(this.BASE_PATH + keyFilePath);
//        keyStore.load(is, keyPass.toCharArray());
//        Certificate cert = keyStore.getCertificate(keyAlias);
//        PublicKey publicKey = cert.getPublicKey();
//        return publicKey;
//    }
//
//    public boolean verifySignature(String signature, String content, String publicKeyPass, String publicKeyAlias, String publicKeyFilePath) throws Exception {
//        if (!this.verifySig) {
//            return true;
//        } else {
//            boolean t = false;
//
//            try {
//                byte[] db = Base64.decodeBase64(signature.getBytes());
//                Signature sig = Signature.getInstance("SHA256withRSA");
//                byte[] data = content.getBytes();
//                sig.initVerify(this.getPublicKey(publicKeyPass, publicKeyAlias, publicKeyFilePath));
//                sig.update(data);
//                t = sig.verify(db);
//            } catch (Exception var10) {
//                var10.printStackTrace();
//            }
//
//            return t;
//        }
//    }
//}
