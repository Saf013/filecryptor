package com.filecryptor.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@Slf4j
public class CryptoService {

    public Binary encryptToBinary(String algoritm, MultipartFile inputFile, String encryptionKey, int mod) throws Exception {
        Key secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), algoritm);
        if(algoritm.equals("DESede")) {
            secretKey = getDesKey(encryptionKey);
        }
        //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance(algoritm);
        cipher.init(mod, secretKey);

        InputStream input = inputFile.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
            if (encryptedBytes != null) {
                output.write(encryptedBytes);
            }
        }

        byte[] finalEncryptedBytes = cipher.doFinal();
        if (finalEncryptedBytes != null) {
            output.write(finalEncryptedBytes);
        }


        return new Binary(BsonBinarySubType.BINARY, output.toByteArray());
    }

/*    @SneakyThrows
    public Key getAesKey(String key) {
        byte[] decode = Base64.getDecoder().decode(key.getBytes());
        //DESedeKeySpec keySpec = new DESedeKeySpec(decode);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");

        return secretKeyFactory.generateSecret(keySpec);
    }*/

    @SneakyThrows
    public Key getDesKey(String key) {
        byte[] decode = Base64.getDecoder().decode(key.getBytes());
        DESedeKeySpec keySpec = new DESedeKeySpec(decode);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
        return secretKeyFactory.generateSecret(keySpec);
    }

    public String validateDesKey(String key) {

        try {
            byte[] decode = Base64.getDecoder().decode(key.getBytes());
            if (decode.length != 32) {
                return "У ключа не правильний розмір";
            }
            DESedeKeySpec keySpec = new DESedeKeySpec(decode);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException e) {
            return "Неправильний ключ";
        }

        return "valid";
    }


    public static boolean isValidAESKeyString(String keyString) {
        try {

            Key secret = new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02X", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
