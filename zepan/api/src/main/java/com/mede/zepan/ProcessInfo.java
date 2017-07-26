package com.mede.zepan;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mede.zepan.configs.RequestConfiguration;

/**
 * Created by JWang on 6/4/2017.
 */
public class ProcessInfo {

    
    private RequestConfiguration requestConfiguration;
    private List<FileInfo> fileInfos = new ArrayList<>();

    public ProcessInfo() {
    }

    @JsonProperty
    public RequestConfiguration getRequestConfiguration() {
        return requestConfiguration;
    }

    public void setRequestConfiguration(RequestConfiguration requestConfiguration) {
        this.requestConfiguration = requestConfiguration;
    }

    @JsonProperty
    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public void addFileInfos(List<FileInfo> fileInfos) {this.fileInfos.addAll(fileInfos);}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcessInfo that = (ProcessInfo) o;

        if (requestConfiguration != null ? !requestConfiguration.equals(that.requestConfiguration)
            : that.requestConfiguration != null) {
            return false;
        }
        return fileInfos != null ? fileInfos.equals(that.fileInfos) : that.fileInfos == null;

    }

    @Override
    public int hashCode() {
        int result = requestConfiguration != null ? requestConfiguration.hashCode() : 0;
        result = 31 * result + (fileInfos != null ? fileInfos.hashCode() : 0);
        return result;
    }
}
