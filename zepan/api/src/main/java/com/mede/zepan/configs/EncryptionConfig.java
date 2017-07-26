package com.mede.zepan.configs;

/**
 * Created by JWang on 5/25/2017.
 */
public class EncryptionConfig {
    
    public static final String SYMMETRIC = "SYMMETRIC";
    private String alg;
    private String type;
    private String secret;
    private String salt;

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EncryptionConfig that = (EncryptionConfig) o;

        if (alg != null ? !alg.equals(that.alg) : that.alg != null) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (secret != null ? !secret.equals(that.secret) : that.secret != null) {
            return false;
        }
        return salt != null ? salt.equals(that.salt) : that.salt == null;

    }

    @Override
    public int hashCode() {
        int result = alg != null ? alg.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }
}
