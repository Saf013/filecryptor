package com.filecryptor.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceTest {



    @SneakyThrows
    @Test
    void encrypt() {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encoded = cipher.doFinal(key.getEncoded());
        String en = Base64.getEncoder().encodeToString(encoded);
        //System.out.println(Base64.getEncoder().encode(en.getBytes()).length);
        System.out.println(en);
        assertEquals("", key);
    }

    @SneakyThrows
    @Test
    void isValidAESKeyString() {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encoded = cipher.doFinal(key.getEncoded());
        String en = Base64.getEncoder().encodeToString(encoded);
        System.out.println("00112233445566778899AABBCCDDEEFF".length());
        assertEquals(true, CryptoService.isValidAESKeyString("00112233445566778899AABBCCDDEEFF"));
    }
}