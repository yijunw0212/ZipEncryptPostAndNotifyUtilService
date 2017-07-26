package com.mede.zepan;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessProcessInfo {
    
    private List<SuccessFileInfo> fileInfos = new ArrayList<>();

    @JsonProperty
    public List<SuccessFileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<SuccessFileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }
    
    public void addFileInfo(SuccessFileInfo fileInfo) {
        this.fileInfos.add(fileInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SuccessProcessInfo that = (SuccessProcessInfo) o;

        return fileInfos != null ? fileInfos.equals(that.fileInfos) : that.fileInfos == null;

    }

    @Override
    public int hashCode() {
        return fileInfos != null ? fileInfos.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SuccessProcessInfo{");
        sb.append("fileInfos=")
          .append(fileInfos);
        sb.append('}');
        return sb.toString();
    }
}
