package com.mede.zepan;

import com.mede.zepan.configs.EncryptionConfig;

/**
 *
 */
public interface EncrypterFactory {

    InputStreamDecorator newEncrypter(EncryptionConfig config, FileInfo into);
}
