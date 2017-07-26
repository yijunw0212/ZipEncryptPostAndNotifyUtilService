package com.mede.zepan.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import com.mede.zepan.configs.EncryptionConfig;
import com.mede.zepan.encryption.AesCbcEncrypter;
import com.mede.zepan.encryption.AesEncrypter;
import com.mede.zepan.FileInfo;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class EncryptTest {
    
    @Test
    public void testAesCbcEncryption() throws Exception {
        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES/CBC/PKCS5Padding");
        encryptionConfig.setSecret("secret");
        encryptionConfig.setSalt("foobar");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);
        AesCbcEncrypter decorator = new AesCbcEncrypter(encryptionConfig, new FileInfo());
        
        String test = "Hello.\nI am a string that is going to be encrypted.";
        InputStream inputStream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        inputStream = decorator.decorate(inputStream);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int c;
        byte[] bytes = new byte[1024];
        while((c = inputStream.read(bytes)) != -1) {
            bout.write(bytes, 0, c);
        }
        
        inputStream.close();
        bout.flush();
        bout.close();
        char[] password = encryptionConfig.getSecret()
                                          .toCharArray();
        byte[] salt = encryptionConfig.getSalt()
                                      .getBytes(StandardCharsets.UTF_8);
        
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        InputStream stream = new ByteArrayInputStream(bout.toByteArray());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(readIv(stream)));
        InputStream in =  new CipherInputStream(stream, cipher);
        StringBuilder sb = new StringBuilder();
        bytes = new byte[1024];
        while ((c = in.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, c));
        }
        System.out.println(sb.toString());
        assertEquals(sb.toString(), test);
        
        
    }



    @Test
    public void testAesEncryption() throws Exception {
        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES");
        encryptionConfig.setSecret("secret");
        encryptionConfig.setSalt("foobar");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);
        AesEncrypter decorator = new AesEncrypter(encryptionConfig, new FileInfo());

        String test = "Hello.\nI am a string that is going to be encrypted.";
        InputStream inputStream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        inputStream = decorator.decorate(inputStream);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int c;
        byte[] bytes = new byte[1024];
        while ((c = inputStream.read(bytes)) != -1) {
            bout.write(bytes, 0, c);
        }

        inputStream.close();
        bout.flush();
        bout.close();
        char[] password = encryptionConfig.getSecret()
                                          .toCharArray();
        byte[] salt = encryptionConfig.getSalt()
                                      .getBytes(StandardCharsets.UTF_8);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        InputStream stream = new ByteArrayInputStream(bout.toByteArray());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        InputStream in = new CipherInputStream(stream, cipher);
        StringBuilder sb = new StringBuilder();
        bytes = new byte[1024];
        while ((c = in.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, c));
        }
        System.out.println(sb.toString());
        assertEquals(sb.toString(), test);

    }

    @Test
    public void testAesEncryptionWithNoSalt16() throws Exception {
        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES");
        encryptionConfig.setSecret("iamsixteenlong00");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);
        AesEncrypter decorator = new AesEncrypter(encryptionConfig, new FileInfo());

        String test = "Hello.\nI am a string that is going to be encrypted.";
        InputStream inputStream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        inputStream = decorator.decorate(inputStream);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int c;
        byte[] bytes = new byte[1024];
        while ((c = inputStream.read(bytes)) != -1) {
            bout.write(bytes, 0, c);
        }

        inputStream.close();
        bout.flush();
        bout.close();
        
        SecretKey secretKey = new SecretKeySpec(encryptionConfig.getSecret().getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        InputStream stream = new ByteArrayInputStream(bout.toByteArray());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        InputStream in = new CipherInputStream(stream, cipher);
        StringBuilder sb = new StringBuilder();
        bytes = new byte[1024];
        while ((c = in.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, c));
        }
        System.out.println(sb.toString());
        assertEquals(sb.toString(), test);

    }

    @Test
    public void testAesEncryptionWithNoSalt32() throws Exception {
        EncryptionConfig encryptionConfig = new EncryptionConfig();
        encryptionConfig.setAlg("AES");
        encryptionConfig.setSecret("iamsixteenlong00iamsixteenlong00");
        encryptionConfig.setType(EncryptionConfig.SYMMETRIC);
        AesEncrypter decorator = new AesEncrypter(encryptionConfig, new FileInfo());

        String test = "Hello.\nI am a string that is going to be encrypted.";
        InputStream inputStream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        inputStream = decorator.decorate(inputStream);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int c;
        byte[] bytes = new byte[1024];
        while ((c = inputStream.read(bytes)) != -1) {
            bout.write(bytes, 0, c);
        }

        inputStream.close();
        bout.flush();
        bout.close();

        SecretKey secretKey = new SecretKeySpec(encryptionConfig.getSecret()
                                                                .getBytes(StandardCharsets.UTF_8),
            "AES");
        Cipher cipher = Cipher.getInstance("AES");
        InputStream stream = new ByteArrayInputStream(bout.toByteArray());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        InputStream in = new CipherInputStream(stream, cipher);
        StringBuilder sb = new StringBuilder();
        bytes = new byte[1024];
        while ((c = in.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, c));
        }
        System.out.println(sb.toString());
        assertEquals(sb.toString(), test);

    }
    
    private byte[] readIv(InputStream in) throws IOException {
        byte[] iv = new byte[16];
        int c = in.read(iv);
        if(c == 16) {
            System.out.println("IV: " + new String(iv));
            return iv;
        }
        throw new IOException("Failed to read 16 byte IV");
    }

}
