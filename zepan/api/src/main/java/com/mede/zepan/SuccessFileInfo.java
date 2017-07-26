package com.mede.zepan;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class SuccessFileInfo {

    private String fileName;
    private Boolean zipped = false;
    private Boolean encrypted = false;
    private String encryptionType;
    private String encryptionAlg;
    private String postUrl;
    private String checksumType;
    private String checksum;

    @JsonProperty
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty
    public Boolean getZipped() {
        return zipped;
    }

    public void setZipped(Boolean zipped) {
        this.zipped = zipped;
    }

    @JsonProperty
    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }

    @JsonProperty
    public String getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }

    @JsonProperty
    public String getEncryptionAlg() {
        return encryptionAlg;
    }

    public void setEncryptionAlg(String encryptionAlg) {
        this.encryptionAlg = encryptionAlg;
    }

    @JsonProperty
    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    @JsonProperty
    public String getChecksumType() {
        return checksumType;
    }

    public void setChecksumType(String checksumType) {
        this.checksumType = checksumType;
    }

    @JsonProperty
    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SuccessFileInfo that = (SuccessFileInfo) o;

        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) {
            return false;
        }
        if (zipped != null ? !zipped.equals(that.zipped) : that.zipped != null) {
            return false;
        }
        if (encrypted != null ? !encrypted.equals(that.encrypted) : that.encrypted != null) {
            return false;
        }
        if (encryptionType != null ? !encryptionType.equals(that.encryptionType)
            : that.encryptionType != null) {
            return false;
        }
        if (encryptionAlg != null ? !encryptionAlg.equals(that.encryptionAlg)
            : that.encryptionAlg != null) {
            return false;
        }
        if (postUrl != null ? !postUrl.equals(that.postUrl) : that.postUrl != null) {
            return false;
        }
        if (checksumType != null ? !checksumType.equals(that.checksumType)
            : that.checksumType != null) {
            return false;
        }
        return checksum != null ? checksum.equals(that.checksum) : that.checksum == null;

    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (zipped != null ? zipped.hashCode() : 0);
        result = 31 * result + (encrypted != null ? encrypted.hashCode() : 0);
        result = 31 * result + (encryptionType != null ? encryptionType.hashCode() : 0);
        result = 31 * result + (encryptionAlg != null ? encryptionAlg.hashCode() : 0);
        result = 31 * result + (postUrl != null ? postUrl.hashCode() : 0);
        result = 31 * result + (checksumType != null ? checksumType.hashCode() : 0);
        result = 31 * result + (checksum != null ? checksum.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SuccessFileInfo{");
        sb.append("fileName='")
          .append(fileName)
          .append('\'');
        sb.append(", zipped=")
          .append(zipped);
        sb.append(", encrypted=")
          .append(encrypted);
        sb.append(", encryptionType='")
          .append(encryptionType)
          .append('\'');
        sb.append(", encryptionAlg='")
          .append(encryptionAlg)
          .append('\'');
        sb.append(", postUrl='")
          .append(postUrl)
          .append('\'');
        sb.append(", checksumType='")
          .append(checksumType)
          .append('\'');
        sb.append(", checksum='")
          .append(checksum)
          .append('\'');
        sb.append('}');
        return sb.toString();
    }
}
