package com.mede.zepan;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JWang on 5/25/2017.
 */
public class FileInfo {
    
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    
    private String inputPath;
    private String outputPath;
    private String checksumType;
    private String checksum;
    private Boolean zipped = false;
    private Boolean encrypted = false;
    private String encryptionType;
    private String encryptionAlg;
    private String error;
    private String errorDescription;
    private String postURL;
    private String postStatus = SUCCESS;

    @JsonProperty
    public String getInputPath() {
        return inputPath;
    }
    @JsonProperty
    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    @JsonProperty
    public String getOutputPath() {
        return outputPath;
    }
    @JsonProperty
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

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

    @JsonProperty
    public Boolean getZipped() {
        return zipped;
    }
    @JsonProperty
    public void setZipped(Boolean zipped) {
        this.zipped = zipped;
    }
    @JsonProperty
    public Boolean getEncrypted() {
        return encrypted;
    }
    @JsonProperty
    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }
    @JsonProperty
    public String getEncryptionType() {
        return encryptionType;
    }
    @JsonProperty
    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }
    @JsonProperty
    public String getEncryptionAlg() {
        return encryptionAlg;
    }
    @JsonProperty
    public void setEncryptionAlg(String encryptionAlg) {
        this.encryptionAlg = encryptionAlg;
    }
    @JsonProperty
    public String getError() {
        return error;
    }
    @JsonProperty
    public void setError(String error) {
        this.error = error;
    }
    @JsonProperty
    public String getErrorDescription() {
        return errorDescription;
    }
    @JsonProperty
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    @JsonProperty
    public String getPostURL() {
        return postURL;
    }
    @JsonProperty
    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }
    @JsonProperty
    public String getPostStatus() {
        return postStatus;
    }
    @JsonProperty
    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public FileInfo copy() {
        
        return new FileInfo(this.inputPath, this.outputPath, this.checksumType, this.checksum,
            this.zipped, this.encrypted, this.encryptionType, this.encryptionAlg, this.error,
            this.errorDescription, this.postURL, this.postStatus);
    }

//    @JsonProperty
//    public void updateAllNotifications(String url, String Status){
//
//    }

    public FileInfo(){}

    public FileInfo(
            String InputPath,
            String OutputPath,
            String checksumType,
            String checksum,
            Boolean Zipped,
            Boolean Encrypted,
            String EncryptionType,
            String EncryptionAlg,
            String Error,
            String ErrorDescription,
            String PostURL,
            String PostStatus){
        this.inputPath = InputPath;
        this.outputPath=OutputPath;
        this.checksumType = checksumType;
        this.checksum = checksum;
        this.zipped=Zipped;
        this.encrypted=Encrypted;
        this.encryptionType=EncryptionType;
        this.encryptionAlg=EncryptionAlg;
        this.error=Error;
        this.errorDescription=ErrorDescription;
        this.postURL=PostURL;
        this.postStatus=PostStatus;

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileInfo{");
        sb.append("inputPath='")
          .append(inputPath)
          .append('\'');
        sb.append(", outputPath='")
          .append(outputPath)
          .append('\'');
        sb.append(", checksumType='")
          .append(checksumType)
          .append('\'');
        sb.append(", checksum='")
          .append(checksum)
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
        sb.append(", error='")
          .append(error)
          .append('\'');
        sb.append(", errorDescription='")
          .append(errorDescription)
          .append('\'');
        sb.append(", postURL='")
          .append(postURL)
          .append('\'');
        sb.append(", postStatus='")
          .append(postStatus)
          .append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileInfo fileInfo = (FileInfo) o;

        if (inputPath != null ? !inputPath.equals(fileInfo.inputPath)
            : fileInfo.inputPath != null) {
            return false;
        }
        if (outputPath != null ? !outputPath.equals(fileInfo.outputPath)
            : fileInfo.outputPath != null) {
            return false;
        }
        if (checksumType != null ? !checksumType.equals(fileInfo.checksumType)
            : fileInfo.checksumType != null) {
            return false;
        }
        if (checksum != null ? !checksum.equals(fileInfo.checksum) : fileInfo.checksum != null) {
            return false;
        }
        if (zipped != null ? !zipped.equals(fileInfo.zipped) : fileInfo.zipped != null) {
            return false;
        }
        if (encrypted != null ? !encrypted.equals(fileInfo.encrypted)
            : fileInfo.encrypted != null) {
            return false;
        }
        if (encryptionType != null ? !encryptionType.equals(fileInfo.encryptionType)
            : fileInfo.encryptionType != null) {
            return false;
        }
        if (encryptionAlg != null ? !encryptionAlg.equals(fileInfo.encryptionAlg)
            : fileInfo.encryptionAlg != null) {
            return false;
        }
        if (error != null ? !error.equals(fileInfo.error) : fileInfo.error != null) {
            return false;
        }
        if (errorDescription != null ? !errorDescription.equals(fileInfo.errorDescription)
            : fileInfo.errorDescription != null) {
            return false;
        }
        if (postURL != null ? !postURL.equals(fileInfo.postURL) : fileInfo.postURL != null) {
            return false;
        }
        return postStatus != null ? postStatus.equals(fileInfo.postStatus)
            : fileInfo.postStatus == null;

    }

    @Override
    public int hashCode() {
        int result = inputPath != null ? inputPath.hashCode() : 0;
        result = 31 * result + (outputPath != null ? outputPath.hashCode() : 0);
        result = 31 * result + (checksumType != null ? checksumType.hashCode() : 0);
        result = 31 * result + (checksum != null ? checksum.hashCode() : 0);
        result = 31 * result + (zipped != null ? zipped.hashCode() : 0);
        result = 31 * result + (encrypted != null ? encrypted.hashCode() : 0);
        result = 31 * result + (encryptionType != null ? encryptionType.hashCode() : 0);
        result = 31 * result + (encryptionAlg != null ? encryptionAlg.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (errorDescription != null ? errorDescription.hashCode() : 0);
        result = 31 * result + (postURL != null ? postURL.hashCode() : 0);
        result = 31 * result + (postStatus != null ? postStatus.hashCode() : 0);
        return result;
    }
}
