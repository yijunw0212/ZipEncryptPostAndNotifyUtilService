package com.mede.zepan.encryption;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import com.mede.zepan.AbstractEncrypter;
import com.mede.zepan.EncrypterFactory;
import com.mede.zepan.InputStreamDecorator;
import com.mede.zepan.configs.EncryptionConfig;
import com.mede.zepan.exceptions.EncryptException;
import com.mede.zepan.exceptions.ZepanException;
import com.mede.zepan.FileInfo;

/**
 * This uses AES with 256 key size and CBC. The IV is generated for each run and
 * is stored as 16 bytes at the beginning of the stream, unencrypted.
 * 
 * The salt is not stored in the stream but is known upfront.
 */
public class AesCbcEncrypter extends AbstractEncrypter {
    
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public AesCbcEncrypter(EncryptionConfig encryptionConfig, FileInfo info) {
        super(encryptionConfig, info);
    }

    @Override
    public InputStream decorate(InputStream in) throws ZepanException {
        try {
            char[] password = config.getSecret().toCharArray();
            byte[] salt = config.getSalt().getBytes(StandardCharsets.UTF_8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            AlgorithmParameters parameters = cipher.getParameters();
            byte[] iv = parameters.getParameterSpec(IvParameterSpec.class).getIV();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return new CompoundInputStream(iv, new CipherInputStream(in, cipher));
        } catch (Exception e) {
            throw new EncryptException(e);
        }
    }
    
    public static class AesCbcEncrypterFactory implements EncrypterFactory {

        @Override
        public InputStreamDecorator newEncrypter(EncryptionConfig config, FileInfo info) {
            return new AesCbcEncrypter(config, info);
        }
    }

}
