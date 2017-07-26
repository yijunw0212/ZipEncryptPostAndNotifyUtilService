package com.mede.zepan.encryption;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
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
 * This uses AES.
 * If no salt is provided, then the secret must 16 (128 bit) or 32 (256 bit) chars long.
 * Otherwise the salt is used with the secret to derive a 256 bit key.
 * The salt is not stored in the stream but is known upfront.
 */
public class AesEncrypter extends AbstractEncrypter {
    
    public static final String ALGORITHM = "AES";

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    
    
    public AesEncrypter(EncryptionConfig encryptionConfig, FileInfo info) {
        super(encryptionConfig, info);
    }

    @Override
    public InputStream decorate(InputStream in) throws ZepanException {
        try {
            SecretKey secretKey;
            if(config.getSalt() == null) {
                String key = config.getSecret();
                if(key.length() != 16 && key.length() != 32) {
                    throw new EncryptException("Unsalted secret must be 16 or 32 chars long for " + ALGORITHM + " encryption.");
                }
                secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            } else {
                char[] password = config.getSecret()
                                        .toCharArray();
                byte[] salt = config.getSalt()
                                    .getBytes(StandardCharsets.UTF_8);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
                SecretKey tmp = factory.generateSecret(spec);
                secretKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
            }
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new EncryptException(e);
        }
    }
    
    public static class AesEncrypterFactory implements EncrypterFactory {

        @Override
        public InputStreamDecorator newEncrypter(EncryptionConfig config, FileInfo info) {
            return new AesEncrypter(config, info);
        }
    }

}
