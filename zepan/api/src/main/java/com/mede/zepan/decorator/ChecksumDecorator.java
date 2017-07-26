package com.mede.zepan.decorator;

import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mede.zepan.OutputStreamDecorator;
import com.mede.zepan.exceptions.ZepanException;

/**
 *
 */
public class ChecksumDecorator implements OutputStreamDecorator {
    
    private String digestType;
    private MessageDigest md;

    public ChecksumDecorator(String digestType) throws ZepanException {
        this.digestType = digestType;
    }

    @Override
    public OutputStream decorate(OutputStream out) throws ZepanException {
        try {
            md = MessageDigest.getInstance(digestType);
            return new DigestOutputStream(out, md);
        } catch (NoSuchAlgorithmException e) {
            throw new ZepanException(e);
        }
        
    }
    
    public String getChecksum() {
        byte[] bytes = md.digest();
        return createHash(bytes);
    }

    private String createHash(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16)
                             .substring(1));
        }
        return sb.toString();
    }

    public String getDigestType() {
        return digestType;
    }
}
