package com.NadraWraper.Utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class AESKeyProvider {

    @Value("${aes.fixed.key}")
    private String keyString;

    @Value("${aes.fixed.iv}")
    private String ivString;

    private SecretKey secretKey;
    private byte[] iv;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(keyString.getBytes(), "AES");
        this.iv = ivString.getBytes();
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getIv() {
        return iv;
    }
}
