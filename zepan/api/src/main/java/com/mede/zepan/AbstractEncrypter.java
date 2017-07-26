package com.mede.zepan;

import java.io.InputStream;

import com.mede.zepan.configs.EncryptionConfig;
import com.mede.zepan.exceptions.ZepanException;

/**
 *
 */
public abstract class AbstractEncrypter implements InputStreamDecorator {

    protected EncryptionConfig config;

    public AbstractEncrypter(EncryptionConfig encryptionConfig, FileInfo info) {
        this.config = encryptionConfig;
        if(config.getSecret() == null || config.getSecret().length() == 0) {
            throw new IllegalArgumentException("Encryption requires a secret.");
        }
        info.setEncrypted(true);
        info.setEncryptionAlg(encryptionConfig.getAlg());
        info.setEncryptionType(encryptionConfig.getType());
    }

    public abstract InputStream decorate(InputStream inputStream) throws ZepanException;

}
