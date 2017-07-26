package com.mede.zepan.configs;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by JWang on 5/25/2017.
 */
public class RequestConfiguration {

    @NotEmpty
    @JsonProperty
    private String inputDirectory;
    @JsonProperty
    private String outputDirectory;
    @JsonProperty
    private Boolean immediateAbort = Boolean.TRUE;
    @JsonProperty
    private String checksum;
    @JsonProperty
    private Boolean zip = Boolean.FALSE;
    @JsonProperty
    private Long zipStopSize;
    @JsonProperty
    private String zipName;

    @JsonProperty
    private Boolean threading;
    @JsonProperty
    private Integer threadingFileGroupSize;

    @JsonProperty
    private EncryptionConfig encryptionConfig;
    @JsonProperty
    private List<NotificationConfig> notificationConfigs = new ArrayList<>();
    @JsonProperty
    private PostConfig postConfig;



    public RequestConfiguration(){

    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }
    
    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Boolean getImmediateAbort() {
        return immediateAbort;
    }

    public void setImmediateAbort(Boolean immediateAbort) {
        this.immediateAbort = immediateAbort;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Boolean getZip() {
        return zip;
    }

    public void setZip(Boolean zip) {
        this.zip = zip;
    }

    public Long getZipStopSize() {
        return zipStopSize;
    }

    public void setZipStopSize(Long zipStopSize) {
        this.zipStopSize = zipStopSize;
    }

    public String getZipName() {
        return zipName;
    }

    public void setZipName(String zipName) {
        this.zipName = zipName;
    }

    public Boolean getThreading() {
        return threading;
    }

    public void setThreading(Boolean threading) {
        this.threading = threading;
    }

    /**
     * When using threading, the input files are split into groups.
     * This defines the max size of each group.
     * @return
     */
    public Integer getThreadingFileGroupSize() {
        return threadingFileGroupSize;
    }

    public void setThreadingFileGroupSize(Integer threadingFileGroupSize) {
        this.threadingFileGroupSize = threadingFileGroupSize;
    }

    public EncryptionConfig getEncryptionConfig() {
        return encryptionConfig;
    }

    public void setEncryptionConfig(EncryptionConfig encryptionConfig) {
        this.encryptionConfig = encryptionConfig;
    }

    public List<NotificationConfig> getNotificationConfigs() {
        return notificationConfigs;
    }

    public void setNotificationConfigs(List<NotificationConfig> notificationConfigs) {
        this.notificationConfigs = notificationConfigs;
    }

    public PostConfig getPostConfig() {
        return postConfig;
    }

    public void setPostConfig(PostConfig postConfig) {
        this.postConfig = postConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequestConfiguration that = (RequestConfiguration) o;

        if (inputDirectory != null ? !inputDirectory.equals(that.inputDirectory)
            : that.inputDirectory != null) {
            return false;
        }
        if (outputDirectory != null ? !outputDirectory.equals(that.outputDirectory)
            : that.outputDirectory != null) {
            return false;
        }
        if (immediateAbort != null ? !immediateAbort.equals(that.immediateAbort)
            : that.immediateAbort != null) {
            return false;
        }
        if (checksum != null ? !checksum.equals(that.checksum) : that.checksum != null) {
            return false;
        }
        if (zip != null ? !zip.equals(that.zip) : that.zip != null) {
            return false;
        }
        if (zipStopSize != null ? !zipStopSize.equals(that.zipStopSize)
            : that.zipStopSize != null) {
            return false;
        }
        if (zipName != null ? !zipName.equals(that.zipName) : that.zipName != null) {
            return false;
        }
        if (threading != null ? !threading.equals(that.threading) : that.threading != null) {
            return false;
        }
        if (threadingFileGroupSize != null ? !threadingFileGroupSize.equals(
            that.threadingFileGroupSize) : that.threadingFileGroupSize != null) {
            return false;
        }
        if (encryptionConfig != null ? !encryptionConfig.equals(that.encryptionConfig)
            : that.encryptionConfig != null) {
            return false;
        }
        if (notificationConfigs != null ? !notificationConfigs.equals(that.notificationConfigs)
            : that.notificationConfigs != null) {
            return false;
        }
        return postConfig != null ? postConfig.equals(that.postConfig) : that.postConfig == null;

    }

    @Override
    public int hashCode() {
        int result = inputDirectory != null ? inputDirectory.hashCode() : 0;
        result = 31 * result + (outputDirectory != null ? outputDirectory.hashCode() : 0);
        result = 31 * result + (immediateAbort != null ? immediateAbort.hashCode() : 0);
        result = 31 * result + (checksum != null ? checksum.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (zipStopSize != null ? zipStopSize.hashCode() : 0);
        result = 31 * result + (zipName != null ? zipName.hashCode() : 0);
        result = 31 * result + (threading != null ? threading.hashCode() : 0);
        result = 31 * result + (threadingFileGroupSize != null ? threadingFileGroupSize.hashCode()
            : 0);
        result = 31 * result + (encryptionConfig != null ? encryptionConfig.hashCode() : 0);
        result = 31 * result + (notificationConfigs != null ? notificationConfigs.hashCode() : 0);
        result = 31 * result + (postConfig != null ? postConfig.hashCode() : 0);
        return result;
    }
}
